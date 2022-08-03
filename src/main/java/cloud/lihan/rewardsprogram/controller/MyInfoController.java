package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.utils.LoginUtil;
import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 个人信息展示控制
 *
 * @author hanyun.li
 * @createTime 2022/08/03 18:04:00
 */
@Slf4j
@Controller("myInfoController")
@RequestMapping("/myInfo")
public class MyInfoController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView toMyInfoPage(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                log.error("MyInfoController.toMyInfoPage() exist error! error info : [userId not exist!]");
                view.setViewName("cover/not_logger_in");
                return view;
            }
            return this.myInfoProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    /**
     * 我的信息提供
     *
     * @param view 模版
     * @param userId 用户ID
     * @return 模版
     * @throws Exception 异常信息
     */
    private ModelAndView myInfoProvider(ModelAndView view, String userId) throws Exception {
        UserDTO user = userService.getUserByUserId(userId);
        view.addObject("user", user);
        view.setViewName("person/my_info");
        return view;
    }

}
