package cloud.lihan.rewardsprogram.service.inner;

import cloud.lihan.rewardsprogram.dto.UserDTO;
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

}
