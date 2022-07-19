package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 登录相关控制
 *
 * @author hanyun.li
 * @createTime 2022/07/07 15:42:00
 */
@Controller("loginController")
@RequestMapping()
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ModelAndView toLoginPage(ModelAndView view) {
        view.setViewName("signin/index");
        return view;
    }

    @PostMapping("/login")
    public ModelAndView login(UserVO userVO) throws IOException {
        return loginProvider(userVO);
    }

    @GetMapping("/sidebars")
    public ModelAndView sidebars(ModelAndView view) {
        view.setViewName("sidebars/sidebars");
        return view;
    }

    /**
     * 登录提供
     *
     * @param userVO 用户输入信息
     * @return index.html 登录页面 或者(home.html 首页)
     * @throws IOException 异常信息
     */
    private ModelAndView loginProvider(UserVO userVO) throws IOException {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(userVO.getUserName())) {
            view.addObject("usernameIsEmpty", Boolean.TRUE);
            view.setViewName("signin/index");
            return view;
        }

        if (StringUtils.isEmpty(userVO.getPassword())) {
            view.addObject("passwordIsEmpty", Boolean.TRUE);
            view.addObject("usernameValue", userVO.getUserName());
            view.setViewName("signin/index");
            return view;
        }

        // 校验用户名是否输入正确
        UserDTO user = userService.getUserByUsername(userVO.getUserName());
        if (Objects.isNull(user)) {
            view.addObject("usernameIsError", Boolean.TRUE);
            view.addObject("usernameValue", userVO.getUserName());
            view.setViewName("signin/index");
            return view;
        }

        // 校验密码是否输入正确
        if (!userVO.getPassword().equals(user.getPassword())) {
            view.addObject("passwordIsError", Boolean.TRUE);
            view.addObject("usernameValue", userVO.getUserName());
            view.setViewName("signin/index");
            return view;
        }

        view.setViewName("product/product");
        return view;
    }

}
