package cloud.lihan.rewardsprogram.service.inner;

import cloud.lihan.rewardsprogram.dto.ContentDTO;
import cloud.lihan.rewardsprogram.vo.ContentVO;

import java.util.List;

/**
 * 帖子相关的业务方法
 *
 * @author hanyun.li
 * @createTime 2022/09/29 16:41:00
 */
public interface ContentService {

    /**
     * 发布帖子
     *
     * @param contentVO 帖子信息
     * @throws Exception 异常信息
     */
    void releaseContent(ContentVO contentVO) throws Exception;

    /**
     * 根据ID删除帖子
     *
     * @param contentId 帖子ID
     * @throws Exception
     */
    void deleteContentById(String contentId) throws Exception;

    /**
     * 编辑帖子
     *
     * @param contentVO 帖子信息
     * @throws Exception 异常信息
     */
    void editContent(ContentVO contentVO) throws Exception;

    /**
     * 点赞帖子
     *
     * @param contentVO 帖子信息
     * @throws Exception 异常信息
     */
    void likeContent(ContentVO contentVO) throws Exception;

    /**
     * 取消点赞帖子
     *
     * @param contentVO 帖子信息
     * @throws Exception 异常信息
     */
    void cancelLikeContent(ContentVO contentVO) throws Exception;

    /**
     * 获取所有帖子
     *
     * @param userId 用户ID(作用：用以在自己的帖子上展示编辑按钮)
     * @return {@link List<ContentDTO>} 所有已经发布的帖子
     * @throws Exception 异常信息
     */
    List<ContentDTO> getAllContents(String userId) throws Exception;

}
