package cloud.lihan.rewardsprogram.service.impl;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.dao.inner.ContentDao;
import cloud.lihan.rewardsprogram.dto.ContentDTO;
import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.entity.document.ContentDocument;
import cloud.lihan.rewardsprogram.manager.ContentManager;
import cloud.lihan.rewardsprogram.service.inner.ContentService;
import cloud.lihan.rewardsprogram.service.inner.UserService;
import cloud.lihan.rewardsprogram.vo.ContentVO;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * 帖子相关的业务细节
 *
 * @author hanyun.li
 * @createTime 2022/09/29 17:17:00
 */
@Slf4j
@Service("contentService")
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;
    @Autowired
    private ContentManager contentManager;
    @Autowired
    private UserService userService;

    @Override
    public void releaseContent(ContentVO contentVO) throws Exception {
        contentDao.createContentDocument(contentManager.contentVOConvertContentDocument(contentVO));
    }

    @Override
    public void deleteContentById(String contentId) throws Exception {
        contentDao.deleteContentDocumentById(contentId);
    }

    @Override
    public void editContent(ContentVO contentVO) throws Exception {
        Query query = new Query.Builder()
                .ids(id -> id.values(contentVO.getContentId()))
                .build();
        Map<String, JsonData> optionsMaps = new HashMap<>(IntegerConstant.ONE);
        optionsMaps.put("content", JsonData.of(contentVO.getContent()));
        String source = "ctx._source.content = params.content";
        contentDao.updateContentField(optionsMaps, source, query);
    }

    @Override
    public void likeContent(ContentVO contentVO) throws Exception {
        ContentDocument contentDocument = contentDao.getContentById(contentVO.getContentId());
        if (Objects.isNull(contentDocument)) {
            log.error("ContentServiceImpl.likeContent() exit error! contentId = {} not exit!", contentVO.getContentId());
            return;
        }

        List<String> likeUserIdList = JSONObject.parseArray(contentDocument.getLikeUserIds(), String.class);
        if (Objects.isNull(likeUserIdList)) {
            likeUserIdList = new ArrayList<>();
        }

        // 一个用户不能多次点赞同一个帖子
        if (likeUserIdList.contains(contentVO.getUserId())) {
            return;
        }
        likeUserIdList.add(contentVO.getUserId());
        contentVO.setLikeUserIds(likeUserIdList);
        this.updateLikeUserIds(contentVO);
    }

    @Override
    public void cancelLikeContent(ContentVO contentVO) throws Exception {
        ContentDocument contentDocument = contentDao.getContentById(contentVO.getContentId());
        if (Objects.isNull(contentDocument)) {
            log.error("ContentServiceImpl.cancelLikeContent() exit error! contentId = {} not exit!", contentVO.getContentId());
            return;
        }

        List<String> likeUserIdList = JSONObject.parseArray(contentDocument.getLikeUserIds(), String.class);
        if (CollectionUtils.isEmpty(likeUserIdList)) {
            return;
        }

        likeUserIdList.remove(contentVO.getUserId());
        contentVO.setLikeUserIds(likeUserIdList);
        this.updateLikeUserIds(contentVO);
    }

    @Override
    public List<ContentDTO> getAllContents(String userId) throws Exception {
        List<ContentDTO> contentDTOS = contentManager.contentDocumentsConvertContentDTO(contentDao.getMultipleContentByQuery());
        contentDTOS.parallelStream().forEach(contentDTO -> {
            try {
                UserDTO user = userService.getUserByUserId(contentDTO.getUserId());
                // 设置发帖人显示名称
                contentDTO.setNikeNameOrUserName(StringUtils.isEmpty(user.getNickName()) ? user.getUserName() : user.getNickName());

                List<String> likeUserIdList = JSONObject.parseArray(contentDTO.getLikeUserIds(), String.class);

                // 拼接点赞名单信息
                this.splicingLikeContentInfo(contentDTO, likeUserIdList);
                // 按钮展示控制
                this.viewButtonControl(contentDTO, userId, likeUserIdList);
            } catch (IOException e) {
                log.error("ContentServiceImpl.getAllContents() exit error! error info : {}", e.getMessage());
            }
        });
        return contentDTOS;
    }

    @Override
    public ContentDTO getSingleContent(String contentId) throws Exception {
        return contentManager.contentDocumentConvertContentDTO(contentDao.getContentById(contentId));
    }

    /**
     * 更新点赞人数
     *
     * @param contentVO 帖子信息
     * @throws Exception 异常信息
     */
    private void updateLikeUserIds(ContentVO contentVO) throws Exception {
        Query query = new Query.Builder()
                .ids(id -> id.values(contentVO.getContentId()))
                .build();
        Map<String, JsonData> optionsMaps = new HashMap<>(IntegerConstant.ONE);
        optionsMaps.put("likeUserIds", JsonData.of(JSONObject.toJSONString(contentVO.getLikeUserIds())));
        String source = "ctx._source.likeUserIds = params.likeUserIds";
        contentDao.updateContentField(optionsMaps, source, query);
    }

    /**
     * 拼接点赞名单信息
     *
     * @param contentDTO 帖子信息
     * @param likeUserIdList 点赞用户ID集合
     * @throws IOException 异常信息
     */
    private void splicingLikeContentInfo(ContentDTO contentDTO, List<String> likeUserIdList) throws IOException {
        if (CollectionUtils.isEmpty(likeUserIdList)) {
            return;
        }

        StringBuilder likeContentInfo = new StringBuilder();
        int times = likeUserIdList.size();
        for (int i = 0; i < times; i++) {
            UserDTO user = userService.getUserByUserId(likeUserIdList.get(i));
            String name = StringUtils.isEmpty(user.getNickName()) ? user.getUserName() : user.getNickName();
            likeContentInfo.append(name);
            if (i < times - 1) {
                likeContentInfo.append("，");
            }
        }
        likeContentInfo.append("等人点赞了这个帖子");
        contentDTO.setLikeContentInfo(likeContentInfo.toString());
    }

    /**
     * 按钮展示控制(判断是否是自己发送的帖子，对应展示响应权限按钮)
     *
     * @param contentDTO 输出的帖子信息
     * @param currentLoginUserId 当前登陆的用户ID
     */
    private void viewButtonControl(ContentDTO contentDTO, String currentLoginUserId, List<String> likeUserIdList) {
        // 如果是自己的帖子，则显示编辑按钮
        if (currentLoginUserId.equals(contentDTO.getUserId())) {
            contentDTO.setIsViewEditAndDeleteButton(Boolean.TRUE);
        } else {
            contentDTO.setIsViewEditAndDeleteButton(Boolean.FALSE);
        }

        if (CollectionUtils.isEmpty(likeUserIdList)) {
            contentDTO.setIsViewLikeButton(Boolean.TRUE);
            contentDTO.setIsViewCancelLikeButton(Boolean.FALSE);
        } else {

            // 判断该帖子的点赞用户ID中是否存在当前登陆用户的ID
            if (likeUserIdList.contains(currentLoginUserId)) {
                contentDTO.setIsViewLikeButton(Boolean.FALSE);
                contentDTO.setIsViewCancelLikeButton(Boolean.TRUE);
            } else {
                contentDTO.setIsViewLikeButton(Boolean.TRUE);
                contentDTO.setIsViewCancelLikeButton(Boolean.FALSE);
            }
        }
    }

}
