package cloud.lihan.rewardsprogram.common.utils;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;

import java.util.Random;

/**
 * 涉及各种安全操作
 *
 * @author hanyun.li
 * @createTime 2022/08/08 16:19:00
 */
public class SafetyUtil {

    /**
     * 加密验证码
     *
     * @param unchangedVerificationCode 原验证码
     * @return 加密后的验证码
     * @throws IllegalArgumentException 原验证码参数为空异常
     */
    public static int encryptionVerificationCode(int unchangedVerificationCode) throws IllegalArgumentException {
        // 原验证码乘以0.31
        return unchangedVerificationCode * (IntegerConstant.THERE * IntegerConstant.TEN + IntegerConstant.ONE * IntegerConstant.TEN);
    }

    /**
     * 解密验证码
     *
     * @param encryptedVerificationCode 已经加密过的验证码
     * @return 原验证码
     * @throws IllegalArgumentException 已经加密过的验证码参数为空异常
     */
    public static int decryptVerificationCode(int encryptedVerificationCode) throws IllegalArgumentException {
        // 加密验证码除以0.31
        return encryptedVerificationCode / (IntegerConstant.THERE * IntegerConstant.TEN + IntegerConstant.ONE * IntegerConstant.TEN);
    }

    /**
     * 生成4位数的验证码
     *
     * @return 验证码
     */
    public static int generateVerificationCode() throws IllegalArgumentException {
        return generateVerificationCode(IntegerConstant.FOUR);
    }

    /**
     * 生成指定位数的验证码
     *
     * @param nums 验证码位数（不超过6位）
     * @return 验证码
     */
    public static int generateVerificationCode(int nums) throws IllegalArgumentException {
        if (IntegerConstant.SIX < nums || nums < IntegerConstant.ONE) {
            throw new IllegalArgumentException("SafetyUtil.generateVerificationCode() exit error! nums have to in (1~6)!");
        }

        int verificationCode = IntegerConstant.ZERO;
        Random random = new Random();
        for (int n = 0; n < nums; n++) {
            verificationCode += (random.nextInt(IntegerConstant.NINE) + IntegerConstant.ONE) * Math.pow(IntegerConstant.TEN, n);
        }
        return verificationCode;
    }

}
