package cloud.lihan.rewardsprogram.service.impl;

import cloud.lihan.rewardsprogram.common.constants.IncentiveValueRuleConstant;
import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.dao.inner.WishDao;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.dto.WishDTO;
import cloud.lihan.rewardsprogram.entity.document.WishDocument;
import cloud.lihan.rewardsprogram.manager.WishManager;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.service.inner.WishService;
import cloud.lihan.rewardsprogram.vo.WishVO;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(rollbackFor = Exception.class)
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
    public WishDTO getSingeRandomWish(String userId) throws IOException {
        List<WishDTO> multipleRandomWish = this.getMultipleRandomWish(userId, IntegerConstant.ONE);
        if (CollectionUtils.isEmpty(multipleRandomWish)) {
            return new WishDTO();
        }
        return wishManager.wishDocumentConvertWishDTO(multipleRandomWish.get(IntegerConstant.ZERO));
    }

    @Override
    public List<WishDTO> getMultipleRandomWish(String userId, Integer wishNum) throws IOException {
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(s -> s.term(t -> t
                                .field("userId.keyword")
                                .value(userId))
                        )
                ).build();
        List<WishDocument> wishDocuments = wishDao.getRandomNumbersWishDocuments(wishNum, query);
        return wishManager.wishDocumentsConvertWishDTO(wishDocuments);
    }

    @Override
    public List<WishDTO> getMultipleNotImplementedRandomWish(String userId, Integer wishNum) throws IOException {
        // 筛选未实现的愿望
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(s -> s.term(t -> t
                                .field("userId.keyword")
                                .value(userId))
                        )
                        .must(s -> s.term(t -> t
                                .field("isRealized")
                                .value(Boolean.FALSE))
                        )
                ).build();
        List<WishDocument> wishDocuments = wishDao.getRandomNumbersWishDocuments(wishNum, query);
        return wishManager.wishDocumentsConvertWishDTO(wishDocuments);
    }

    @Override
    public List<WishDTO> getMultipleRealizedRandomWish(String userId, Integer wishNum) throws IOException {
        // 筛选已经实现的愿望
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(s -> s.term(t -> t
                                .field("userId.keyword")
                                .value(userId))
                        )
                        .must(s -> s.term(t -> t
                                .field("isRealized")
                                .value(Boolean.TRUE))
                        )
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

    @Override
    public Integer getNotImplementedWishCount(String userId) throws IOException {
        return this.getWishCount(userId, Boolean.FALSE);
    }

    @Override
    public Integer getRealizedWishCount(String userId) throws IOException {
        return this.getWishCount(userId, Boolean.TRUE);
    }

    /**
     * 获取已/未实现的愿望数量
     *
     * @param userId 用户ID
     * @param isRealized true:已实现 false:未实现
     * @return 已/未实现的愿望数量
     * @throws IOException 异常信息
     */
    private Integer getWishCount(String userId, Boolean isRealized) throws IOException {
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(s -> s.term(t -> t
                                .field("userId.keyword")
                                .value(userId))
                        )
                        .must(s -> s.term(t -> t
                                .field("isRealized")
                                .value(isRealized))
                        )
                ).build();
        return wishDao.getWishDocumentCount(query);
    }

}
