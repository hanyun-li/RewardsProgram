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

}
