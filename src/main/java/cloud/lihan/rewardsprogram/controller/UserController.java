package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.SessionConstant;
import cloud.lihan.rewardsprogram.common.context.SendVerificationCodeLimitData;
import cloud.lihan.rewardsprogram.common.controller.BaseController;
import cloud.lihan.rewardsprogram.common.core.Base;
import cloud.lihan.rewardsprogram.common.enums.StatusCodeEnum;
import cloud.lihan.rewardsprogram.common.utils.EmailUtil;
import cloud.lihan.rewardsprogram.common.utils.SafetyUtil;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.event.carrier.SendVerificationCodeLimitEvent;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 用户信息操作控制
 *
 * @author hanyun.li
 * @createTime 2022/08/08 14:24:00
 */
@Slf4j
@Controller("userController")
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/password/edit")
    public ModelAndView editPassword(UserVO userVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        view.addObject("usernameValue", userVO.getUserName());
        view.addObject("passwordValue", userVO.getPassword());
        view.addObject("userEmail", userVO.getUserEmail());
        view.addObject("verificationCodeValue", userVO.getVerificationCode());
        if (StringUtils.isEmpty(userVO.getUserName())) {
            view.addObject("usernameIsEmpty", Boolean.TRUE);
            view.setViewName("password/forgot_password");
            return view;
        }

        if (StringUtils.isEmpty(userVO.getPassword())) {
            view.addObject("passwordIsEmpty", Boolean.TRUE);
            view.setViewName("password/forgot_password");
            return view;
        }

        // 校验用户名是否存在
        UserDTO user = userService.getUserByUsername(userVO.getUserName());
        if (Objects.isNull(user)) {
            view.addObject("usernameNotExist", Boolean.TRUE);
            view.setViewName("password/forgot_password");
            return view;
        }

        // 判断用户是否输入了验证码
        if (Objects.isNull(userVO.getVerificationCode()) || IntegerConstant.ZERO.equals(userVO.getVerificationCode())) {
            view.addObject("verificationCodeIsEmpty", Boolean.TRUE);
            view.setViewName("password/forgot_password");
            return view;
        }

        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(SessionConstant.VERIFICATION_CODE);
        // 判断验证码是否已经失效
        if (Objects.isNull(attribute)) {
            view.addObject("verificationCodeIsInvalid", Boolean.TRUE);
            view.setViewName("password/forgot_password");
            return view;
        }

        int encryptedVerificationCode = (int) attribute;
        // 校验验证码是否正确
        if (userVO.getVerificationCode().equals(SafetyUtil.decryptVerificationCode(encryptedVerificationCode))) {
            userService.editPassword(user.getId(), userVO.getPassword());
            // 设置验证码用过一次后失效
            session.removeAttribute(SessionConstant.VERIFICATION_CODE);

            // 等待密码更新成功
            Thread.sleep(1000);
            view.setViewName("cover/edit_password_success");
            return view;
        }

        view.addObject("verificationCodeIsError", Boolean.TRUE);
        view.setViewName("password/forgot_password");
        return view;
    }

    @PostMapping("/code/send")
    @ResponseBody
    public Base sendVerificationCode(UserVO userVO, HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(userVO.getUserName())) {
            return ApiReturn(StatusCodeEnum.USER_NAME_NOT_EMPTY.getCode(), StatusCodeEnum.USER_NAME_NOT_EMPTY.getDescription());
        }

        if (StringUtils.isEmpty(userVO.getUserEmail())) {
            return ApiReturn(StatusCodeEnum.EMAIL_NOT_EMPTY.getCode(), StatusCodeEnum.EMAIL_NOT_EMPTY.getDescription());
        }

        // 校验邮箱格式是否正确
        if (EmailUtil.checkEmailFormat(userVO.getUserEmail())) {
            UserDTO userDTO = userService.getUserByUserEmail(userVO.getUserEmail());
            // 判断该邮箱是否有效
            if (Objects.isNull(userDTO)) {
                return ApiReturn(StatusCodeEnum.EMAIL_ADDRESS_INVALID.getCode(), StatusCodeEnum.EMAIL_ADDRESS_INVALID.getDescription());
            }

            // 判断输入的邮箱地址是否与用户匹配
            if (!userVO.getUserName().equals(userDTO.getUserName())) {
                return ApiReturn(StatusCodeEnum.EMAIL_ADDRESS_MISMATCH.getCode(), StatusCodeEnum.EMAIL_ADDRESS_MISMATCH.getDescription());
            }

            // 判断该邮箱是否已经发送过验证码了，有没有过间隔时间，是否可以重新发送了
            if (SendVerificationCodeLimitData.canSend(userDTO.getId())) {
                HttpSession session = request.getSession();
                int verificationCode = SafetyUtil.generateVerificationCode();
                // 将验证码加密之后，存入session中
                session.setAttribute(SessionConstant.VERIFICATION_CODE, SafetyUtil.encryptionVerificationCode(verificationCode));
                session.setMaxInactiveInterval(SessionConstant.VERIFICATION_CODE_EFFICIENT_TIME);
                EmailUtil.sendMail(userVO.getUserEmail(), "验证码: <h1>" + verificationCode + "</h1>", "Verification Code");

                // 禁止该用户高频率发送验证码
                applicationEventPublisher.publishEvent(new SendVerificationCodeLimitEvent(this, userDTO));
                return apiOk();
            } else {
                return ApiReturn(StatusCodeEnum.EMAIL_ADDRESS_PROHIBITED.getCode(), StatusCodeEnum.EMAIL_ADDRESS_PROHIBITED.getDescription());
            }
        }
        return ApiReturn(StatusCodeEnum.EMAIL_ADDRESS_FORMAT_NOT_TRUE.getCode(), StatusCodeEnum.EMAIL_ADDRESS_FORMAT_NOT_TRUE.getDescription());
    }

    @GetMapping("/password/forgot")
    public ModelAndView toForgotPasswordPage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("password/forgot_password");
        return view;
    }

    @GetMapping("/getSinglePersonInfo")
    @ResponseBody
    public Base getSinglePersonInfo(@RequestParam("personInfo") String personInfo) throws Exception {
        UserDTO user = userService.getUserByUserInfo(personInfo);
        return apiOk(user);
    }

    @GetMapping("/getAllPersonInfo")
    @ResponseBody
    public Base getAllPersonInfo() throws Exception {
        return apiOk(userService.getAllUser());
    }

}
