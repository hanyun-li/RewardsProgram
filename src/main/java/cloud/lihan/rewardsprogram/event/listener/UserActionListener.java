package cloud.lihan.rewardsprogram.event.listener;

import cloud.lihan.rewardsprogram.common.constants.SessionConstant;
import cloud.lihan.rewardsprogram.common.context.SendVerificationCodeLimitData;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.event.carrier.SendVerificationCodeLimitEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户操作相关监听器
 *
 * @author hanyun.li
 * @createTime 2022/08/11 15:07:00
 */
@Slf4j
@Component("userActionListener")
public class UserActionListener {

    @Async
    @EventListener
    public void sendVerificationCodeLimit(SendVerificationCodeLimitEvent event) throws InterruptedException {
        UserDTO userDTO = event.getEventData();
        if (Objects.isNull(userDTO)) {
            log.error("UserActionListener.sendVerificationCodeLimit() exist error! userDTO is empty!");
            return;
        }

        // 锁定发送邮件功能，不允许发送邮件
        SendVerificationCodeLimitData.prohibit(userDTO.getId());
        // 锁定发送邮件的间隔时间，在此期间，用户无法再次发送验证码
        Thread.sleep(SessionConstant.REPEAT_MODIFICATION_INTERVAL_TIME);
        // 解除发送邮件功能限制，允许发送邮件
        SendVerificationCodeLimitData.lift(userDTO.getId());
    }

}
