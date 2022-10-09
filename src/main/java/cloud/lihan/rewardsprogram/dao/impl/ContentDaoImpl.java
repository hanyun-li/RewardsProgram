package cloud.lihan.rewardsprogram.dao.impl;

import cloud.lihan.rewardsprogram.common.constants.ElasticsearchScriptConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.enums.IndexEnum;
import cloud.lihan.rewardsprogram.common.utils.CurrentTimeUtil;
import cloud.lihan.rewardsprogram.dao.inner.ContentDao;
import cloud.lihan.rewardsprogram.entity.document.ContentDocument;
import cloud.lihan.rewardsprogram.entity.document.PlanDocument;
import cloud.lihan.rewardsprogram.entity.document.UserDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 帖子数据相关操作
 *
 * @author hanyun.li
 * @createTime 2022/09/29 11:06:00
 */
@Repository("contentDao")
public class ContentDaoImpl implements ContentDao {

    @Autowired
    private ElasticsearchClient esClient;

    @Override
    public void createContentDocument(ContentDocument contentDocument) throws IOException {
        contentDocument.setId(UUID.randomUUID().toString());
        CurrentTimeUtil.Time time = CurrentTimeUtil.newCurrentTimes();
        contentDocument.setCreateTime(time.getTime());
        contentDocument.setCreateTimestamp(time.getTimestamp());
        contentDocument.setUpdateTime(time.getTime());
        esClient.create(i -> i
                .index(IndexEnum.CONTENT_INDEX.getIndexName())
                .id(contentDocument.getId())
                .document(contentDocument)
        );
    }

    @Override
    public void deleteContentDocumentById(String contentId) throws IOException {
        esClient.delete(d -> d
                .id(contentId)
                .index(IndexEnum.CONTENT_INDEX.getIndexName())
        );
    }

    @Override
    public void updateContentField(Map<String, JsonData> optionsMaps, String source, Query updateByQuery) throws IOException {
        UpdateByQueryRequest update = UpdateByQueryRequest.of(u -> u
                .index(IndexEnum.CONTENT_INDEX.getIndexName())
                .query(updateByQuery)
                .script(s -> s.inline(i -> i
                        .lang(ElasticsearchScriptConstant.SCRIPT_LANGUAGE)
                        .params(optionsMaps)
                        .source(source)
                ))
        );
        esClient.updateByQuery(update);
    }

    @Override
    public List<ContentDocument> getMultipleContentByQuery() throws IOException {
        SearchResponse<ContentDocument> search = esClient.search(s -> s
                        .sort(so -> so
                                .field(f -> f.field("createTimestamp")
                                        .unmappedType(FieldType.Long)
                                        .order(SortOrder.Desc)
                                )
                        )
                        .index(IndexEnum.CONTENT_INDEX.getIndexName())
                        .size(IntegerConstant.ONE_HUNDRED)
                , ContentDocument.class);
        return this.processContent(search);
    }

    @Override
    public ContentDocument getContentById(String contentId) throws IOException {
        Query query = new Query.Builder()
                .ids(id -> id.values(contentId))
                .build();
        return this.findContentDocument(query);
    }

    /**
     * 组装帖子文档集合
     *
     * @param searchResponse 查询返回的响应体
     * @return 帖子文档集合
     */
    private List<ContentDocument> processContent(SearchResponse<ContentDocument> searchResponse) {
        List<ContentDocument> contentDocuments = new LinkedList<>();
        for (Hit<ContentDocument> hit : searchResponse.hits().hits()) {
            contentDocuments.add(hit.source());
        }
        return contentDocuments;
    }

    /**
     * 根据自定义查询条件获取单个帖子文档
     *
     * @param query 自定义查询条件
     * @return {@link ContentDocument} 帖子文档
     */
    private ContentDocument findContentDocument(Query query) throws IOException{
        SearchResponse<ContentDocument> search = esClient.search(s -> s
                        .index(IndexEnum.CONTENT_INDEX.getIndexName())
                        .query(query)
                        .size(IntegerConstant.ONE)
                , ContentDocument.class);
        List<ContentDocument> contentDocuments = this.processContent(search);
        return CollectionUtils.isEmpty(contentDocuments) ? null : contentDocuments.get(IntegerConstant.ZERO);
    }

}
