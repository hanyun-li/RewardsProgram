package cloud.lihan.rewardsprogram.manager;

import cloud.lihan.rewardsprogram.dto.WishDTO;
import cloud.lihan.rewardsprogram.entety.document.WishDocument;
import cloud.lihan.rewardsprogram.vo.WishVO;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 愿望相关的实体对象之间的字段属性赋值
 *
 * @author hanyun.li
 * @createTime 2022/06/29 14:02:00
 */
@Component("wishManager")
public class WishManager {

    /**
     * 特定对象转换 {@link WishDocument} to {@link WishDTO}
     *
     * @param wishDocument 愿望文档实体
     * @return {@link WishDTO}
     */
    public WishDTO wishDocumentConvertWishDTO(WishDocument wishDocument) {
        if (Objects.isNull(wishDocument)) {
            return null;
        }
        WishDTO wishDTO = new WishDTO();
        wishDTO.setId(wishDocument.getId());
        wishDTO.setWishInfo(wishDocument.getWishInfo());
        wishDTO.setIsRealized(wishDocument.getIsRealized());
        wishDTO.setUserId(wishDocument.getUserId());
        wishDTO.setCreateTime(wishDocument.getCreateTime());
        wishDTO.setUpdateTime(wishDocument.getUpdateTime());
        return wishDTO;
    }

    /**
     * 特定集合转换 {@link List<WishDocument>} to {@link List<WishDTO>}
     *
     * @param wishDocuments 愿望文档实体集合
     * @return {@link List<WishDTO>}
     */
    public List<WishDTO> wishDocumentsConvertWishDTO(List<WishDocument> wishDocuments) {
        List<WishDTO> wishDTOS = new LinkedList<>();
        for (WishDocument wishDocument : wishDocuments) {
            wishDTOS.add(this.wishDocumentConvertWishDTO(wishDocument));
        }
        return wishDTOS;
    }

    /**
     * 特定对象转换 {@link WishVO} to {@link WishDocument}
     *
     * @param wishVO 愿望输入实体
     * @return {@link WishDocument}
     */
    public WishDocument wishVOConvertWishDocument(WishVO wishVO) {
        if (Objects.isNull(wishVO)) {
            return null;
        }
        WishDocument wishDocument = new WishDocument();
        wishDocument.setWishInfo(wishVO.getWishInfo());
        wishDocument.setUserId(wishVO.getUserId());
        return wishDocument;
    }

    /**
     * 特定对象转换 {@link List<WishVO>} to {@link List<WishDocument>}
     *
     * @param wishVOs 愿望输入实体集合
     * @return {@link List<WishDocument>}
     */
    public List<WishDocument> wishVOConvertsWishDocument(List<WishVO> wishVOs) {
        List<WishDocument> wishDTOS = new LinkedList<>();
        for (WishVO wishVO : wishVOs) {
            wishDTOS.add(this.wishVOConvertWishDocument(wishVO));
        }
        return wishDTOS;
    }
}
