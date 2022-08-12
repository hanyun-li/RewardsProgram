package cloud.lihan.rewardsprogram.common.enums;

/**
 * 自定义状态码枚举
 *
 * @author hanyun.li
 * @createTime 2022/07/18 11:10:00
 */
public enum StatusCodeEnum {

    /**
     * 邮箱相关
     */
    EMAIL_ADDRESS_INVALID("5001", "该邮箱不存在🙅"),
    EMAIL_ADDRESS_PROHIBITED("5002", "您发送的太频繁了，请稍后再发送😊"),
    EMAIL_ADDRESS_MISMATCH("5003", "邮箱地址不匹配，请重新输入🙅"),
    EMAIL_ADDRESS_FORMAT_NOT_TRUE("5004", "邮箱地址格式不正确，请重新输入🙅"),
    EMAIL_NOT_EMPTY("5005", "请输入邮箱地址😊"),

    /**
     * 用户相关
     */
    USER_NAME_NOT_EMPTY("5101", "请输入用户名😊")
    ;

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述信息
     */
    private String description;

    StatusCodeEnum() {
    }

    StatusCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public final String getCode() {
        return code;
    }

    public final String getDescription() {
        return description;
    }

}
