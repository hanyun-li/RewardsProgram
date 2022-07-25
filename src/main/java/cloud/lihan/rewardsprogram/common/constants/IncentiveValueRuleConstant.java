package cloud.lihan.rewardsprogram.common.constants;

/**
 * 激励值规则常量
 *
 * @author hanyun.li
 * @createTime 2022/07/25 15:11:00
 */
public interface IncentiveValueRuleConstant {

    Integer DAY_PLAN = 1;

    Integer WEEK_PLAN = DAY_PLAN * IntegerConstant.SEVEN + IntegerConstant.ONE;

    Integer MOUTH_PLAN = WEEK_PLAN * IntegerConstant.FOUR + IntegerConstant.THERE;

    Integer YEAR_PLAN = MOUTH_PLAN * (IntegerConstant.TEN + IntegerConstant.TWO) + IntegerConstant.TEN + IntegerConstant.TWO;

}
