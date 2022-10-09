package cloud.lihan.rewardsprogram.vo;

import lombok.Data;

/**
 * 计划实体数据传输（接收用）
 *
 * @author hanyun.li
 * @createTime 2022/07/20 10:31:00
 */
@Data
public class PlanVO {

    /**
     * 计划信息
     */
    private String planInfo;

    /**
     * 用户ID(UUID类型)
     */
    private String userId;

}
