package cloud.lihan.rewardsprogram.vo;

import lombok.Data;

/**
 * 用户实体数据传输（接收用）
 *
 * @author hanyun.li
 * @createTime 2022/07/08 14:35:00
 */
@Data
public class UserVO {

    /**
     * 用户Id（更新时使用）
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户密码
     */
    private String password;

}
