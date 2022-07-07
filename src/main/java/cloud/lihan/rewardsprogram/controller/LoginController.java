package cloud.lihan.rewardsprogram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录相关控制
 *
 * @author hanyun.li
 * @createTime 2022/07/07 15:42:00
 */
@Controller("loginController")
@RequestMapping()
public class LoginController {

    @GetMapping()
    public ModelAndView index(ModelAndView view){
        view.setViewName("signin/index");
        return view;
    }


}
