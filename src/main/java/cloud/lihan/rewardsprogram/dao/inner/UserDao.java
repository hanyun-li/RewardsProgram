package cloud.lihan.rewardsprogram.dao.inner;


import cloud.lihan.rewardsprogram.entity.document.UserDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;

import java.io.IOException;
import java.util.Map;

/**
 * 用户相关的数据方法
 *
 * @author hanyun.li
 * @createTime 2022/07/08 14:39:00
 */
public interface UserDao {

    /**
     * 创建用户文档
     *
     * @param userDocument 用户文档
     * @throws IOException 异常信息
     */
    void createUserDocument(UserDocument userDocument) throws IOException;

    /**
     * 根据特定条件更新用户信息
     *
     * @param source 更新语句
     * @param optionsMaps   更新(操作)集合
     * @param updateByQuery 自定义更新条件
     * @throws IOException 异常信息
     */
    void updateUserField(Map<String, JsonData> optionsMaps, String source, Query updateByQuery) throws IOException;

    /**
     * 根据ID获取用户文档信息
     *
     * @param userDocumentId 用户文档ID
     * @return {@link UserDocument} 用户信息
     * @throws IOException 异常信息
     */
    UserDocument getUserByUserDocumentId(String userDocumentId) throws IOException;

    /**
     * 根据特定条件查询单个用户文档
     *
     * @param query 自定义查询条件
     * @return {@link UserDocument} 用户信息
     * @throws IOException 异常信息
     */
    UserDocument getSingleUserByQuery(Query query) throws IOException;

}
