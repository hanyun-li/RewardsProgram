package cloud.lihan.rewardsprogram.event.carrier;

import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.event.carrier.base.BaseEvent;

/**
 * 发送验证码限制事件载体
 *
 * @author hanyun.li
 * @createTime 2022/08/11 14:54:00
 */
public class SendVerificationCodeLimitEvent extends BaseEvent<UserDTO> {

    /**
     * 构造器
     *
     * @param source    发布事件的类实例
     * @param eventData 事件携带的信息
     */
    public SendVerificationCodeLimitEvent(Object source, UserDTO eventData) {
        super(source, eventData);
    }

}
