package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.entity.chat.Message;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务
 *
 * @see ServerEndpoint WebSocket服务端 需指定端点的访问路径
 * @see Session   WebSocket会话对象 通过它给客户端发送消息
 *
 * @author hanyun.li
 * @createTime 2022/09/08 10:08:00
 */
@Component
@ServerEndpoint("/chat")
public class WebSocketController {

    /**
     * 全部在线会话  PS: 基于场景考虑 这里使用线程安全的Map存储会话对象。
     */
    private static final Map<String, Session> ONLINE_SESSIONS = new ConcurrentHashMap<>();

    /**
     * 当客户端打开连接：1.添加会话对象 2.更新在线人数
     */
    @OnOpen
    public void onOpen(Session session) {
        ONLINE_SESSIONS.put(session.getId(), session);
        sendMessageToAll(session.getId(), Message.jsonStr(session.getId(), Message.PONG, "", "", "", ONLINE_SESSIONS.size()));
    }

    /**
     * 当客户端发送消息：1.获取它的用户名和消息 2.发送消息给所有人
     * <p>
     * PS: 这里约定传递的消息为JSON字符串 方便传递更多参数！
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = JSON.parseObject(jsonStr, Message.class);
        // 发送心跳消息
        if (Message.PING.equals(message.getMsg())) {
            sendMessageToAll(session.getId(), Message.jsonStr(session.getId(), Message.PONG, "", "", message.getMsg(), ONLINE_SESSIONS.size()));
            return;
        } else if (Message.EMOJI.equals(message.getType())) {
            sendMessageToAll(session.getId(), Message.jsonStr(session.getId(), Message.EMOJI, message.getUsername(),message.getAvatarUrl(), message.getMsg(), ONLINE_SESSIONS.size()));
            return;
        }
        sendMessageToAll(session.getId(), Message.jsonStr(session.getId(), Message.SPEAK, message.getUsername(),message.getAvatarUrl(), message.getMsg(), ONLINE_SESSIONS.size()));
    }

    /**
     * 当关闭连接：1.移除会话对象 2.更新在线人数
     */
    @OnClose
    public void onClose(Session session) {
        ONLINE_SESSIONS.remove(session.getId());
        sendMessageToAll(session.getId(), Message.jsonStr(session.getId(), Message.QUIT, "","", "", ONLINE_SESSIONS.size()));
    }

    /**
     * 当通信发生异常：打印错误日志
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 公共方法：发送信息给所有人
     */
    private static void sendMessageToAll(String sessionId, String msg) {
        Message message = JSON.parseObject(msg, Message.class);
        ONLINE_SESSIONS.forEach((id, session) -> {
            try {
                // 检测是否心跳消息
                if (isHeartCheck(message.getType())) {
                    session.getBasicRemote().sendText(msg);
                } else {
                    // 这里判断不给自己发消息
                    if (!id.equals(sessionId)) {
                        session.getBasicRemote().sendText(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 检测消息类型是否是心跳消息
     *
     * @param messageType 消息类型
     * @return true:是心跳消息 false:不是心跳消息(聊天消息)
     */
    private static Boolean isHeartCheck(String messageType) {
        if (Message.PING.equals(messageType)) {
            return Boolean.TRUE;
        }

        if (Message.PONG.equals(messageType)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

}
