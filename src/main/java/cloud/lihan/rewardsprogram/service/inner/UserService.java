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
     * @throws IOException 异常信息
     */
    void savaUser(UserVO userVO) throws IOException;

    /**
     * 失败登录时，增加失败登录次数
     *
     * @param userId 用户ID
     * @throws IOException 异常信息
     */
    void failLogin(String userId) throws IOException;

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

}
