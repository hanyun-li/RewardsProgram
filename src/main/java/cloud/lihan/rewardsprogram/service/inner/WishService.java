package cloud.lihan.rewardsprogram.service.inner;


import cloud.lihan.rewardsprogram.dto.UserDTO;
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
     * @param userDTO 用户传输对象
     * @param wishVO 愿望信息
     * @throws IOException 异常信息
     */
    void makingWish(UserDTO userDTO, WishVO wishVO) throws IOException;

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
     * @param wishId 愿望ID
     * @throws IOException 异常信息
     */
    void deleteWishById(String wishId) throws IOException;

    /**
     * 根据ID实现愿望
     *
     * @param wishId 愿望ID
     * @throws IOException 异常信息
     */
    void fulfillmentWishById(String wishId) throws IOException;

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
     * @param wishNum 需要获取的愿望数量
     * @return {@link List<WishDTO>}
     * @throws IOException 异常信息
     */
    List<WishDTO> getMultipleRandomWish(Integer wishNum) throws IOException;

    /**
     * 根据ID获取愿望
     *
     * @param wishId 愿望ID
     * @return {@link WishDTO} 愿望信息
     * @throws IOException 异常信息
     */
    WishDTO getWishByWishId(String wishId) throws IOException;

    /**
     * 检查输入的愿望信息是否已经存在
     *
     * @param wishInfo 愿望信息
     * @param userId 用户ID
     * @return true:已经存在 false:不存在
     * @throws Exception 异常信息
     */
    Boolean checkWishInfo(String wishInfo, String userId) throws Exception;

}
