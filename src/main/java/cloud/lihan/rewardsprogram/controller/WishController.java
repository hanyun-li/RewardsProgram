package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.utils.EmailUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
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
public class WishController {

    @Autowired
    private WishService wishService;
    @Autowired
    private UserService userService;

    @PostMapping("/implements")
    public ModelAndView implementsWish(WishVO wishVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 输入的实现人信息不能为空
            if (StringUtils.isEmpty(wishVO.getImplementsPersonInfo())) {
                view.addObject("implementsPersonInfoIsEmpty", Boolean.TRUE);
                view.addObject("wishId", wishVO.getWishId());
                view.setViewName("cover/implements_wish");
                return view;
            }

            UserDTO userDTO = userService.getUserByUserInfo(wishVO.getImplementsPersonInfo());
            // 输入的实现人信息是否存在
            if (Objects.isNull(userDTO)) {
                view.addObject("implementsPersonInfoIsNotExists", Boolean.TRUE);
                view.addObject("wishId", wishVO.getWishId());
                view.setViewName("cover/implements_wish");
                return view;
            }

            WishDTO wishDTO = wishService.getWishByWishId(wishVO.getWishId());
            // 判断愿望是否已经实现
            if (Boolean.TRUE.equals(wishDTO.getIsRealized())) {
                view.setViewName("cover/wish_is_realized");
                return view;
            }

            // 实现愿望
            wishService.fulfillmentWishById(wishVO.getWishId());

            String name = StringUtils.isEmpty(user.getNickName()) ? user.getUserName() : user.getNickName();
            // 通知实现人
            EmailUtil.sendMail(userDTO.getUserEmail(), this.sendWishFulfillmentNotificationEmail(name, wishDTO.getWishInfo()), "Wish Fulfillment Notification");
            view.setViewName("wish/implements_wish_success");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

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
                return this.toBlowOutCandles();
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

    @GetMapping("/toImplementsWish")
    public ModelAndView toImplementsWish(@RequestParam("wishId") String wishId,
                                         HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            view.addObject("wishId", wishId);
            view.setViewName("cover/implements_wish");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    /**
     * 跳转许愿动画页面
     *
     * @return 模版
     */
    private ModelAndView toBlowOutCandles() {
        ModelAndView view = new ModelAndView();
        view.setViewName("wish/blow_out_candles");
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
    private synchronized ModelAndView wishProvider(ModelAndView view, String userId) throws Exception {
        Integer notImplementedWishCount = wishService.getNotImplementedWishCount(userId);
        List<WishDTO> multipleRandomWish = wishService.getMultipleRandomWish(userId, IntegerConstant.FIVE);
        view.addObject("notImplementedWishCount", notImplementedWishCount);
        view.addObject("multipleRandomWish", multipleRandomWish);
        view.setViewName("wish/wish");
        return view;
    }

    /**
     * 给实现人发送愿望实现通知邮件
     *
     * @param name 需要实现愿望的用户名称（昵称/用户名）
     * @param wishInfo 愿望内容信息
     * @return 邮件模版信息
     */
    private String sendWishFulfillmentNotificationEmail(String name, String wishInfo) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>愿望实现通知</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <h1>\n" +
                "    愿望实现通知\n" +
                "  </h1>\n" +
                "  <h2>\n" +
                name +
                "    已将您选为他（她）的愿望实现人，请您尽快实现其愿望\uD83D\uDE0A。\n" +
                "  </h2>\n" +
                "  <h2>\n" +
                "    愿望内容：" + wishInfo + "。\n" +
                "  </h2>\n" +
                "</body>\n" +
                "</html>";
    }

}
