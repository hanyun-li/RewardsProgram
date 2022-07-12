package cloud.lihan.rewardsprogram.service.impl;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.LoginLimitConstant;
import cloud.lihan.rewardsprogram.dao.inner.UserDao;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.entety.document.UserDocument;
import cloud.lihan.rewardsprogram.manager.UserManager;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.UserVO;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void savaUser(UserVO userVO) throws IOException {
        userDao.createUserDocument(userManager.userVOConvertUserDocument(userVO));
    }

    @Override
    public void failLogin(String userId) throws IOException {
        UserDTO userDTO = this.getUserByUserId(userId);
        if (Objects.isNull(userDTO)) {
            return;
        }

        Integer currentDayLoginFailTimes = userDTO.getCurrentDayLoginFailTimes();
        if (currentDayLoginFailTimes > LoginLimitConstant.CURRENT_DAY_LOGIN_FAIL_MAX_TIMES) {
            log.info("超过最大失败登录次数，" + LoginLimitConstant.CURRENT_DAY_LOGIN_FAIL_MAX_TIMES + "次！");
            return;
        }

        Query query = new Query.Builder()
                .term(t -> t.field("id").value(userId))
                .build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        optionsMap.put("currentDayLoginFailTimes", JsonData.of(++currentDayLoginFailTimes));
        String source = "ctx._source.currentDayLoginFailTimes = params.currentDayLoginFailTimes";
        userDao.updateUserSingleField(optionsMap, source, query);
    }

    @Override
    public void editPassword(String userId, String userPassword) throws IOException {
        Query query = new Query.Builder()
                .term(t -> t.field("id").value(userId))
                .build();
        Map<String, JsonData> optionsMap = new HashMap<>(IntegerConstant.ONE);
        optionsMap.put("password", JsonData.of(userPassword));
        String source = "ctx._source.password = params.password";
        userDao.updateUserSingleField(optionsMap, source, query);
    }

    @Override
    public UserDTO getUserByUserId(String userId) throws IOException {
        UserDocument userDocument = userDao.getUserByUserDocumentId(userId);
        return userManager.userDocumentConvertUserDTO(userDocument);
    }

}
