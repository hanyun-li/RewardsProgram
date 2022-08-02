package cloud.lihan.rewardsprogram.dao.impl;

import cloud.lihan.rewardsprogram.common.constants.ElasticsearchScriptConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import cloud.lihan.rewardsprogram.common.enums.IndexEnum;
import cloud.lihan.rewardsprogram.common.utils.CurrentTimeUtil;
import cloud.lihan.rewardsprogram.dao.inner.WishDao;
import cloud.lihan.rewardsprogram.entity.document.WishDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 愿望数据相关操作
 *
 * @author hanyun.li
 * @createTime 2022/06/28 18:39:00
 */
@Repository("wishDao")
public class WishDaoImpl implements WishDao {

    @Autowired
    private ElasticsearchClient esClient;

    @Override
    public void createWishDocument(WishDocument wishDocument) throws IOException {
        wishDocument.setId(UUID.randomUUID().toString());
        CurrentTimeUtil.Time time = CurrentTimeUtil.newCurrentTimes();
        wishDocument.setCreateTime(time.getTime());
        wishDocument.setCreateTimestamp(time.getTimestamp());
        wishDocument.setUpdateTime(time.getTime());
        wishDocument.setIsRealized(Boolean.FALSE);
        esClient.create(i -> i
                .index(IndexEnum.WISH_INDEX.getIndexName())
                .id(wishDocument.getId())
                .document(wishDocument)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bulkCreateWishDocument(List<WishDocument> wishDocuments) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (WishDocument wishDocument : wishDocuments) {
            wishDocument.setId(UUID.randomUUID().toString());
            SimpleDateFormat format = new SimpleDateFormat(TimeFormatConstant.STANDARD);
            wishDocument.setCreateTime(format.format(new Date()));
            wishDocument.setUpdateTime(format.format(new Date()));
            wishDocument.setIsRealized(Boolean.FALSE);
            br.operations(op -> op
                    .index(idx -> idx
                            .index(IndexEnum.WISH_INDEX.getIndexName())
                            .id(wishDocument.getId())
                            .document(wishDocument)
                    )
            );
        }
        esClient.bulk(br.build());
    }

    @Override
    public void deleteWishDocumentById(String wishDocumentId) throws IOException {
        esClient.delete(d -> d
                .index(IndexEnum.WISH_INDEX.getIndexName())
                .id(wishDocumentId)
        );
    }

    @Override
    public void updateWishSingleField(Map<String, JsonData> optionsMaps, String source, Query query) throws IOException {
        UpdateByQueryRequest update = UpdateByQueryRequest.of(u -> u
                .index(IndexEnum.WISH_INDEX.getIndexName())
                .query(query)
                .script(s -> s.inline(i -> i
                        .lang(ElasticsearchScriptConstant.SCRIPT_LANGUAGE)
                        .params(optionsMaps)
                        .source(source)
                ))
        );
        esClient.updateByQuery(update);
    }

    @Override
    public WishDocument getWishDocumentById(String wishDocumentId) throws IOException {
        Query query = new Query.Builder()
                .term(t -> t.field("id").value(wishDocumentId))
                .build();
        SearchResponse<WishDocument> search = esClient.search(s -> s
                        .index(IndexEnum.WISH_INDEX.getIndexName())
                        .query(query)
                        .size(IntegerConstant.ONE)
                , WishDocument.class);
        List<WishDocument> wishDocuments = this.processWish(search);
        return CollectionUtils.isEmpty(wishDocuments) ? null : wishDocuments.get(IntegerConstant.ZERO);
    }

    @Override
    public List<WishDocument> getWishDocuments() throws IOException {
        SearchResponse<WishDocument> search = esClient.search(s -> s
                        .index(IndexEnum.WISH_INDEX.getIndexName())
                , WishDocument.class);
        return this.processWish(search);
    }

    @Override
    public List<WishDocument> getRandomNumbersWishDocuments(Integer wishDocumentNum, Query query) throws IOException {
        // 随机排序脚本
        Reader queryJson = new StringReader(ElasticsearchScriptConstant.RANDOM_SORT_SCRIPT);
        SearchResponse<WishDocument> search = esClient.search(s -> s
                        .withJson(queryJson)
                        .index(IndexEnum.WISH_INDEX.getIndexName())
                        .size(wishDocumentNum)
                        .query(query)
                , WishDocument.class);
        return processWish(search);
    }

    @Override
    public Integer getWishDocumentCount(Query query) throws IOException {
        CountResponse count = esClient.count(c -> c
                .index(IndexEnum.WISH_INDEX.getIndexName())
                .query(query)
        );
        return (int) count.count();
    }

    /**
     * 组装愿望集合
     *
     * @param searchResponse 查询返回的响应体
     * @return 愿望集合
     */
    private List<WishDocument> processWish(SearchResponse<WishDocument> searchResponse) {
        List<WishDocument> wishDocuments = new LinkedList<>();
        for (Hit<WishDocument> hit : searchResponse.hits().hits()) {
            wishDocuments.add(hit.source());
        }
        return wishDocuments;
    }

}
