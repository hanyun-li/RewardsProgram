package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.utils.LoginUtil;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * 在线聊天控制器
 *
 * @author hanyun.li
 * @createTime 2022/09/08 10:08:00
 */
@Slf4j
@Controller("chatController")
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private UserService userService;

    /**
     * 聊天界面
     */
    @GetMapping("/index")
    public ModelAndView index(String username, String password, HttpServletRequest request) throws UnknownHostException {
        if (StringUtils.isEmpty(username)) {
            username = "匿名用户";
        }
        ModelAndView mav = new ModelAndView("/chat");
        mav.addObject("username", username);
        mav.addObject("webSocketUrl", "ws://"+ InetAddress.getLocalHost().getHostAddress()+":"+request.getServerPort()+request.getContextPath()+"/chat");
        return mav;
    }

    /**
     * 去到聊天房间
     *
     * @return 模版
     * @throws Exception 异常信息
     */
    @GetMapping
    public ModelAndView toRoom(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            view.setViewName("chat/chat_room");
            view.addObject("username", user.getNickName());
            view.addObject("webSocketUrl", "ws://"+ InetAddress.getLocalHost().getHostAddress()+":"+request.getServerPort()+request.getContextPath()+"/chat");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

}
