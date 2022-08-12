package cloud.lihan.rewardsprogram.common.enums;

/**
 * 自定义状态码枚举
 *
 * @author hanyun.li
 * @createTime 2022/07/18 11:10:00
 */
public enum StatusCodeEnum {

    /**
     * 邮箱相关状态码
     */
    EMAIL_ADDRESS_INVALID("5001", "该邮箱不存在"),
    EMAIL_ADDRESS_PROHIBITED("5002", "您发送的太频繁了，请稍后再发送😊"),
    EMAIL_ADDRESS_MISMATCH("5003", "邮箱地址不匹配，请重新输入🙅"),
    USER_NAME_NOT_EMPTY("5004", "用户名不能为空，请输入用户名😊")
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
