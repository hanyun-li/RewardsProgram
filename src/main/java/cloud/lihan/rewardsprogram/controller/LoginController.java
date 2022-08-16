package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.constants.LoginLimitConstant;
import cloud.lihan.rewardsprogram.common.constants.SessionConstant;
import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.PlanService;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
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
    @Autowired
    private PlanService planService;

    @PostMapping("/login")
    public ModelAndView login(UserVO userVO, HttpServletRequest request) throws Exception {
        return this.loginProvider(userVO, request);
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().removeAttribute(SessionConstant.LOGIN_TOKEN);
        return this.toLoginPage(new ModelAndView());
    }

    @GetMapping()
    public ModelAndView toLoginPage(ModelAndView view) {
        view.setViewName("signin/index");
        return view;
    }

    @GetMapping("/logoutWarn")
    public ModelAndView toLogoutWarnPage(ModelAndView view) {
        view.setViewName("modals/logout_warn");
        return view;
    }

    /**
     * 登录提供
     *
     * @param userVO 用户输入信息
     * @return index.html 登录页面 或者(plan.html 首页)
     * @throws Exception 异常信息
     */
    private synchronized ModelAndView loginProvider(UserVO userVO, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        // 设置本次登录维持的最长会话超时时间
        session.setMaxInactiveInterval(SessionConstant.MAX_TIMEOUT_TIME);

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

        // 检测用户当天是否被锁定登录
        if (userService.isLocked(user)) {
            view.addObject("maxLoginFailTimes", LoginLimitConstant.CURRENT_DAY_LOGIN_FAIL_MAX_TIMES);
            view.setViewName("cover/login_locked");
            return view;
        }

        // 校验密码是否输入正确
        if (!userVO.getPassword().equals(user.getPassword())) {
            userService.failLogin(user.getId());
            view.addObject("passwordIsError", Boolean.TRUE);
            view.addObject("usernameValue", userVO.getUserName());
            view.setViewName("signin/index");
            return view;
        }

        // 设置登录token
        session.setAttribute(SessionConstant.LOGIN_TOKEN, user.getId());

        // 判断该用户今天是否已经登录过，如果今天是首次登录且是白天（9:00~18:00），则触发招呼特效
        if (!userService.isLoggedInToday(user)) {
            view.setViewName("greeting/greeting");
            return view;
        }

        List<PlanDTO> todayUnfinishedPlans = planService.getTodayUnfinishedPlans(user.getId());
        List<PlanDTO> todayFinishedPlans = planService.getTodayFinishedPlans(user.getId());
        view.addObject("todayUnfinishedPlans", todayUnfinishedPlans);
        view.addObject("todayFinishedPlans", todayFinishedPlans);
        view.setViewName("plan/plan");
        return view;
    }

}
