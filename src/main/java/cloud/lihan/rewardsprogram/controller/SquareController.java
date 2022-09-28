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

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 广场控制器
 *
 * @author hanyun.li
 * @createTime 2022/09/28 11:53:00
 */
@Slf4j
@Controller("squareController")
@RequestMapping("/square")
public class SquareController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ModelAndView toSquare(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            view.setViewName("square/square");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

}
