package cloud.lihan.rewardsprogram.common.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限制重复提交编辑密码请求的全局变量
 *
 * @author hanyun.li
 * @createTime 2022/08/11 15:19:00
 */
@Slf4j
public class SendVerificationCodeLimitData {

    /**
     * 用户是否可以发送验证码 其中K:用户ID V:（true:可以发送 false:不可发送）
     */
    public static ConcurrentHashMap<String, Boolean> sendVerificationCodeLimitStatus = new ConcurrentHashMap<>();

    /**
     * 检查该用户此时是否可以发送验证码
     *
     * @param userId 用户ID
     * @return true:可以发送 false:不可以发送
     * @throws IllegalArgumentException 用户ID为空异常
     */
    public static Boolean canSend(String userId) throws IllegalArgumentException{
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("SendVerificationCodeLimitData.canSend() exit error! userId is empty!");
        }

        Boolean canSendStatus = sendVerificationCodeLimitStatus.get(userId);
        // 首次发送验证码时，添加发送状态
        if (Objects.isNull(canSendStatus)) {
            sendVerificationCodeLimitStatus.put(userId, Boolean.TRUE);
            return Boolean.TRUE;
        }
        return canSendStatus;
    }

    /**
     * 允许用户发送验证码
     *
     * @param userId 用户ID
     */
    public static void lift(String userId) {
        sendVerificationCodeLimitStatus.put(userId, Boolean.TRUE);
    }

    /**
     * 禁止用户发送验证码
     *
     * @param userId 用户ID
     */
    public static void prohibit(String userId) {
        sendVerificationCodeLimitStatus.put(userId, Boolean.FALSE);
    }

}
