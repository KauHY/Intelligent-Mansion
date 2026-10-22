package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.controller.request.EditorResultQuery;
import com.crlink.smartpark.customer.controller.response.QuestionnaireAnalysisReportDTO;
import com.crlink.smartpark.customer.controller.response.QuestionnaireDataVO;
import com.crlink.smartpark.customer.controller.response.editor.EditorAnalysisDetailDTO;
import com.crlink.smartpark.customer.dto.EditorsDTO;
import com.crlink.smartpark.customer.dto.QuestionnaireDTO;
import com.crlink.smartpark.customer.entity.Questionnaire;
import com.crlink.smartpark.customer.model.QuestionnaireReqDTO;
import com.crlink.smartpark.customer.model.dto.QuestionnaireUpdateStatusReqDTO;
import com.crlink.smartpark.customer.model.query.QuestionnaireQuery;
import com.crlink.smartpark.customer.model.vo.QuestionnaireVO;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 问卷 服务类
 * </p>
 * */
public interface QuestionnaireService extends IService<Questionnaire> {


    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateQuestionnaire(QuestionnaireReqDTO reqDTO);

    PageInfo<QuestionnaireDTO> listEmployeeQuestionnaires(Integer answeredStatus, BaseQuery query);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    QuestionnaireDTO getQuestionnaireById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<QuestionnaireVO> listQuestionnairePage(QuestionnaireQuery query);

    Boolean removeQuestionnaireById(String id);

    Boolean updateStatus(QuestionnaireUpdateStatusReqDTO reqDTO);

    Boolean copy(String id);

    Boolean editQuestionnaire(EditorsDTO reqDTO);

    PageInfo<QuestionnaireDataVO> statistics(BaseQuery<String> baseQuery);

    EditorsDTO getQuestionnaireSubmitDetail(String questionnaireSubmissionId);

    QuestionnaireAnalysisReportDTO getQuestionnaireAnalysisReport(String questionnaireId);

    PageInfo<EditorAnalysisDetailDTO> getQuestionnaireAnalysisDetail(EditorResultQuery query);

    void analysisDetailDownload(EditorResultQuery query);
}
