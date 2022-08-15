package cloud.lihan.rewardsprogram.service.impl;

import cloud.lihan.rewardsprogram.common.constants.IncentiveValueRuleConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.PlanLimitConstant;
import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import cloud.lihan.rewardsprogram.common.utils.CurrentTimeUtil;
import cloud.lihan.rewardsprogram.dao.inner.PlanDao;
import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.entity.document.PlanDocument;
import cloud.lihan.rewardsprogram.manager.PlanManager;
import cloud.lihan.rewardsprogram.service.inner.PlanService;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.PlanVO;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 计划相关的业务细节
 *
 * @author hanyun.li
 * @createTime 2022/07/20 11:29:00
 */
@Slf4j
@Service("planService")
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanDao planDao;
    @Autowired
    private PlanManager planManager;
    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlan(PlanVO planVO) throws Exception {
        userService.increaseCreatePlanTimes(planVO.getUserId());
        planDao.createPlanDocument(planManager.planVOConvertPlanDocument(planVO));
    }

    @Override
    public void bulkSavePlan(List<PlanVO> planVOs) throws IOException {
        planDao.bulkCreatePlanDocument(planManager.planVOConvertsPlanDocument(planVOs));
    }

    @Override
    public void deletePlanById(String planId) throws IOException {
        planDao.deletePlanDocumentById(planId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishPlanById(UserDTO userDTO, String planId) throws IOException {
        // 指定主键
        Query query = new Query.Builder()
                .ids(id -> id.values(planId))
                .build();
        // 组装更新map
        Map<String, JsonData> optionMaps = new HashMap<>(IntegerConstant.ONE);
        optionMaps.put("isFinished", JsonData.of(Boolean.TRUE));
        String source = "ctx._source.isFinished = params.isFinished";
        planDao.updatePlanSingleField(optionMaps, source, query);
        // 增加激励值
        userService.increaseIncentiveValue(userDTO, IncentiveValueRuleConstant.DAY_PLAN);
    }

    @Override
    public PlanDTO getPlanByPlanId(String planId) throws IOException {
        PlanDocument planDocument = planDao.getPlanByPlanDocumentId(planId);
        return planManager.planDocumentConvertPlanDTO(planDocument);
    }

    @Override
    public List<PlanDTO> getTodayFinishedPlans(String userId) throws Exception {
        return this.getTodayPlans(Boolean.TRUE, userId);
    }

    @Override
    public List<PlanDTO> getTodayUnfinishedPlans(String userId) throws Exception {
        return this.getTodayPlans(Boolean.FALSE, userId);
    }

    @Override
    public Boolean checkPlanInfo(String planInfo, String userId) throws Exception {
        String todayTime = CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D);
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(s -> s.term(t -> t
                                // 注意：当planInfo字段内容过长时，此处会进行分词匹配，导致搜索不到结果，需要在字段后面添加"keyword"进行不分词搜索
                                .field("planInfo.keyword")
                                .value(planInfo))
                        )
                        .must(s -> s.term(t -> t
                                .field("isFinished")
                                .value(Boolean.FALSE))
                        )
                        .must(s -> s.term(t -> t
                                .field("userId.keyword")
                                .value(userId))
                        )
                        // 注意：此处使用matchPhrase而不使用match的原因在于match匹配会将入参也进行分词匹配，而matchPhrase则直接将入参数当成整体匹配
                        .must(s -> s.matchPhrase(m -> m
                                .field("createTime")
                                .query(todayTime)
                        ))
                ).build();
        List<PlanDTO> planDTOS = planManager.planDocumentsConvertPlanDTO(planDao.getMultiplePlanByQuery(query));
        return CollectionUtils.isEmpty(planDTOS) ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean canCreatePlan(UserDTO userDTO) throws Exception {
        if (Objects.isNull(userDTO)) {
            log.error("PlanServiceImpl.savePlan() is exit error! userDTO is null!");
        }

        // 获取当天的时间(eg:2022-8-4)
        String currentDayTime = CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D);
        // 对老用户进行初始化字段值"lastTimeAddPlanTime"
        if (Objects.isNull(userDTO.getLastTimeAddPlanTime())) {
            userService.initLastTimeAddPlanTime(userDTO.getId(), currentDayTime);
            // 此处等待字段"lastTimeAddPlanTime"初始化
            Thread.sleep(1000);
        }

        // 判断当天时间是否与上一次创建计划的时间是同一天
        if (CurrentTimeUtil.isSameDay(userDTO.getLastTimeAddPlanTime(), currentDayTime)) {
            Integer currentDayCreatePlanTimes = userDTO.getCurrentDayCreatePlanTimes();
            // 检测是否超过最大创建计划次数
            if (currentDayCreatePlanTimes >= PlanLimitConstant.CURRENT_DAY_CREATE_PLAN_MAX_TIMES) {
                log.warn("Exceeded maximum number of create plan " + PlanLimitConstant.CURRENT_DAY_CREATE_PLAN_MAX_TIMES + "times! The userId is: {}", userDTO.getId());
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        userService.resetCreatePlanTimes(userDTO.getId(), currentDayTime);
        return Boolean.TRUE;
    }

    /**
     * 获取今日的计划列表
     *
     * @param isFinished 是否已经完成 true:是 false:否
     * @return {@link List<PlanDTO>} 今日的计划列表
     * @throws Exception 包含es的I/O异常和时间转换的异常
     */
    private List<PlanDTO> getTodayPlans(Boolean isFinished, String userId) throws Exception {
        String todayTime = CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D);
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(s -> s.term(t -> t
                                .field("isFinished")
                                .value(isFinished))
                        )
                        .must(s -> s.term(t -> t
                                .field("userId.keyword")
                                .value(userId))
                        )
                        .must(s -> s.matchPhrase(m -> m
                                .field("createTime")
                                .query(todayTime)
                        ))
                )
                .build();
        return planManager.planDocumentsConvertPlanDTO(planDao.getMultiplePlanByQuery(query));
    }

}
