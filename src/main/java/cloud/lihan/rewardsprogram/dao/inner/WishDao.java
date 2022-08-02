package cloud.lihan.rewardsprogram.dao.inner;

import cloud.lihan.rewardsprogram.entity.document.WishDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 愿望相关的数据方法
 *
 * @author hanyun.li
 * @createTime 2022/06/29 10:16:00
 */
public interface WishDao {

    /**
     * 创建单个愿望文档
     *
     * @param wishDocument 愿望文档
     * @throws IOException 异常信息
     */
    void createWishDocument(WishDocument wishDocument) throws IOException;

    /**
     * 批量创建多个愿望文档
     *
     * @param wishDocuments 愿望文档
     * @throws IOException 异常信息
     */
    void bulkCreateWishDocument(List<WishDocument> wishDocuments) throws IOException;

    /**
     * 根据ID删除愿望文档
     *
     * @param wishDocumentId 愿望文档标识
     * @throws IOException 异常信息
     */
    void deleteWishDocumentById(String wishDocumentId) throws IOException;

    /**
     * 根据自定义条件更新愿望文档的
     *
     * @param source 更新语句
     * @param optionsMaps 更新(操作)集合
     * @param updateByQuery 自定义更新条件
     * @throws IOException 异常信息
     */
    void updateWishSingleField(Map<String, JsonData> optionsMaps, String source, Query updateByQuery) throws IOException;

    /**
     * 根据ID获取愿望文档
     *
     * @param wishDocumentId 愿望文档标识
     * @return {@link WishDocument}
     * @throws IOException 异常信息
     */
    WishDocument getWishDocumentById(String wishDocumentId) throws IOException;

    /**
     * 获取所有愿望文档
     *
     * @return {@link List<WishDocument>}
     * @throws IOException 异常信息
     */
    List<WishDocument> getWishDocuments() throws IOException;

    /**
     * 随机获取指定数量的愿望文档
     *
     * @param wishDocumentNum 需要获取的愿望文档数量
     * @param query 自定义查询条件
     * @return {@link List<WishDocument>}
     * @throws IOException 异常信息
     */
    List<WishDocument> getRandomNumbersWishDocuments(Integer wishDocumentNum, Query query) throws IOException;

    /**
     * 获取愿望文档数量
     *
     * @param query 自定义查询条件
     * @return 愿望文档数量
     * @throws IOException 异常信息
     */
    Integer getWishDocumentCount(Query query) throws IOException;

}
