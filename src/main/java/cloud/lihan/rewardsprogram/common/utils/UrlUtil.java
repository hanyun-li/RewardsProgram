package cloud.lihan.rewardsprogram.common.utils;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import org.thymeleaf.util.StringUtils;

/**
 * 资源地址url相关工具
 *
 * @author hanyun.li
 * @createTime 2022/12/26 15:48:00
 */
public class UrlUtil {

    /**
     * 拼接上传头像的url（拼接规则为：用户ID_当天时间.文件后缀）
     *
     * @param userId 用户ID
     * @param avatarFileName 头像ID
     * @return 生成的文件名称（eg: 2022-12-26_6ccc6613-4ead-4515-9449-e337822ec645_.png）
     * @throws IllegalArgumentException 参数异常
     */
    public static String spliceAvatarUrl(String userId, String avatarFileName) throws IllegalArgumentException{
        if (StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException("userId is empty!");
        }

        if (StringUtils.isEmpty(avatarFileName)) {
            throw new IllegalArgumentException("fileSuffixName is empty!");
        }

        // 获取头像文件后缀名称
        String[] split = avatarFileName.split("\\.");
        String fileSuffixName = split[split.length - IntegerConstant.ONE];

        return CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D) +
                "_" +
                System.currentTimeMillis() +
                "_" +
                userId +
                "." +
                fileSuffixName;
    }

    /**
     * 获取默认头像地址
     *
     * @return 默认头像地址（用户未上传头像时使用）
     */
    public static String getDefaultAvatarUrl() {
        return "data:image/jpeg;base64,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";
    }

}
