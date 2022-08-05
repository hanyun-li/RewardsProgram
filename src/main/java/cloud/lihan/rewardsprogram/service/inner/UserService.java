package cloud.lihan.rewardsprogram.service.inner;

import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.dto.provider.WishProviderDTO;
import cloud.lihan.rewardsprogram.vo.UserVO;

import java.io.IOException;

/**
 * 用户相关的业务方法
 *
 * @author hanyun.li
 * @createTime 2022/07/08 16:22:00
 */
public interface UserService {

    /**
     * 保存用户
     *
     * @param userVO 用户信息
     * @throws Exception 异常信息
     */
    void savaUser(UserVO userVO) throws Exception;

    /**
     * 失败登录时，增加失败登录次数
     *
     * @param userId 用户ID
     * @throws Exception 异常信息
     */
    void failLogin(String userId) throws Exception;

    /**
     * 编辑密码
     *
     * @param userId 用户ID
     * @param userPassword 用户密码
     * @throws IOException 异常信息
     */
    void editPassword(String userId, String userPassword) throws IOException;

    /**
     * 编辑用户信息
     *
     * @param userVO 用户信息
     * @throws IOException 异常信息
     */
    void editUserInfo(UserVO userVO) throws IOException;

    /**
     * 增加激励值（完成计划时增加）
     *
     * @param userDTO 用户传输对象
     * @param increaseIncentiveValue 需要增加的激励值
     * @throws IOException 异常信息
     */
    void increaseIncentiveValue(UserDTO userDTO, Integer increaseIncentiveValue) throws IOException;

    /**
     * 减少激励值（实现愿望时消耗）
     *
     * @param userDTO 用户传输对象
     * @param reduceIncentiveValue 需要消耗的激励值
     * @throws IOException 异常信息
     */
    void reduceIncentiveValue(UserDTO userDTO, Integer reduceIncentiveValue) throws IOException;

    /**
     * 初始化最后一次登录失败的那一天的时间
     *
     * @param userId 用户ID
     * @param currentDayTime 当前时间
     * @throws Exception 异常信息
     */
    void initLastTimeLoginFailTime(String userId, String currentDayTime) throws Exception;

    /**
     * 初始化最后一次添加计划的那一天的时间
     *
     * @param userId 用户ID
     * @param currentDayTime 当前时间
     * @throws Exception 异常信息
     */
    void initLastTimeAddPlanTime(String userId, String currentDayTime) throws Exception;

    /**
     * 重置登录失败次数
     *
     * @param userId 用户ID
     * @param currentDayTime 当天时间
     * @throws IOException 异常信息
     */
    void resetLoginFailTimes(String userId, String currentDayTime) throws IOException;

    /**
     * 重置创建计划次数
     *
     * @param userId 用户ID
     * @param currentDayTime 当天时间
     * @throws IOException 异常信息
     */
    void resetCreatePlanTimes(String userId, String currentDayTime) throws IOException;

    /**
     * 增加创建计划次数
     *
     * @param userId 用户ID
     * @throws IOException 异常信息
     */
    void increaseCreatePlanTimes(String userId) throws IOException;

    /**
     * 根据ID获取用户信息
     *
     * @param userId 愿望ID
     * @return {@link UserDTO} 用户信息
     * @throws IOException 异常信息
     */
    UserDTO getUserByUserId(String userId) throws IOException;

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return {@link UserDTO} 用户信息
     * @throws IOException 异常信息
     */
    UserDTO getUserByUsername(String username) throws IOException;

    /**
     * 激励值是否足够许愿
     *
     * @param userDTO 用户传输对象
     * @return {@link WishProviderDTO} 许愿时，返回的相关信息实体
     */
    WishProviderDTO isIncentiveValueEnough(UserDTO userDTO);

    /**
     * 根据昵称获取用户信息
     *
     * @param nickName 昵称
     * @return {@link UserDTO} 用户信息
     * @throws IOException 异常信息
     */
    UserDTO getUserByNickname(String nickName) throws IOException;

    /**
     * 检测当天是否超过最大失败登录次数
     *
     * @param userDTO 用户信息
     * @return true:超过 false:未超过
     * @throws Exception 异常信息
     */
    Boolean isLocked(UserDTO userDTO) throws Exception;

}
