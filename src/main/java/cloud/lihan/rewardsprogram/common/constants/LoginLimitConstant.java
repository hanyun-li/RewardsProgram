package cloud.lihan.rewardsprogram.common.constants;

/**
 * 登录相关限制值
 *
 * @author hanyun.li
 * @createTime 2022/07/08 15:13:00
 */
public interface LoginLimitConstant {

    /**
     * 当天允许失败登录的最大次数
     */
    Integer CURRENT_DAY_LOGIN_FAIL_MAX_TIMES = 5;
}
