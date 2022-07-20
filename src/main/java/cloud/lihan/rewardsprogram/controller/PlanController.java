package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.utils.LoginUtil;
import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.PlanService;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.PlanVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author hanyun.li
 * @createTime 2022/07/20 16:01:00
 */
@Slf4j
@Controller("planController")
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ModelAndView savePlan(PlanVO planVO, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(LoginUtil.getLoginTokenByRequest(request));
            if (Objects.isNull(user)) {
                log.error("PlanController.savePlan() exist error! error info : [userId:{} not exist!]", userId);
                view.setViewName("cover/not_logger_in");
                return view;
            }
            planVO.setUserId(userId);
            planService.savePlan(planVO);
            List<PlanDTO> todayUnfinishedPlans = planService.getTodayUnfinishedPlans(userId);
            List<PlanDTO> todayFinishedPlans = planService.getTodayFinishedPlans(userId);
            view.addObject("todayUnfinishedPlans", todayUnfinishedPlans);
            view.addObject("todayFinishedPlans", todayFinishedPlans);
            view.setViewName("product/product");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

}
