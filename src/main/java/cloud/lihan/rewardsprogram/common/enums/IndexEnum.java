package cloud.lihan.rewardsprogram.common.enums;

/**
 * 索引名枚举
 *
 * @author hanyun.li
 * @createTime 2022/07/18 11:10:00
 */
public enum IndexEnum {

    /**
     * 索引
     */
    USER_INDEX("user", "用户索引"),
    WISH_INDEX("wish", "愿望索引"),
    PLAN_INDEX("plan", "计划索引");

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 描述信息
     */
    private String description;

    IndexEnum() {
    }

    IndexEnum(String indexName, String description) {
        this.indexName = indexName;
        this.description = description;
    }

    public final String getIndexName() {
        return indexName;
    }

    public final String getDescription() {
        return description;
    }

}
