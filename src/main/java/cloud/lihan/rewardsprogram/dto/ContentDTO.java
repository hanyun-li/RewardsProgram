package cloud.lihan.rewardsprogram.dto;

import cloud.lihan.rewardsprogram.entity.document.ContentDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子实体数据传输（输出用）
 *
 * @author hanyun.li
 * @createTime 2022/09/29 17:11:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentDTO extends ContentDocument {

    /**
     * 是否展示编辑按钮(true:展示 false:不展示)
     */
    private Boolean isViewEditAndDeleteButton;

    /**
     * 是否展示点赞按钮(true:展示 false:不展示)
     */
    private Boolean isViewLikeButton;

    /**
     * 是否展示取消点赞按钮(true:展示 false:不展示)
     */
    private Boolean isViewCancelLikeButton;

    /**
     * 当前帖子的作者名称（注：优先展示昵称，只有当昵称为空时，才展示用户名）
     */
    private String nikeNameOrUserName;

    /**
     * 点赞这个帖子的用户名单信息
     */
    private String likeContentInfo;

}
