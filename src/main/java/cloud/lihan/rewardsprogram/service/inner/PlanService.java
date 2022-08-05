package cloud.lihan.rewardsprogram.service.inner;

import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.vo.PlanVO;
import cloud.lihan.rewardsprogram.common.constants.PlanLimitConstant;

import java.io.IOException;
import java.util.List;

/**
 * 计划相关的业务方法
 *
 * @author hanyun.li
 * @createTime 2022/07/20 11:19:00
 */
public interface PlanService {

    /**
     * 保存计划
     *
     * @param planVO 计划信息
     * @throws Exception 异常信息
     */
    void savePlan(PlanVO planVO) throws Exception;

    /**
     * 批量保存计划
     *
     * @param planVOs 计划集合
     * @throws IOException 异常信息
     */
    void bulkSavePlan(List<PlanVO> planVOs) throws IOException;

    /**
     * 根据ID删除计划
     *
     * @param planId 计划ID
     * @throws IOException 异常信息
     */
    void deletePlanById(String planId) throws IOException;

    /**
     * 根据ID实现计划
     *
     * @param userDTO 用户传输对象
     * @param planId 计划ID
     * @throws IOException 异常信息
     */
    void finishPlanById(UserDTO userDTO, String planId) throws IOException;

    /**
     * 根据ID获取计划
     *
     * @param planId 计划ID
     * @return {@link PlanDTO} 计划信息
     * @throws IOException 异常信息
     */
    PlanDTO getPlanByPlanId(String planId) throws IOException;

    /**
     * 获取今日已完成的计划列表
     *
     * @param userId 用户ID
     * @return {@link List<PlanDTO>} 今日已完成的计划列表
     * @throws Exception 异常信息
     */
    List<PlanDTO> getTodayFinishedPlans(String userId) throws Exception;

    /**
     * 获取今日未完成的计划列表
     *
     * @param userId 用户ID
     * @return {@link List<PlanDTO>} 今日未完成的计划列表
     * @throws Exception 异常信息
     */
    List<PlanDTO> getTodayUnfinishedPlans(String userId) throws Exception;

    /**
     * 检查输入的计划信息是否在今日已经创建过
     *
     * @param planInfo 计划信息
     * @param userId 用户ID
     * @return true:已经创建 false:未创建
     * @throws Exception 异常信息
     */
    Boolean checkPlanInfo(String planInfo, String userId) throws Exception;

    /**
     * 是否可以创建计划（检测是否已达到当日最大创建计划次数）
     *
     * {@link PlanLimitConstant} 当天允许创建计划的最大次数
     * @param userDTO 用户信息
     * @return true：可以创建 false：不可以创建
     * @throws Exception 异常信息
     */
    Boolean canCreatePlan(UserDTO userDTO) throws Exception;

}
