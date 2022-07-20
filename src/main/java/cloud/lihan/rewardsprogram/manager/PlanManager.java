package cloud.lihan.rewardsprogram.manager;

import cloud.lihan.rewardsprogram.dto.PlanDTO;
import cloud.lihan.rewardsprogram.entety.document.PlanDocument;
import cloud.lihan.rewardsprogram.vo.PlanVO;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 计划相关的实体对象之间的字段属性赋值
 *
 * @author hanyun.li
 * @createTime 2022/07/20 11:13:00
 */
@Component("planManager")
public class PlanManager {

    /**
     * 特定对象转换 {@link PlanDocument} to {@link PlanDTO}
     *
     * @param planDocument 计划文档实体
     * @return {@link PlanDTO}
     */
    public PlanDTO planDocumentConvertPlanDTO(PlanDocument planDocument) {
        if (Objects.isNull(planDocument)) {
            return null;
        }
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(planDocument.getId());
        planDTO.setPlanInfo(planDocument.getPlanInfo());
        planDTO.setIsFinished(planDocument.getIsFinished());
        planDTO.setUserId(planDocument.getUserId());
        planDTO.setCreateTime(planDocument.getCreateTime());
        planDTO.setUpdateTime(planDocument.getUpdateTime());
        return planDTO;
    }

    /**
     * 特定集合转换 {@link List <PlanDocument>} to {@link List<PlanDTO>}
     *
     * @param planDocuments 计划文档实体集合
     * @return {@link List<PlanDTO>}
     */
    public List<PlanDTO> planDocumentsConvertPlanDTO(List<PlanDocument> planDocuments) {
        List<PlanDTO> planDTOS = new LinkedList<>();
        for (PlanDocument planDocument : planDocuments) {
            planDTOS.add(this.planDocumentConvertPlanDTO(planDocument));
        }
        return planDTOS;
    }

    /**
     * 特定对象转换 {@link PlanVO} to {@link PlanDocument}
     *
     * @param PlanVO 计划输入实体
     * @return {@link PlanDocument}
     */
    public PlanDocument planVOConvertPlanDocument(PlanVO PlanVO) {
        if (Objects.isNull(PlanVO)) {
            return null;
        }
        PlanDocument PlanDocument = new PlanDocument();
        PlanDocument.setPlanInfo(PlanVO.getPlanInfo());
        PlanDocument.setUserId(PlanVO.getUserId());
        return PlanDocument;
    }

    /**
     * 特定对象转换 {@link List<PlanVO>} to {@link List<PlanDocument>}
     *
     * @param planVOs 计划输入实体集合
     * @return {@link List<PlanDocument>}
     */
    public List<PlanDocument> planVOConvertsPlanDocument(List<PlanVO> planVOs) {
        List<PlanDocument> planDTOS = new LinkedList<>();
        for (PlanVO planVO : planVOs) {
            planDTOS.add(this.planVOConvertPlanDocument(planVO));
        }
        return planDTOS;
    }

}
