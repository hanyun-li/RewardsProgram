package cloud.lihan.rewardsprogram.common.utils;

import java.util.UUID;

/**
 * 生成随机数
 *
 * @author hanyun.li
 * @createTime 2022/07/04 10:23:00
 */
public class UuidUtil {

    /**
     * 生成随机id使用
     */
    private static final UUID RANDOM_NUM = UUID.randomUUID();

    /**
     * 获取uuid
     *
     * @return {@link String}
     */
    public static String newUUID() {
        return RANDOM_NUM.toString();
    }

}
