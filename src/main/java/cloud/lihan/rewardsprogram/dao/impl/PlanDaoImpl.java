package cloud.lihan.rewardsprogram.dao.impl;

import cloud.lihan.rewardsprogram.common.constants.ElasticsearchScriptConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import cloud.lihan.rewardsprogram.common.enums.IndexEnum;
import cloud.lihan.rewardsprogram.common.utils.CurrentTimeUtil;
import cloud.lihan.rewardsprogram.dao.inner.PlanDao;
import cloud.lihan.rewardsprogram.entety.document.PlanDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
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
 * 计划数据相关操作
 *
 * @author hanyun.li
 * @createTime 2022/07/20 10:44:00
 */
@Repository("planDao")
public class PlanDaoImpl implements PlanDao {

    @Autowired
    private ElasticsearchClient esClient;

    @Override
    public void createPlanDocument(PlanDocument planDocument) throws IOException {
        planDocument.setId(UUID.randomUUID().toString());
        CurrentTimeUtil.Time time = CurrentTimeUtil.newCurrentTimes();
        planDocument.setCreateTime(time.getTime());
        planDocument.setCreateTimestamp(time.getTimestamp());
        planDocument.setUpdateTime(time.getTime());
        planDocument.setIsFinished(Boolean.FALSE);
        esClient.create(i -> i
                .index(IndexEnum.PLAN_INDEX.getIndexName())
                .id(planDocument.getId())
                .document(planDocument)
        );
    }

    @Override
    public void bulkCreatePlanDocument(List<PlanDocument> planDocuments) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (PlanDocument planDocument : planDocuments) {
            planDocument.setId(UUID.randomUUID().toString());
            CurrentTimeUtil.Time time = CurrentTimeUtil.newCurrentTimes();
            planDocument.setCreateTime(time.getTime());
            planDocument.setCreateTimestamp(time.getTimestamp());
            planDocument.setUpdateTime(time.getTime());
            planDocument.setIsFinished(Boolean.FALSE);
            br.operations(op -> op
                    .index(i -> i
                            .index(IndexEnum.PLAN_INDEX.getIndexName())
                            .id(planDocument.getId())
                            .document(planDocument)
                    )
            );
        }
        esClient.bulk(br.build());
    }

    @Override
    public void deletePlanDocumentById(String planDocumentId) throws IOException {
        esClient.delete(d -> d
                .index(IndexEnum.PLAN_INDEX.getIndexName())
                .id(planDocumentId)
        );
    }

    @Override
    public void updatePlanSingleField(Map<String, JsonData> optionsMaps, String source, Query updateByQuery) throws IOException {
        UpdateByQueryRequest update = UpdateByQueryRequest.of(u -> u
                .index(IndexEnum.PLAN_INDEX.getIndexName())
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
    public PlanDocument getPlanByPlanDocumentId(String planDocumentId) throws IOException {
        Query query = new Query.Builder()
                .term(t -> t.field("id").value(planDocumentId))
                .build();
        return this.findSinglePlanDocument(query);
    }

    @Override
    public PlanDocument getSinglePlanByQuery(Query query) throws IOException {
        return this.findSinglePlanDocument(query);
    }

    @Override
    public List<PlanDocument> getMultiplePlanByQuery(Query query) throws IOException {
        return this.findMultiplePlanDocument(query, IntegerConstant.FIVE);
    }

    /**
     * 根据自定义查询条件获取单个计划文档
     *
     * @param query 自定义查询条件
     * @return {@link PlanDocument} 计划文档
     */
    private PlanDocument findSinglePlanDocument(Query query) throws IOException {
        List<PlanDocument> planDocuments = this.findMultiplePlanDocument(query, IntegerConstant.ONE);
        return CollectionUtils.isEmpty(planDocuments) ? null : planDocuments.get(IntegerConstant.ZERO);
    }

    /**
     * 根据自定义查询条件获取多个计划文档
     *
     * @param query 自定义查询条件
     * @return {@link PlanDocument} 计划文档
     */
    private List<PlanDocument> findMultiplePlanDocument(Query query, Integer queryDocumentSize) throws IOException {
        SearchResponse<PlanDocument> search = esClient.search(s -> s
                        .sort(so -> so
                                .field(f -> f
                                        .field("createTimestamp")
                                        // 注意：该类型如果不指定，当索引中没有文档的时候，会报错：all shards failed！
                                        .unmappedType(FieldType.Long)
                                        .order(SortOrder.Desc)
                                )
                        )
                        .index(IndexEnum.PLAN_INDEX.getIndexName())
                        .query(query)
                        .size(queryDocumentSize)
                , PlanDocument.class);
        return this.processWish(search);
    }

    /**
     * 组装计划文档集合
     *
     * @param searchResponse 查询返回的响应体
     * @return 计划文档集合
     */
    private List<PlanDocument> processWish(SearchResponse<PlanDocument> searchResponse) {
        List<PlanDocument> planDocuments = new LinkedList<>();
        for (Hit<PlanDocument> hit : searchResponse.hits().hits()) {
            planDocuments.add(hit.source());
        }
        return planDocuments;
    }

}
