package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.controller.BaseController;
import cloud.lihan.rewardsprogram.common.utils.LoginUtil;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.dto.WishDTO;
import cloud.lihan.rewardsprogram.dto.provider.WishProviderDTO;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.service.inner.WishService;
import cloud.lihan.rewardsprogram.vo.WishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 愿望相关控制器
 *
 * @author hanyun.li
 * @createTime 2022/06/29 15:16:00
 */
@Slf4j
@Controller("wishController")
@RequestMapping("/wish")
public class WishController extends BaseController {

    @Autowired
    private WishService wishService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ModelAndView makingWish(WishVO wishVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 输入的愿望信息不能为空
            if (StringUtils.isEmpty(wishVO.getWishInfo())) {
                view.addObject("planInfoErrorInfo", "请输入计划信息！");
                return this.wishProvider(view, userId);
            }

            // 输入的愿望信息不能重复
            if (wishService.checkWishInfo(wishVO.getWishInfo(), userId)) {
                view.addObject("planInfoErrorInfo", "计划信息重复，请重新输入！");
                return this.wishProvider(view, userId);
            }

            // 检测用户激励值是否足够本次许愿
            WishProviderDTO wishProvider = userService.isIncentiveValueEnough(user);
            if (wishProvider.getIsIncentiveValueEnough()) {
                wishVO.setUserId(userId);
                wishService.makingWish(user, wishVO);
                // 这里休眠3秒是为了，让吹蜡烛动画可以播放3秒
                Thread.sleep(3000);
                return this.wishProvider(view, userId);
            }

            view.addObject("wishProvider", wishProvider);
            view.setViewName("modals/modals");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping
    public ModelAndView toWish(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }
            return this.wishProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/toBlowOutCandles")
    public ModelAndView toBlowOutCandles(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }
            view.setViewName("wish/blow_out_candles");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    /**
     * 愿望提供
     *
     * @param view 模版
     * @param userId 用户ID
     * @return 模版
     * @throws Exception 异常信息
     */
    private ModelAndView wishProvider(ModelAndView view, String userId) throws Exception {
        Integer notImplementedWishCount = wishService.getNotImplementedWishCount(userId);
        List<WishDTO> multipleRandomWish = wishService.getMultipleRealizedRandomWish(userId, IntegerConstant.FIVE);
        view.addObject("notImplementedWishCount", notImplementedWishCount);
        view.addObject("multipleRandomWish", multipleRandomWish);
        view.setViewName("wish/wish");
        return view;
    }

}
