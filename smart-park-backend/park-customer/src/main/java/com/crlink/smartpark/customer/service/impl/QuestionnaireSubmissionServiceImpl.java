package com.crlink.smartpark.customer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.controller.request.QuestionnaireSubmitReqDTO;
import com.crlink.smartpark.customer.dto.QuestionnaireSubmissionDTO;
import com.crlink.smartpark.customer.entity.QuestionnaireSubmission;
import com.crlink.smartpark.customer.mapper.QuestionnaireSubmissionMapper;
import com.crlink.smartpark.customer.model.query.QuestionnaireSubmissionQuery;
import com.crlink.smartpark.customer.service.QuestionnaireSubmissionService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 问卷提交内容 服务实现类
 * */
@Service
public class QuestionnaireSubmissionServiceImpl extends ServiceImpl<QuestionnaireSubmissionMapper, QuestionnaireSubmission> implements QuestionnaireSubmissionService {

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<QuestionnaireSubmission, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(QuestionnaireSubmission::getId, sValue)
                .in(QuestionnaireSubmission::getId, ids).list().parallelStream()
                .collect(Collectors.toMap(QuestionnaireSubmission::getId, sValue));
        return result;
    }


    @Override
    public Boolean submitQuestionnaire(QuestionnaireSubmitReqDTO dto) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        if (StringUtils.isBlank(employeeId)) {
            throw new ResolinkRuntimeException("800003");
        }
        QuestionnaireSubmission one = this.lambdaQuery().select(QuestionnaireSubmission::getId)
                .eq(QuestionnaireSubmission::getQuestionnaireId, dto.getQuestionnaireId())
                .eq(QuestionnaireSubmission::getEmployeeId, employeeId).one();
        if (Objects.nonNull(one)) {
            throw new ResolinkRuntimeException("800002");
        }
        QuestionnaireSubmission questionnaireSubmission = new QuestionnaireSubmission();
        String submission = dto.getSubmission();
//        JSONObject jsonObject = JSONUtil.parseObj(submission);
        questionnaireSubmission.setQuestionnaireId(dto.getQuestionnaireId());
        questionnaireSubmission.setSubmission(submission);
        questionnaireSubmission.setEmployeeId(employeeId);
        EntityUserUtil.setAll(questionnaireSubmission);
        return this.save(questionnaireSubmission);
    }

    @Override
    public Map<String, Object> getQuestionnaireSubmitResult(String questionnaireId) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        if (StringUtils.isBlank(employeeId)) {
            return Collections.emptyMap();
        }
        QuestionnaireSubmission questionnaireSubmission = this.lambdaQuery().eq(QuestionnaireSubmission::getQuestionnaireId, questionnaireId)
                .eq(QuestionnaireSubmission::getEmployeeId, employeeId).one();
        Map<String, Object> submission = JSONUtil.parseObj(questionnaireSubmission.getSubmission());
        return submission;
    }

    @Override
    public Integer countSubmission(String questionnaireId) {
        Integer count = this.lambdaQuery().eq(QuestionnaireSubmission::getQuestionnaireId, questionnaireId).count();
        return count;
    }


    /**
     * 通过id查询实体
     */
    @Override
    public QuestionnaireSubmissionDTO getQuestionnaireSubmissionById(String id) {
        QuestionnaireSubmission entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<QuestionnaireSubmissionDTO> listQuestionnaireSubmissionPage(QuestionnaireSubmissionQuery query) {
        query.startPage();
        List<QuestionnaireSubmission> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<QuestionnaireSubmissionDTO> result = null;
        return query.createPageInfo(result);
    }

    private List<QuestionnaireSubmissionDTO> entitiesToDTOs(List<QuestionnaireSubmission> entities) {
        return MyBeanUtils.copyList(entities, QuestionnaireSubmissionDTO::new, (s, t) -> {

        });
    }

}
