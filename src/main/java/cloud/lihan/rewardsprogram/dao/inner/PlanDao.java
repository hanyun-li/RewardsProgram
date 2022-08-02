package cloud.lihan.rewardsprogram.dao.inner;

import cloud.lihan.rewardsprogram.entity.document.PlanDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 计划相关的数据方法
 *
 * @author hanyun.li
 * @createTime 2022/07/20 10:32:00
 */
public interface PlanDao {

    /**
     * 创建计划文档
     *
     * @param planDocument 计划文档
     * @throws IOException 异常信息
     */
    void createPlanDocument(PlanDocument planDocument) throws IOException;

    /**
     * 批量创建多个计划文档
     *
     * @param planDocuments 计划文档
     * @throws IOException 异常信息
     */
    void bulkCreatePlanDocument(List<PlanDocument> planDocuments) throws IOException;

    /**
     * 根据ID删除计划文档
     *
     * @param planDocumentId 计划文档标识
     * @throws IOException 异常信息
     */
    void deletePlanDocumentById(String planDocumentId) throws IOException;

    /**
     * 根据特定条件更新计划信息
     *
     * @param source 更新语句
     * @param optionsMaps   更新(操作)集合
     * @param updateByQuery 自定义更新条件
     * @throws IOException 异常信息
     */
    void updatePlanSingleField(Map<String, JsonData> optionsMaps, String source, Query updateByQuery) throws IOException;

    /**
     * 根据ID获取计划文档信息
     *
     * @param planDocumentId 计划文档ID
     * @return {@link PlanDocument} 计划信息
     * @throws IOException 异常信息
     */
    PlanDocument getPlanByPlanDocumentId(String planDocumentId) throws IOException;

    /**
     * 根据特定条件查询单个计划文档
     *
     * @param query 自定义查询条件
     * @return {@link PlanDocument} 计划信息
     * @throws IOException 异常信息
     */
    PlanDocument getSinglePlanByQuery(Query query) throws IOException;

    /**
     * 根据特定条件查询多个个计划文档
     *
     * @param query 自定义查询条件
     * @return {@link PlanDocument} 计划信息 {@link cloud.lihan.rewardsprogram.common.constants.IntegerConstant} 查询数量控制常量
     * @throws IOException 异常信息
     */
    List<PlanDocument> getMultiplePlanByQuery(Query query) throws IOException;

}
