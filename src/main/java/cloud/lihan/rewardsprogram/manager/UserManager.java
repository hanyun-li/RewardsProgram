package cloud.lihan.rewardsprogram.manager;

import cloud.lihan.rewardsprogram.dto.UserDTO;
import cloud.lihan.rewardsprogram.entity.document.UserDocument;
import cloud.lihan.rewardsprogram.vo.UserVO;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 用户相关的实体对象之间的字段属性赋值
 *
 * @author hanyun.li
 * @createTime 2022/07/08 16:49:00
 */
@Component("userManager")
public class UserManager {

    /**
     * 特定对象转换 {@link UserDocument} to {@link UserDTO}
     *
     * @param userDocument 用户文档实体
     * @return {@link UserDTO}
     */
    public UserDTO userDocumentConvertUserDTO(UserDocument userDocument) {
        if (Objects.isNull(userDocument)) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDocument.getId());
        userDTO.setNickName(userDocument.getNickName());
        userDTO.setUserName(userDocument.getUserName());
        userDTO.setUserEmail(userDocument.getUserEmail());
        userDTO.setPassword(userDocument.getPassword());
        userDTO.setLastTimeLoginFailTime(userDocument.getLastTimeLoginFailTime());
        userDTO.setCurrentDayLoginFailTimes(userDocument.getCurrentDayLoginFailTimes());
        userDTO.setLastTimeAddPlanTime(userDocument.getLastTimeAddPlanTime());
        userDTO.setCurrentDayCreatePlanTimes(userDocument.getCurrentDayCreatePlanTimes());
        userDTO.setIncentiveValue(userDocument.getIncentiveValue());
        userDTO.setCreateTime(userDocument.getCreateTime());
        userDTO.setCreateTimestamp(userDocument.getCreateTimestamp());
        userDTO.setUpdateTime(userDocument.getUpdateTime());
        return userDTO;
    }

    /**
     * 特定集合转换 {@link List<UserDocument>} to {@link List<UserDTO>}
     *
     * @param userDocuments 用户文档实体集合
     * @return {@link List<UserDTO>}
     */
    public List<UserDTO> userDocumentsConvertUserDTO(List<UserDocument> userDocuments) {
        List<UserDTO> userDTOS = new LinkedList<>();
        for (UserDocument userDocument : userDocuments) {
            userDTOS.add(this.userDocumentConvertUserDTO(userDocument));
        }
        return userDTOS;
    }

    /**
     * 特定对象转换 {@link UserVO} to {@link UserDocument}
     *
     * @param userVO 用户输入实体
     * @return {@link UserDocument}
     */
    public UserDocument userVOConvertUserDocument(UserVO userVO) {
        if (Objects.isNull(userVO)) {
            return null;
        }
        UserDocument userDocument = new UserDocument();
        userDocument.setNickName(userVO.getNickName());
        userDocument.setUserName(userVO.getUserName());
        userDocument.setUserEmail(userVO.getUserEmail());
        userDocument.setPassword(userVO.getPassword());
        return userDocument;
    }

    /**
     * 特定对象转换 {@link List<UserVO>} to {@link List<UserDocument>}
     *
     * @param userVOs 愿望输入实体集合
     * @return {@link List<UserDocument>}
     */
    public List<UserDocument> userVOConvertsUserDocument(List<UserVO> userVOs) {
        List<UserDocument> userDTOS = new LinkedList<>();
        for (UserVO userVO : userVOs) {
            userDTOS.add(this.userVOConvertUserDocument(userVO));
        }
        return userDTOS;
    }

}
