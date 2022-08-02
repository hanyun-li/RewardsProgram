package cloud.lihan.rewardsprogram.entity.document;

import cloud.lihan.rewardsprogram.common.documents.BaseDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 计划信息文档
 *
 * @author hanyun.li
 * @createTime 2022/07/19 16:58:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanDocument extends BaseDocument {

    /**
     * 计划信息
     */
    private String planInfo;

    /**
     * 用户ID(UUID类型)
     */
    private String userId;

    /**
     * 是否已经完成 true:是 false:否
     */
    private Boolean isFinished;

}
