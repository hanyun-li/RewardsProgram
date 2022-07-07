package cloud.lihan.rewardsprogram.common.core;

/**
 * 返回信息实体
 *
 * @author hanyun.li
 * @createTime 2022/06/29 15:25:00
 */
public class Base {

    /**
     * 状态码 200:正常
     */
    private String code;

    /**
     * 数据
     */
    private Object data;

    /**
     * 状态消息（描述）
     */
    private String msg;

    public Base() {

    }

    public Base(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Base(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
