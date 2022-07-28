package cloud.lihan.rewardsprogram.dto.provider;

import lombok.Data;

/**
 * 许愿时，返回的相关信息实体
 *
 * @author hanyun.li
 * @createTime 2022/07/28 15:53:00
 */
@Data
public class WishProviderDTO {

    /**
     * 是否可以许愿 true:可以 false：不可以
     */
    private Boolean isIncentiveValueEnough;

    /**
     * 本次许愿需要消耗的激励值
     */
    private Integer currentConsumeIncentiveValue;

    /**
     * 本次许愿还需要多少激励值才够
     */
    private Integer differenceIncentiveValue;

}
