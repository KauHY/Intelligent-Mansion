package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.controller.request.QuestionnaireSubmitReqDTO;
import com.crlink.smartpark.customer.dto.QuestionnaireSubmissionDTO;
import com.crlink.smartpark.customer.entity.QuestionnaireSubmission;
import com.crlink.smartpark.customer.model.query.QuestionnaireSubmissionQuery;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 问卷提交内容 服务类
 * </p>
 * */
public interface QuestionnaireSubmissionService extends IService<QuestionnaireSubmission> {

    /**
     * 根据Id查询值
     */

    Integer countSubmission(String questionnaireId);


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    QuestionnaireSubmissionDTO getQuestionnaireSubmissionById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<QuestionnaireSubmissionDTO> listQuestionnaireSubmissionPage(QuestionnaireSubmissionQuery query);

    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<QuestionnaireSubmission, T> sValue);

    Boolean submitQuestionnaire(QuestionnaireSubmitReqDTO dto);

    Map<String, Object> getQuestionnaireSubmitResult(String questionnaireId);
}
