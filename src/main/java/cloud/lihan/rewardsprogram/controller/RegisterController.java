package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 注册相关控制
 *
 * @author hanyun.li
 * @createTime 2022/07/12 18:19:00
 */
@Controller("registerController")
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ModelAndView toRegisterPage(ModelAndView view){
        view.setViewName("register/register");
        return view;
    }

    @PostMapping()
    public ModelAndView register(UserVO userVO) throws IOException {
        return registerProvider(userVO);
    }

    /**
     * 登录提供
     *
     * @param userVO 用户输入信息
     * @return index.html 登录页面 或者(home.html 首页)
     * @throws IOException 异常信息
     */
    private ModelAndView registerProvider(UserVO userVO) throws IOException {
        ModelAndView view = new ModelAndView();
//        if (StringUtils.isEmpty(userVO.getUserName())) {
//            view.addObject("usernameIsEmpty", Boolean.TRUE);
//            view.addObject("emailValue", userVO.getUserEmail());
//            view.setViewName("register/register");
//            return view;
//        }
//
//        if (StringUtils.isEmpty(userVO.getUserEmail())) {
//            view.addObject("emailIsEmpty", Boolean.TRUE);
//            view.addObject("usernameValue", userVO.getUserName());
//            view.setViewName("register/register");
//            return view;
//        }
//
//        if (StringUtils.isEmpty(userVO.getPassword())) {
//            view.addObject("passwordIsEmpty", Boolean.TRUE);
//            view.addObject("usernameValue", userVO.getUserName());
//            view.addObject("emailValue", userVO.getUserEmail());
//            view.setViewName("register/register");
//            return view;
//        }
//
//        // 校验用户名是否存在
//        UserDTO user = userService.getUserByUsername(userVO.getUserName());
//        if (Objects.nonNull(user)) {
//            view.addObject("usernameIsExist", Boolean.TRUE);
//            view.addObject("usernameValue", userVO.getUserName());
//            view.addObject("emailValue", userVO.getUserEmail());
//            view.setViewName("register/register");
//            return view;
//        }

//        userService.savaUser(userVO);
        view.setViewName("cover/cover_new");
        return view;
    }

}
