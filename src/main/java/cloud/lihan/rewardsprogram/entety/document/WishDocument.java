package cloud.lihan.rewardsprogram.entety.document;

import cloud.lihan.rewardsprogram.common.documents.BaseDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 愿望信息文档
 *
 * @author hanyun.li
 * @createTime 2022/06/28 17:30:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WishDocument extends BaseDocument {

    /**
     * 愿望信息
     */
    private String wishInfo;

    /**
     * 用户ID(UUID类型)
     */
    private String userId;

    /**
     * 是否已经实现 true:是 false:否
     */
    private Boolean isRealized;

}
