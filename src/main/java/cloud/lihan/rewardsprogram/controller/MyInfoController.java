package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.utils.LoginUtil;
import cloud.lihan.rewardsprogram.common.utils.UrlUtil;
import cloud.lihan.rewardsprogram.config.file.AvatarPropertiesConfig;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    @Autowired
    private AvatarPropertiesConfig avatarProperties;

    @PostMapping("/editAvatar")
    public ModelAndView editAvatar(@RequestParam("avatarImage") MultipartFile avatarImage, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 未获取到头像
            if (Objects.isNull(avatarImage) || avatarImage.isEmpty()) {
                log.warn("MyInfoController.editAvatar() exist warn. warn info : avatar is null !");
            } else {
                // 生成图片
                String originalFilename = avatarImage.getOriginalFilename();
                if (StringUtils.isEmpty(originalFilename)) {
                    log.error("MyInfoController.editAvatar() exist warn. error info : originalFilename is empty !");
                } else {
                    String avatarUrl = UrlUtil.spliceAvatarUrl(userId, originalFilename);
                    File file = new File(avatarProperties.getAvatarPath() + avatarUrl);
                    avatarImage.transferTo(file);

                    // 如果头像上传成功，则更新头像地址
                    if (file.exists()) {
                        userService.editAvatarUrl(userId, avatarUrl);
                    }
                }
            }

            Thread.sleep(1000);
            return this.myInfoProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @PostMapping
    public ModelAndView saveMyInfo(UserVO userVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 校验昵称是否为空
            if (Objects.isNull(userVO.getNickName())) {
                view.addObject("isTheNicknameEmpty", Boolean.TRUE);
                return this.myInfoProvider(view, userId);
            }

            // 校验用户名是否为空
            if (Objects.isNull(userVO.getUserName())) {
                view.addObject("isTheUsernameEmpty", Boolean.TRUE);
                return this.myInfoProvider(view, userId);
            }

            // 判断昵称是否已经存在
            if (!userVO.getNickName().equals(user.getNickName())) {
                UserDTO userByNickname = userService.getUserByNickname(userVO.getNickName());
                if (Objects.nonNull(userByNickname)) {
                    view.addObject("repeatedNickname", Boolean.TRUE);
                    return this.myInfoProvider(view, userId);
                }
            }

            // 判断用户名是否已经存在
            if (!userVO.getUserName().equals(user.getUserName())) {
                UserDTO userByUsername = userService.getUserByUsername(userVO.getUserName());
                if (Objects.nonNull(userByUsername)) {
                    view.addObject("repeatedUsername", Boolean.TRUE);
                    return this.myInfoProvider(view, userId);
                }
            }

            userVO.setUserId(userId);
            userService.editUserInfo(userVO);
            Thread.sleep(1000);
            return this.myInfoProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping
    public ModelAndView toMyInfoPage(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
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
    private synchronized ModelAndView myInfoProvider(ModelAndView view, String userId) throws Exception {
        UserDTO user = userService.getUserByUserId(userId);
        view.addObject("user", user);
        view.setViewName("person/my_info");
        return view;
    }

}
