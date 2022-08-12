package cloud.lihan.rewardsprogram.event.carrier.base;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 事件基础类
 *
 * @author hanyun.li
 * @createTime 2022/08/11 14:43:00
 */
public abstract class BaseEvent<T> extends ApplicationEvent {

    /**
     * 事件携带的信息
     */
    private final T eventData;

    /**
     * 构造器
     *
     * @param source 发布事件的类实例
     * @param eventData 事件携带的信息
     */
    public BaseEvent(Object source, T eventData) {
        super(source);
        this.eventData = eventData;
    }

    public T getEventData() {
        return eventData;
    }

}
