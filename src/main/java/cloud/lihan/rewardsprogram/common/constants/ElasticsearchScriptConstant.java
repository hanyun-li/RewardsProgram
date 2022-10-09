package cloud.lihan.rewardsprogram.common.constants;

/**
 * Elasticsearch自定义脚本
 *
 * @author hanyun.li
 * @createTime 2022/07/04 10:53:00
 */
public interface ElasticsearchScriptConstant {

    /**
     * 脚本语言
     */
    String SCRIPT_LANGUAGE = "painless";

    /**
     * 随机排序脚本
     */
    String RANDOM_SORT_SCRIPT = "{\n" +
            "\"sort\": {\n" +
            "    \"_script\": {\n" +
            "      \"script\": \"Math.random()\",\n" +
            "      \"type\": \"number\",\n" +
            "      \"order\": \"asc\"\n" +
            "        }\n" +
            "    }\n" +
            "}";

}
