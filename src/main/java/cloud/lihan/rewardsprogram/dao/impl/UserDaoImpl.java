package cloud.lihan.rewardsprogram.dao.impl;

import cloud.lihan.rewardsprogram.common.constants.ElasticsearchScriptConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.constants.TimeFormatConstant;
import cloud.lihan.rewardsprogram.common.enums.IndexEnum;
import cloud.lihan.rewardsprogram.common.utils.CurrentTimeUtil;
import cloud.lihan.rewardsprogram.dao.inner.UserDao;
import cloud.lihan.rewardsprogram.entity.document.UserDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
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
 * 用户数据相关操作
 *
 * @author hanyun.li
 * @createTime 2022/07/08 14:52:00
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private ElasticsearchClient esClient;

    @Override
    public void createUserDocument(UserDocument userDocument) throws Exception {
        userDocument.setId(UUID.randomUUID().toString());
        CurrentTimeUtil.Time time = CurrentTimeUtil.newCurrentTimes();
        userDocument.setCreateTime(time.getTime());
        userDocument.setCreateTimestamp(time.getTimestamp());
        userDocument.setUpdateTime(time.getTime());
        String currentTime = CurrentTimeUtil.newCurrentTime(TimeFormatConstant.Y_M_D);
        userDocument.setLastTimeLoginFailTime(currentTime);
        userDocument.setCurrentDayLoginFailTimes(IntegerConstant.ZERO);
        userDocument.setLastTimeAddPlanTime(currentTime);
        userDocument.setCurrentDayCreatePlanTimes(IntegerConstant.ZERO);
        userDocument.setIncentiveValue(IntegerConstant.ZERO);
        userDocument.setLastSuccessfulLoginTime(currentTime);
        esClient.create(i -> i
                .index(IndexEnum.USER_INDEX.getIndexName())
                .id(userDocument.getId())
                .document(userDocument)
        );
    }

    @Override
    public void updateUserField(Map<String, JsonData> optionsMaps, String source, Query updateByQuery) throws IOException {
        UpdateByQueryRequest update = UpdateByQueryRequest.of(u -> u
                .index(IndexEnum.USER_INDEX.getIndexName())
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
    public UserDocument getUserByUserDocumentId(String userId) throws IOException {
        Query query = new Query.Builder()
                .ids(i -> i.values(userId))
                .build();
        return this.findUserDocument(query);
    }

    @Override
    public UserDocument getSingleUserByQuery(Query query) throws IOException {
        return this.findUserDocument(query);
    }

    @Override
    public List<UserDocument> getAllUser() throws IOException {
        SearchResponse<UserDocument> search = esClient.search(s -> s
                        .index(IndexEnum.USER_INDEX.getIndexName()), UserDocument.class);
        return this.processWish(search);
    }

    /**
     * 根据自定义查询条件获取单个用户文档
     *
     * @param query 自定义查询条件
     * @return {@link UserDocument} 用户文档
     */
    private UserDocument findUserDocument(Query query) throws IOException{
        SearchResponse<UserDocument> search = esClient.search(s -> s
                        .index(IndexEnum.USER_INDEX.getIndexName())
                        .query(query)
                        .size(IntegerConstant.ONE)
                , UserDocument.class);
        List<UserDocument> userDocuments = this.processWish(search);
        return CollectionUtils.isEmpty(userDocuments) ? null : userDocuments.get(IntegerConstant.ZERO);
    }

    /**
     * 组装用户集合
     *
     * @param searchResponse 查询返回的响应体
     * @return 用户集合
     */
    private List<UserDocument> processWish(SearchResponse<UserDocument> searchResponse) {
        List<UserDocument> userDocuments = new LinkedList<>();
        for (Hit<UserDocument> hit : searchResponse.hits().hits()) {
            userDocuments.add(hit.source());
        }
        return userDocuments;
    }

}
