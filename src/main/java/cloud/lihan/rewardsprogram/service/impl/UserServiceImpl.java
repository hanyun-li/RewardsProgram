package cloud.lihan.rewardsprogram.service.impl;

import cloud.lihan.rewardsprogram.common.constants.IncentiveValueRuleConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.LoginLimitConstant;
import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import cloud.lihan.rewardsprogram.common.utils.CurrentTimeUtil;
import cloud.lihan.rewardsprogram.dao.inner.UserDao;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.dto.provider.WishProviderDTO;
import cloud.lihan.rewardsprogram.entity.document.UserDocument;
import cloud.lihan.rewardsprogram.manager.UserManager;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.UserVO;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户相关的业务细节
 *
 * @author hanyun.li
 * @createTime 2022/07/08 16:48:00
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserManager userManager;

    @Override
    public void savaUser(UserVO userVO) throws Exception {
        userDao.createUserDocument(userManager.userVOConvertUserDocument(userVO));
    }

    @Override
    public void failLogin(String userId) throws Exception {
        UserDTO userDTO = this.getUserByUserId(userId);
        if (Objects.isNull(userDTO)) {
            log.error("UserServiceImpl.failLogin() exit error! userDTO is null!");
            return;
        }

        Query query = new Query.Builder().ids(i -> i.values(userId)).build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        Integer currentDayLoginFailTimes = userDTO.getCurrentDayLoginFailTimes();
        currentDayLoginFailTimes += IntegerConstant.ONE;
        optionsMap.put("currentDayLoginFailTimes", JsonData.of(currentDayLoginFailTimes));
        String source = "ctx._source.currentDayLoginFailTimes = params.currentDayLoginFailTimes";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPassword(String userId, String userPassword) throws Exception {
        // 获取当天的时间(eg:2022-8-4)
        String currentDayTime = CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D);
        Query query = new Query.Builder()
                .ids(t -> t.values(userId))
                .build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.THERE);
        optionsMap.put("password", JsonData.of(userPassword));
        optionsMap.put("lastTimeLoginFailTime", JsonData.of(currentDayTime));
        optionsMap.put("currentDayLoginFailTimes", JsonData.of(IntegerConstant.ZERO));
        String source = "ctx._source.password = params.password;" +
                "ctx._source.lastTimeLoginFailTime = params.lastTimeLoginFailTime;" +
                "ctx._source.currentDayLoginFailTimes = params.currentDayLoginFailTimes";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void editUserInfo(UserVO userVO) throws IOException {
        Query query = new Query.Builder()
                .ids(t -> t.values(userVO.getUserId()))
                .build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.THERE);
        optionsMap.put("nickName", JsonData.of(userVO.getNickName()));
        optionsMap.put("userName", JsonData.of(userVO.getUserName()));
        optionsMap.put("userEmail", JsonData.of(userVO.getUserEmail()));
        String source = "ctx._source.nickName = params.nickName;" +
                "ctx._source.userName = params.userName;" +
                "ctx._source.userEmail = params.userEmail";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void increaseIncentiveValue(UserDTO userDTO, Integer increaseIncentiveValue) throws IOException {
        Query query = new Query.Builder()
                .ids(t -> t.values(userDTO.getId()))
                .build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        // 适配旧版本还没有激励值的用户
        Integer incentiveValue = Objects.isNull(userDTO.getIncentiveValue()) ? IntegerConstant.ZERO : userDTO.getIncentiveValue();
        // 增加激励值
        Integer currentIncentiveValue = incentiveValue + increaseIncentiveValue;
        optionsMap.put("incentiveValue", JsonData.of(currentIncentiveValue));
        String source = "ctx._source.incentiveValue = params.incentiveValue";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void reduceIncentiveValue(UserDTO userDTO, Integer reduceIncentiveValue) throws IOException {
        Query query = new Query.Builder()
                .ids(t -> t.values(userDTO.getId()))
                .build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        // 扣除激励值
        int currentIncentiveValue = userDTO.getIncentiveValue() - reduceIncentiveValue;
        optionsMap.put("incentiveValue", JsonData.of(Math.max(currentIncentiveValue, IntegerConstant.ZERO)));
        String source = "ctx._source.incentiveValue = params.incentiveValue";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void initLastTimeLoginFailTime(String userId, String currentDayTime) throws Exception {
        Query query = Query.of(q -> q.ids(i -> i.values(userId)));
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        optionsMap.put("lastTimeLoginFailTime", JsonData.of(currentDayTime));
        String source = "ctx._source.lastTimeLoginFailTime = params.lastTimeLoginFailTime";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void initLastTimeAddPlanTime(String userId, String currentDayTime) throws Exception {
        Query query = Query.of(q -> q.ids(i -> i.values(userId)));
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        optionsMap.put("lastTimeAddPlanTime", JsonData.of(currentDayTime));
        String source = "ctx._source.lastTimeAddPlanTime = params.lastTimeAddPlanTime";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void resetLoginFailTimes(String userId, String currentDayTime) throws IOException {
        // 更新最后一次登录失败的那一天的时间为当天时间 且 同时清空当天登录失败次数
        Query query = Query.of(q -> q.ids(i -> i.values(userId)));
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        optionsMap.put("lastTimeLoginFailTime", JsonData.of(currentDayTime));
        optionsMap.put("currentDayLoginFailTimes", JsonData.of(IntegerConstant.ZERO));
        String source = "ctx._source.lastTimeLoginFailTime = params.lastTimeLoginFailTime;" +
                "ctx._source.currentDayLoginFailTimes = params.currentDayLoginFailTimes";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void resetCreatePlanTimes(String userId, String currentDayTime) throws IOException {
        // 更新最后一次创建计划的那一天的时间为当天时间 且 同时清空当天创建计划的次数
        Query query = Query.of(q -> q.ids(i -> i.values(userId)));
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        optionsMap.put("lastTimeAddPlanTime", JsonData.of(currentDayTime));
        optionsMap.put("currentDayCreatePlanTimes", JsonData.of(IntegerConstant.ZERO));
        String source = "ctx._source.lastTimeAddPlanTime = params.lastTimeAddPlanTime;" +
                "ctx._source.currentDayCreatePlanTimes = params.currentDayCreatePlanTimes";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public void increaseCreatePlanTimes(String userId) throws IOException {
        UserDTO userDTO = this.getUserByUserId(userId);
        if (Objects.isNull(userDTO)) {
            log.error("UserServiceImpl.increaseCreatePlanTimes() exit error! userDTO is null!");
            return;
        }

        Query query = new Query.Builder().ids(i -> i.values(userId)).build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        Integer currentDayCreatePlanTimes = userDTO.getCurrentDayCreatePlanTimes();
        currentDayCreatePlanTimes += IntegerConstant.ONE;
        optionsMap.put("currentDayCreatePlanTimes", JsonData.of(currentDayCreatePlanTimes));
        String source = "ctx._source.currentDayCreatePlanTimes = params.currentDayCreatePlanTimes";
        userDao.updateUserField(optionsMap, source, query);
    }

    @Override
    public UserDTO getUserByUserId(String userId) throws IOException {
        UserDocument userDocument = userDao.getUserByUserDocumentId(userId);
        return userManager.userDocumentConvertUserDTO(userDocument);
    }

    @Override
    public UserDTO getUserByUsername(String userName) throws IOException {
        Query username = new Query.Builder()
                .term(t -> t.field("userName.keyword").value(userName))
                .build();
        UserDocument userDocument = userDao.getSingleUserByQuery(username);
        return userManager.userDocumentConvertUserDTO(userDocument);
    }

    @Override
    public WishProviderDTO isIncentiveValueEnough(UserDTO userDTO) {
        WishProviderDTO wishProviderDTO = new WishProviderDTO();
        wishProviderDTO.setCurrentConsumeIncentiveValue(IncentiveValueRuleConstant.EACH_WISH);
        // 激励值为null或者0时
        if (Objects.isNull(userDTO.getIncentiveValue()) || IntegerConstant.ZERO.equals(userDTO.getIncentiveValue())) {
            wishProviderDTO.setIsIncentiveValueEnough(Boolean.FALSE);
            wishProviderDTO.setDifferenceIncentiveValue(IncentiveValueRuleConstant.EACH_WISH);
            return wishProviderDTO;
        }

        // 如果用户的激励值足够，则可以许愿
        if (userDTO.getIncentiveValue() >= IncentiveValueRuleConstant.EACH_WISH) {
            wishProviderDTO.setIsIncentiveValueEnough(Boolean.TRUE);
            wishProviderDTO.setDifferenceIncentiveValue(IntegerConstant.ZERO);
            return wishProviderDTO;
        }

        // 用户的激励值不足，则不可以许愿
        wishProviderDTO.setIsIncentiveValueEnough(Boolean.FALSE);
        wishProviderDTO.setDifferenceIncentiveValue(IncentiveValueRuleConstant.EACH_WISH - userDTO.getIncentiveValue());
        return wishProviderDTO;
    }

    @Override
    public UserDTO getUserByNickname(String nickName) throws IOException {
        Query username = new Query.Builder()
                .term(t -> t.field("nickName.keyword").value(nickName))
                .build();
        UserDocument userDocument = userDao.getSingleUserByQuery(username);
        return userManager.userDocumentConvertUserDTO(userDocument);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean isLocked(UserDTO userDTO) throws Exception {
        // 获取当天的时间(eg:2022-8-4)
        String currentDayTime = CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D);
        // 对老用户进行初始化字段值"lastTimeLoginFailTime"
        if (Objects.isNull(userDTO.getLastTimeLoginFailTime())) {
            this.initLastTimeLoginFailTime(userDTO.getId(), currentDayTime);
            // 此处等待字段"lastTimeLoginFailTime"初始化
            Thread.sleep(1000);
        }

        // 判断当天时间是否与上一次失败登录的时间是同一天
        if (CurrentTimeUtil.isSameDay(userDTO, currentDayTime)) {
            Integer currentDayLoginFailTimes = userDTO.getCurrentDayLoginFailTimes();
            // 检测是否超过最大失败登录次数
            if (currentDayLoginFailTimes >= LoginLimitConstant.CURRENT_DAY_LOGIN_FAIL_MAX_TIMES) {
                log.warn("Exceeded maximum number of failed logins，" + LoginLimitConstant.CURRENT_DAY_LOGIN_FAIL_MAX_TIMES + "times！The userId is: {}", userDTO.getId());
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        this.resetLoginFailTimes(userDTO.getId(), currentDayTime);
        return Boolean.FALSE;
    }

}
