package cloud.lihan.rewardsprogram.common.utils;

import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 当前时间相关工具类
 *
 * @author hanyun.li
 * @createTime 2022/07/08 14:57:00
 */
@Slf4j
public class CurrentTimeUtil {

    /**
     * 比较两时间是否是同一天
     *
     * @param compareTime 需要与当天比较的时间（注：时间格式需与当天时间格式相同）
     * @param currentDayTime 当天时间（注意，时间格式必须与用户信息中"lastTimeLoginFailTime"字段的时间格式相同）
     * @return true：是同一天 false：不是同一天
     */
    public static Boolean isSameDay(String compareTime, String currentDayTime) throws IllegalArgumentException {
        if (Objects.isNull(compareTime)) {
            throw new IllegalArgumentException("CurrentTimeUtil.isSameDay() exit error! The compareTime is null!");
        }

        if (currentDayTime.equals(compareTime)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 判断当天是与最后一次登录失败的那一天是同一天
     *
     * @param userDTO 用户信息
     * @return true：是同一天 false：不是同一天
     * @throws Exception
     */
    public static Boolean isSameDay(UserDTO userDTO) throws Exception {
        // 获取当天的时间(eg:2022-8-4)
        String currentDayTime = CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D);
        return isSameDay(userDTO, currentDayTime);
    }

    /**
     * 判断当天与最后一次登录失败的那一天是否是同一天
     *
     * @param userDTO 用户信息
     * @param currentDayTime 当天时间（注意，时间格式必须与用户信息中"lastTimeLoginFailTime"字段的时间格式相同）
     * @return true：是同一天 false：不是同一天
     */
    public static Boolean isSameDay(UserDTO userDTO, String currentDayTime) throws IllegalArgumentException {
        if (Objects.isNull(userDTO.getLastTimeLoginFailTime())) {
            throw new IllegalArgumentException("CurrentTimeUtil.isSameDay() exit error! The lastTimeLoginFailTime filed not initialized!");
        }

        // 判断当天时间是否与上一次失败登录的时间是同一天
        if (currentDayTime.equals(userDTO.getLastTimeLoginFailTime())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    public static String newCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(TimeFormatConstant.STANDARD);
        return format.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    public static Time newCurrentTimes() {
        SimpleDateFormat format = new SimpleDateFormat(TimeFormatConstant.STANDARD);
        Date date = new Date();
        String time = format.format(date);
        return Time.newInstance(time, date.getTime());
    }

    /**
     * 获取当前时间
     *
     * @param timeFormat 时间展示格式
     * @return 当前时间
     */
    public static String newCurrentTime(String timeFormat) throws Exception {
        if (StringUtils.isEmpty(timeFormat)) {
            throw new Exception("TimeFormat not be empty!");
        }

        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date());
    }

    /**
     * 封装不同类型的时间数据
     */
    public static class Time {

        /**
         * 时间
         */
        private String time;

        /**
         * 时间戳
         */
        private Long timestamp;

        private Time() {
        }

        public static Time newInstance(String time, Long timestamp) {
            return new Time(time, timestamp);
        }

        private Time(String time, Long timestamp) {
            this.time = time;
            this.timestamp = timestamp;
        }

        public String getTime() {
            return time;
        }

        public Long getTimestamp() {
            return timestamp;
        }
    }

}
