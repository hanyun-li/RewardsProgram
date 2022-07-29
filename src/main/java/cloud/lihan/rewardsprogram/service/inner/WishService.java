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
     * @param userId 用户ID
     * @return {@link WishDTO}
     * @throws IOException 异常信息
     */
    WishDTO getSingeRandomWish(String userId) throws IOException;

    /**
     * 随机获取指定数量的愿望(最多获取10条)
     *
     * @param userId 用户ID
     * @param wishNum 需要获取的愿望数量
     * @return {@link List<WishDTO>}
     * @throws IOException 异常信息
     */
    List<WishDTO> getMultipleRandomWish(String userId, Integer wishNum) throws IOException;

    /**
     * 随机获取指定数量未被实现的愿望(最多获取10条)
     *
     * @param userId 用户ID
     * @param wishNum 需要获取的愿望数量
     * @return {@link List<WishDTO>}
     * @throws IOException 异常信息
     */
    List<WishDTO> getMultipleNotImplementedRandomWish(String userId, Integer wishNum) throws IOException;

    /**
     * 随机获取指定数量已经实现的愿望(最多获取10条)
     *
     * @param userId 用户ID
     * @param wishNum 需要获取的愿望数量
     * @return {@link List<WishDTO>}
     * @throws IOException 异常信息
     */
    List<WishDTO> getMultipleRealizedRandomWish(String userId, Integer wishNum) throws IOException;

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

    /**
     * 获取未实现的愿望数量
     *
     * @param userId 用户ID
     * @return 未实现的愿望数量
     * @throws IOException 异常信息
     */
    Integer getNotImplementedWishCount(String userId) throws IOException;

    /**
     * 获取已实现的愿望数量
     *
     * @param userId 用户ID
     * @return 已实现的愿望数量
     * @throws IOException 异常信息
     */
    Integer getRealizedWishCount(String userId) throws IOException;

}
