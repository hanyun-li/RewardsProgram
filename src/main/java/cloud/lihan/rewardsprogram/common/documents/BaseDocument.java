package cloud.lihan.rewardsprogram.common.documents;

import lombok.Data;

/**
 * Elasticsearch文档通用字段
 *
 * @author hanyun.li
 * @createTime 2022/06/28 18:12:00
 */
@Data
public class BaseDocument {

    /**
     * 序号
     */
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

}
