package cloud.lihan.rewardsprogram.service.impl;

import cloud.lihan.rewardsprogram.common.constants.IncentiveValueRuleConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.dao.inner.WishDao;
import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.dto.WishDTO;
import cloud.lihan.rewardsprogram.entety.document.WishDocument;
import cloud.lihan.rewardsprogram.manager.WishManager;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.service.inner.WishService;
import cloud.lihan.rewardsprogram.vo.WishVO;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 愿望相关的业务细节
 *
 * @author hanyun.li
 * @createTime 2022/06/28 18:38:00
 */
@Service("WishService")
public class WishServiceImpl implements WishService {

    @Autowired
    private WishDao wishDao;
    @Autowired
    private WishManager wishManager;
    @Autowired
    private UserService userService;

    @Override
    public void makingWish(UserDTO userDTO, WishVO wishVO) throws IOException {
        userService.reduceIncentiveValue(userDTO, IncentiveValueRuleConstant.FIRST_WISH);
        wishDao.createWishDocument(wishManager.wishVOConvertWishDocument(wishVO));
    }

    @Override
    public void bulkSaveWish(List<WishVO> wishVOs) throws IOException {
        wishDao.bulkCreateWishDocument(wishManager.wishVOConvertsWishDocument(wishVOs));
    }

    @Override
    public void deleteWishById(String wishId) throws IOException {
        wishDao.deleteWishDocumentById(wishId);
    }

    @Override
    public void fulfillmentWishById(String wishId) throws IOException {
        // 指定主键
        Query query = new Query.Builder()
                .ids(id -> id.values(wishId))
                .build();
        // 组装更新map
        Map<String, JsonData> optionMaps = new HashMap<>(IntegerConstant.ONE);
        optionMaps.put("isRealized", JsonData.of(Boolean.TRUE));
        String source = "ctx._source.isRealized = params.isRealized";
        wishDao.updateWishSingleField(optionMaps, source, query);
    }

    @Override
    public WishDTO getSingeRandomWish() throws IOException {
        List<WishDTO> multipleRandomWish = this.getMultipleRandomWish(IntegerConstant.ONE);
        if (CollectionUtils.isEmpty(multipleRandomWish)) {
            return new WishDTO();
        }
        return wishManager.wishDocumentConvertWishDTO(multipleRandomWish.get(IntegerConstant.ZERO));
    }

    @Override
    public List<WishDTO> getMultipleRandomWish(Integer wishNum) throws IOException {
        // 筛选没有被实现的愿望
        Query query = new Query.Builder().term(t -> t
                .field("isRealized")
                .value(v -> v.booleanValue(Boolean.FALSE))
        ).build();
        List<WishDocument> wishDocuments = wishDao.getRandomNumbersWishDocuments(wishNum, query);
        return wishManager.wishDocumentsConvertWishDTO(wishDocuments);
    }

    @Override
    public WishDTO getWishByWishId(String wishId) throws IOException {
        WishDocument wishDocument = wishDao.getWishDocumentById(wishId);
        return wishManager.wishDocumentConvertWishDTO(wishDocument);
    }

    @Override
    public Boolean checkWishInfo(String wishInfo, String userId) throws Exception {
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(s -> s.term(t -> t
                                // 注意：当wishInfo字段内容为中文时，此处会进行分词匹配，导致搜索不到结果，需要在字段后面添加"keyword"进行不分词搜索
                                .field("wishInfo.keyword")
                                .value(wishInfo))
                        )
                        .must(s -> s.term(t -> t
                                .field("isRealized")
                                .value(Boolean.FALSE))
                        )
                        .must(s -> s.term(t -> t
                                .field("userId.keyword")
                                .value(userId))
                        )
                ).build();
        List<WishDTO> wishDTOS = wishManager.wishDocumentsConvertWishDTO(wishDao.getRandomNumbersWishDocuments(IntegerConstant.FIVE, query));
        return CollectionUtils.isEmpty(wishDTOS) ? Boolean.FALSE : Boolean.TRUE;
    }

}
