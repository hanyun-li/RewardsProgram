package cloud.lihan.rewardsprogram.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置
 *
 * @author hanyun.li
 * @createTime 2022/09/08 10:08:00
 */
@Configuration
public class WebSocketConfig {

    /**
     * 用于扫描和注册所有携带ServerEndPoint注解的实例。
     * 注意:若部署到外部容器 则无需提供此类。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
