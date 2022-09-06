package cloud.lihan.rewardsprogram.vo;

import lombok.Data;

/**
 * 愿望实体数据传输（接收用）
 *
 * @author hanyun.li
 * @createTime 2022/06/29 11:16:00
 */
@Data
public class WishVO {

    /**
     * 愿望信息
     */
    private String wishInfo;

    /**
     * 用户ID(UUID类型)
     */
    private String userId;

    /**
     * 愿望ID
     */
    private String wishId;

    /**
     * 愿望实现人信息（昵称/用户名或邮箱地址）
     */
    private String implementsPersonInfo;

}
