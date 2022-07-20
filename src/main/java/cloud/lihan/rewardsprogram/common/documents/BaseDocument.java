package cloud.lihan.rewardsprogram.common.documents;

import lombok.Data;

import java.io.Serializable;

/**
 * Elasticsearch文档通用字段
 *
 * @author hanyun.li
 * @createTime 2022/06/28 18:12:00
 */
@Data
public class BaseDocument implements Serializable {

    private static final long serialVersionUID = -5073605700429341545L;

    /**
     * 序号
     */
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建时间(时间戳)
     */
    private Long createTimestamp;

    /**
     * 更新时间
     */
    private String updateTime;

}
