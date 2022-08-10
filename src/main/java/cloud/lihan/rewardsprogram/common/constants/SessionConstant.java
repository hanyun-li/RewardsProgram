package cloud.lihan.rewardsprogram.common.constants;

/**
 * session相关常量
 *
 * @author hanyun.li
 * @createTime 2022/07/19 17:46:00
 */
public interface SessionConstant {

    /**
     * 最大超时时间(2小时)
     */
    int MAX_TIMEOUT_TIME = 2 * 60 * 60;

    /**
     * 登录token(等效于：userId)
     */
    String LOGIN_TOKEN = "login_token";

    /**
     * 验证码有效时间（10分钟）
     */
    int VERIFICATION_CODE_EFFICIENT_TIME = 10 * 60;

    /**
     * 验证码key
     */
    String VERIFICATION_CODE = "verification_code";

}
