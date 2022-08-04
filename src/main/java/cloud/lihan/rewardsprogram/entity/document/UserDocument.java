package cloud.lihan.rewardsprogram.entity.document;

import cloud.lihan.rewardsprogram.common.constants.LoginLimitConstant;
import cloud.lihan.rewardsprogram.common.documents.BaseDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息文档
 *
 * @author hanyun.li
 * @createTime 2022/07/08 14:30:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDocument extends BaseDocument {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 当天登录失败的次数(功能是为了防止恶意破解密码，
     * 系统默认设定当天5次登录失败，则当天锁定用户登录)
     *
     * {@link LoginLimitConstant} 登录限制相关常量
     */
    private Integer currentDayLoginFailTimes;

    /**
     * 激励值(用途：许愿消耗)
     */
    private Integer incentiveValue;

}
