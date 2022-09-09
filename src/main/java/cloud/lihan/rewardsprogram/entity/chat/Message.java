package cloud.lihan.rewardsprogram.entity.chat;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 在线聊天消息实体类
 *
 * @author hanyun.li
 * @createTime 2022/09/08 10:08:00
 */
@Data
public class Message {

    public static final String ENTER = "ENTER";
    public static final String SPEAK = "SPEAK";
    public static final String QUIT = "QUIT";

    /**
     * 区分session
     */
    private String sessionId;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 发送人
     */
    private String username;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 在线用户数
     */
    private int onlineCount;

    public static String jsonStr(String sessionId, String type, String username, String msg, int onlineTotal) {
        return JSON.toJSONString(new Message(sessionId, type, username, msg, onlineTotal));
    }

    public Message(String sessionId, String type, String username, String msg, int onlineCount) {
        this.sessionId = sessionId;
        this.type = type;
        this.username = username;
        this.msg = msg;
        this.onlineCount = onlineCount;
    }

}
