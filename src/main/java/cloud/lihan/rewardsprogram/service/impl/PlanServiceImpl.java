package cloud.lihan.rewardsprogram.service.impl;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import cloud.lihan.rewardsprogram.common.utils.CurrentTimeUtil;
import cloud.lihan.rewardsprogram.dao.inner.PlanDao;
import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.entety.document.PlanDocument;
import cloud.lihan.rewardsprogram.manager.PlanManager;
import cloud.lihan.rewardsprogram.service.inner.PlanService;
import cloud.lihan.rewardsprogram.vo.PlanVO;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计划相关的业务细节
 *
 * @author hanyun.li
 * @createTime 2022/07/20 11:29:00
 */
@Service("planService")
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanDao planDao;
    @Autowired
    private PlanManager planManager;

    @Override
    public void savePlan(PlanVO planVO) throws IOException {
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
    public void finishPlanById(String planId) throws IOException {
        // 指定主键
        Query query = new Query.Builder()
                .ids(id -> id.values(planId))
                .build();
        // 组装更新map
        Map<String, JsonData> optionMaps = new HashMap<>(IntegerConstant.ONE);
        optionMaps.put("isFinished", JsonData.of(Boolean.TRUE));
        String source = "ctx._source.isFinished = params.isFinished";
        planDao.updatePlanSingleField(optionMaps, source, query);
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
                        .should(s -> s.term(t -> t
                                .field("isFinished")
                                .value(isFinished))
                        )
                        .should(s -> s.term(t -> t
                                .field("userId")
                                .value(userId))
                        )
                        .should(s -> s.match(m -> m
                                .field("createTime")
                                // AUTO:开启模糊搜索
                                .fuzziness("AUTO")
                                .query(todayTime)
                        ))
                )
                .build();
        return planManager.planDocumentsConvertPlanDTO(planDao.getMultiplePlanByQuery(query));
    }

}
