package cloud.lihan.rewardsprogram.dto;

import cloud.lihan.rewardsprogram.entity.document.UserDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体数据传输（输出用）
 *
 * @author hanyun.li
 * @createTime 2022/07/08 14:45:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends UserDocument {

}
