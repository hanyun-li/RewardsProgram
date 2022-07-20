package cloud.lihan.rewardsprogram.common.utils;

import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 当前时间相关工具类
 *
 * @author hanyun.li
 * @createTime 2022/07/08 14:57:00
 */
public class CurrentTimeUtil {

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
