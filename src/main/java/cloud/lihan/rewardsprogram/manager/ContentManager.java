package cloud.lihan.rewardsprogram.manager;

import cloud.lihan.rewardsprogram.dto.ContentDTO;
import cloud.lihan.rewardsprogram.entity.document.ContentDocument;
import cloud.lihan.rewardsprogram.vo.ContentVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 帖子相关的实体对象之间的字段属性赋值
 * 
 * @author hanyun.li
 * @createTime 2022/09/29 17:21:00
 */
@Component("contentManager")
public class ContentManager {

    /**
     * 特定对象转换 {@link ContentDocument} to {@link ContentDTO}
     *
     * @param contentDocument 帖子文档实体
     * @return {@link ContentDTO}
     */
    public ContentDTO contentDocumentConvertContentDTO(ContentDocument contentDocument) {
        if (Objects.isNull(contentDocument)) {
            return null;
        }
        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setId(contentDocument.getId());
        contentDTO.setContent(contentDocument.getContent());
        contentDTO.setImgUrls(contentDocument.getImgUrls());
        contentDTO.setLikeUserIds(contentDocument.getLikeUserIds());
        contentDTO.setUserId(contentDocument.getUserId());
        contentDTO.setCreateTime(contentDocument.getCreateTime());
        contentDTO.setCreateTimestamp(contentDocument.getCreateTimestamp());
        contentDTO.setUpdateTime(contentDocument.getUpdateTime());
        return contentDTO;
    }

    /**
     * 特定集合转换 {@link List<ContentDocument>} to {@link List<ContentDTO>}
     *
     * @param contentDocuments 计划文档实体集合
     * @return {@link List<ContentDTO>}
     */
    public List<ContentDTO> contentDocumentsConvertContentDTO(List<ContentDocument> contentDocuments) {
        List<ContentDTO> contentDTOS = new LinkedList<>();
        for (ContentDocument contentDocument : contentDocuments) {
            contentDTOS.add(this.contentDocumentConvertContentDTO(contentDocument));
        }
        return contentDTOS;
    }

    /**
     * 特定对象转换 {@link ContentVO} to {@link ContentDocument}
     *
     * @param contentVO 计划输入实体
     * @return {@link ContentDocument}
     */
    public ContentDocument contentVOConvertContentDocument(ContentVO contentVO) {
        if (Objects.isNull(contentVO)) {
            return null;
        }
        ContentDocument contentDocument = new ContentDocument();
        contentDocument.setContent(contentVO.getContent());
        contentDocument.setImgUrls(JSONObject.toJSONString(contentVO.getImgUrls()));
        contentDocument.setLikeUserIds(JSONObject.toJSONString(contentVO.getLikeUserIds()));
        contentDocument.setUserId(contentVO.getUserId());
        return contentDocument;
    }

    /**
     * 特定对象转换 {@link List<ContentVO>} to {@link List<ContentDocument>}
     *
     * @param contentVOs 计划输入实体集合
     * @return {@link List<ContentDocument>}
     */
    public List<ContentDocument> contentVOConvertsContentDocument(List<ContentVO> contentVOs) {
        List<ContentDocument> contentDTOS = new LinkedList<>();
        for (ContentVO contentVO : contentVOs) {
            contentDTOS.add(this.contentVOConvertContentDocument(contentVO));
        }
        return contentDTOS;
    }
    
}
