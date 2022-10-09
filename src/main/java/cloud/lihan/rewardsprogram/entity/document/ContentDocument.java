package cloud.lihan.rewardsprogram.entity.document;

import cloud.lihan.rewardsprogram.common.documents.BaseDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子信息文档
 *
 * @author hanyun.li
 * @createTime 2022/09/28 17:32:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContentDocument extends BaseDocument {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 图片url集合(注：数据结构为Map转的json字符串)
     */
    private String imgUrls;

    /**
     * 给这个帖子点赞的用户ID集合(注：数据结构为List转的json字符串)
     */
    private String likeUserIds;

}
