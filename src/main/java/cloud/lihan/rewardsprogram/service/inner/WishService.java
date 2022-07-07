package cloud.lihan.rewardsprogram.service.inner;


import cloud.lihan.rewardsprogram.dto.WishDTO;
import cloud.lihan.rewardsprogram.vo.WishVO;

import java.io.IOException;
import java.util.List;

/**
 * 愿望相关的业务方法
 *
 * @author hanyun.li
 * @createTime 2022/06/28 18:38:00
 */
public interface WishService {

    /**
     * 保存愿望
     *
     * @param wishVO 愿望信息
     * @throws IOException 异常信息
     */
    void saveWish(WishVO wishVO) throws IOException;

    /**
     * 批量保存愿望
     *
     * @param wishVOs 愿望集合
     * @throws IOException 异常信息
     */
    void bulkSaveWish(List<WishVO> wishVOs) throws IOException;

    /**
     * 根据ID删除愿望
     *
     * @param wishDocumentId 愿望文档标识
     * @return true:创建成功 false:创建失败
     * @throws IOException 异常信息
     */
    void deleteWishDocumentById(String wishDocumentId) throws IOException;

    /**
     * 根据ID实现愿望
     *
     * @param wishDocumentId 愿望文档标识
     * @throws IOException 异常信息
     */
    void fulfillmentWishById(String wishDocumentId) throws IOException;

    /**
     * 随机获取一个愿望
     *
     * @return {@link WishDTO}
     * @throws IOException 异常信息
     */
    WishDTO getSingeRandomWish() throws IOException;

    /**
     * 随机获取指定数量的愿望(最多获取10条)
     *
     * @param wishDocumentNum 需要获取的愿望文档数量
     * @return {@link List<WishDTO>}
     * @throws IOException 异常信息
     */
    List<WishDTO> getMultipleRandomWish(Integer wishDocumentNum) throws IOException;

    /**
     * 根据ID获取愿望
     *
     * @param wishDocumentId 愿望主键
     * @return {@link WishDTO}
     * @throws IOException 异常信息
     */
    WishDTO getWishByWishDocumentId(String wishDocumentId) throws IOException;

}
