package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.constants.PlanLimitConstant;
import cloud.lihan.rewardsprogram.common.utils.LoginUtil;
import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.service.inner.PlanService;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.PlanVO;
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
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }

            // 输入的计划信息不能为空
            if (StringUtils.isEmpty(planVO.getPlanInfo())) {
                view.addObject("planInfoErrorInfo", "请输入计划信息！");
                return this.planProvider(view, userId);
            }

            // 输入的计划信息不能重复（当天内有效）
            if (planService.checkPlanInfo(planVO.getPlanInfo(), userId)) {
                view.addObject("planInfoErrorInfo", "计划信息重复，请重新输入！");
                return this.planProvider(view, userId);
            }

            // 检测当天创建的计划数是否超过最大限制次数
            if (planService.canCreatePlan(user)) {
                planVO.setUserId(userId);
                planService.savePlan(planVO);
                Thread.sleep(1000);
                return this.planProvider(view, userId);
            }

            view.addObject("createPlanMaxTimes", PlanLimitConstant.CURRENT_DAY_CREATE_PLAN_MAX_TIMES);
            view.setViewName("cover/create_plan_limit");
            return view;
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/delete")
    public ModelAndView deletePlan(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }
            planService.deletePlanById(id);
            Thread.sleep(1000);
            return this.planProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    @GetMapping("/finish")
    public ModelAndView finishPlan(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }
            planService.finishPlanById(user, id);
            Thread.sleep(1000);
            return this.planProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    /**
     * 去到计划页
     *
     * @return 模版
     * @throws Exception 异常信息
     */
    @GetMapping
    public ModelAndView toPlan(HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        if (LoginUtil.checkLogin(request)) {
            String userId = LoginUtil.getLoginTokenByRequest(request);
            UserDTO user = userService.getUserByUserId(userId);
            if (Objects.isNull(user)) {
                view.setViewName("cover/not_logger_in");
                return view;
            }
            return this.planProvider(view, userId);
        }
        view.setViewName("cover/not_logger_in");
        return view;
    }

    /**
     * 计划提供
     *
     * @param view 模版
     * @param userId 用户ID
     * @return 模版
     * @throws Exception 异常信息
     */
    private synchronized ModelAndView planProvider(ModelAndView view, String userId) throws Exception {
        List<PlanDTO> todayUnfinishedPlans = planService.getTodayUnfinishedPlans(userId);
        List<PlanDTO> todayFinishedPlans = planService.getTodayFinishedPlans(userId);
        view.addObject("todayUnfinishedPlans", todayUnfinishedPlans);
        view.addObject("todayFinishedPlans", todayFinishedPlans);
        view.setViewName("plan/plan");
        return view;
    }

}
