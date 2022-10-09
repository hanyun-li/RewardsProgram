package cloud.lihan.rewardsprogram.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 帖子实体数据传输（接收用）
 *
 * @author hanyun.li
 * @createTime 2022/09/29 16:44:00
 */
@Data
public class ContentVO {

    /**
     * 帖子ID
     */
    private String contentId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 图片url集合
     */
    private Map<String, String> imgUrls;

    /**
     * 给这个帖子点赞的用户ID集合
     */
    private List<String> likeUserIds;

}
