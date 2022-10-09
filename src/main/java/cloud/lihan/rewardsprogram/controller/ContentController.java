package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.utils.LoginUtil;
import cloud.lihan.rewardsprogram.dto.ContentDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.ContentService;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.ContentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 广场控制器
 *
 * @author hanyun.li
 * @createTime 2022/09/28 11:53:00
 */
@Slf4j
@Controller("contentController")
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private UserService userService;
    @Autowired
    private ContentService contentService;

    @PostMapping("/release")
    public ModelAndView releaseContent(ContentVO contentVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 帖子内容不能为空
            if (StringUtils.isEmpty(contentVO.getContent())) {
                view.addObject("contentIsEmpty", Boolean.TRUE);
                view.setViewName("square/release_content");
                return view;
            }

            contentVO.setUserId(userId);
            contentService.releaseContent(contentVO);
            Thread.sleep(1000);
            return this.contentProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/delete")
    public ModelAndView deleteContent(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 帖子ID不能为空
            if (StringUtils.isEmpty(id)) {
                view.addObject("contentIdIsEmpty", Boolean.TRUE);
                return this.contentProvider(view, userId);
            }

            contentService.deleteContentById(id);
            Thread.sleep(1000);
            return this.contentProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @PostMapping("/edit")
    public ModelAndView editContent(ContentVO contentVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 帖子内容和图片不能同时为空
            if (StringUtils.isEmpty(contentVO.getContent()) && CollectionUtils.isEmpty(contentVO.getImgUrls())) {
                view.addObject("contentIsEmpty", Boolean.TRUE);
                return this.contentProvider(view, userId);
            }

            contentService.editContent(contentVO);
            Thread.sleep(1000);
            return this.contentProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/like")
    public ModelAndView likeContent(ContentVO contentVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            contentVO.setUserId(userId);
            contentService.likeContent(contentVO);
            Thread.sleep(1000);
            return this.contentProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/cancelLike")
    public ModelAndView cancelLikeContent(ContentVO contentVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            contentVO.setUserId(userId);
            contentService.cancelLikeContent(contentVO);
            Thread.sleep(1000);
            return this.contentProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/toContent")
    public ModelAndView toContent(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            return this.contentProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/toReleaseContent")
    public ModelAndView toReleaseContent(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            view.setViewName("square/release_content");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    /**
     * 帖子页跳转功能提供方法
     *
     * @param view 模版
     * @param userId 用户ID
     * @return 模版
     * @throws Exception 异常信息
     */
    private ModelAndView contentProvider(ModelAndView view, String userId) throws Exception {
        List<ContentDTO> contents = contentService.getAllContents(userId);
        if (!CollectionUtils.isEmpty(contents)) {
            view.addObject("contents", contents);
        }
        view.setViewName("square/square");
        return view;
    }

}
