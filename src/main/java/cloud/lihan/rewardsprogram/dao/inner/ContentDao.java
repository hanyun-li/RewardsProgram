package cloud.lihan.rewardsprogram.dao.inner;

import cloud.lihan.rewardsprogram.entity.document.ContentDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 广场相关的数据操作方法
 *
 * @author hanyun.li
 * @createTime 2022/09/28 17:58:00
 */
public interface ContentDao {

    /**
     * 创建帖子文档
     *
     * @param contentDocument 帖子文档
     * @throws IOException 包含Elasticsearch异常信息
     */
    void createContentDocument(ContentDocument contentDocument) throws IOException;

    /**
     * 根据ID删除帖子文档
     *
     * @param contentId 帖子标识
     * @throws IOException 包含Elasticsearch异常信息
     */
    void deleteContentDocumentById(String contentId) throws IOException;

    /**
     * 根据自定义条件更新帖子文档
     *
     * @param optionsMaps 更新(操作)集合
     * @param source 更新语句
     * @param updateByQuery 自定义更新条件
     * @throws IOException 包含Elasticsearch异常信息
     */
    void updateContentField(Map<String, JsonData> optionsMaps, String source, Query updateByQuery) throws IOException;

    /**
     * 根据特定条件查询多个帖子文档
     *
     * @return {@link ContentDocument} 帖子信息
     * @throws IOException 异常信息
     */
    List<ContentDocument> getMultipleContentByQuery() throws IOException;

    /**
     * 根据帖子ID获取帖子文档
     *
     * @param contentId 帖子ID
     * @return {@link ContentDocument} 帖子文档
     * @throws IOException 异常信息
     */
    ContentDocument getContentById(String contentId) throws IOException;

}
