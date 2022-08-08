package cloud.lihan.rewardsprogram.common.constants;

/**
 * 激励值规则常量
 *
 * @author hanyun.li
 * @createTime 2022/07/25 15:11:00
 */
public interface IncentiveValueRuleConstant {

    /**
     * 累计完成一天奖励的激励值
     */
    Integer DAY_PLAN = 1;

    /**
     * 累计完成一周奖励的激励值
     */
    Integer WEEK_PLAN = DAY_PLAN * IntegerConstant.SEVEN + IntegerConstant.ONE;

    /**
     * 累计完成一个月奖励的激励值
     */
    Integer MOUTH_PLAN = WEEK_PLAN * IntegerConstant.FOUR + IntegerConstant.THERE;

    /**
     * 累计完成一年奖励的激励值
     */
    Integer YEAR_PLAN = MOUTH_PLAN * (IntegerConstant.TEN + IntegerConstant.TWO) + IntegerConstant.TEN + IntegerConstant.TWO;


    /**
     * 以下是许愿消耗的激励值规则
     */
    /***************************************/

    /**
     * 每个愿望需要消耗的激励值
     */
    Integer EACH_WISH = IntegerConstant.THERE * IntegerConstant.SEVEN * IntegerConstant.FIVE;

}
