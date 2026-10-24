package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.controller.request.form.FormSubmitReqDTO;
import com.crlink.smartpark.customer.dto.EditorDTO;
import com.crlink.smartpark.customer.dto.FormEditorsDTO;
import com.crlink.smartpark.customer.dto.FormSubmissionDTO;
import com.crlink.smartpark.customer.entity.Editor;
import com.crlink.smartpark.customer.entity.Form;
import com.crlink.smartpark.customer.entity.FormSubmission;
import com.crlink.smartpark.customer.entity.FormTemplate;
import com.crlink.smartpark.customer.mapper.FormSubmissionMapper;
import com.crlink.smartpark.customer.model.query.FormSubmissionQuery;
import com.crlink.smartpark.customer.service.EditorService;
import com.crlink.smartpark.customer.service.FormService;
import com.crlink.smartpark.customer.service.FormSubmissionService;
import com.crlink.smartpark.customer.service.FormTemplateService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * 表单提交内容 服务实现类
 * */
@Service
public class FormSubmissionServiceImpl extends ServiceImpl<FormSubmissionMapper, FormSubmission> implements FormSubmissionService {


    @Autowired
    private FormService formService;

    @Autowired
    private FormTemplateService formTemplateService;

    @Autowired
    private EditorService editorService;


    /**
     * 通过id查询实体
     */
    @Override
    public FormSubmissionDTO getFormSubmissionById(String id) {
        FormSubmission entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<FormSubmissionDTO> listFormSubmissionPage(FormSubmissionQuery query) {
        query.startPage();
        List<FormSubmission> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<FormSubmissionDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean submitForm(FormSubmitReqDTO reqDTO) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        if (StringUtils.isBlank(employeeId)) {
            throw new ResolinkRuntimeException("800003");
        }
        FormSubmission one = this.lambdaQuery().select(FormSubmission::getId).eq(FormSubmission::getFormId, reqDTO.getFormId())
                .eq(FormSubmission::getEmployeeId, employeeId).one();
        if (Objects.nonNull(one)) {
            throw new ResolinkRuntimeException("800002");
        }
        FormSubmission formSubmission = MyBeanUtils.copy(reqDTO, FormSubmission::new);
        String submission = reqDTO.getSubmission();
//        JSONObject jsonObject = JSONUtil.parseObj(submission);
        formSubmission.setSubmission(submission);
        formSubmission.setEmployeeId(employeeId);
        EntityUserUtil.setAll(formSubmission);
        return this.save(formSubmission);
    }

    @Override
    @Id2objHandle
    public FormEditorsDTO getSubmitDetail(String formId, String employeeId) {
        FormSubmission one = this.lambdaQuery().eq(FormSubmission::getFormId, formId).eq(FormSubmission::getEmployeeId, employeeId).one();
        Form form = formService.getById(formId);
        String templateId = form.getTemplateId();
        List<String> editorIds = formTemplateService.id2ValueMap(Sets.newHashSet(templateId), FormTemplate::getEditorIds).get(templateId);
        FormEditorsDTO dto = MyBeanUtils.copy(one, FormEditorsDTO::new);
        dto = Optional.ofNullable(dto).orElseGet(FormEditorsDTO::new);
        dto.setId(formId);
        dto.setTitle(form.getTitle());
        dto.setCycle(form.getCycle());
        dto.setCreateTime(formService.getDeadlineTime(form));
        if (Objects.nonNull(one)) {
            dto.setSubmissionId(one.getId());
            dto.setEditorResults(JSONUtil.parseObj(one.getSubmission()));
            dto.setEmployeeId(one.getEmployeeId());
            dto.setCreateTime(one.getCreateTime());
        }
        if (ObjectUtil.isNotEmpty(editorIds)) {
            List<Editor> editors = editorService.listByIds(editorIds);
            dto.setEditors(MyBeanUtils.copyList(editors, EditorDTO::new));
        }
        return dto;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<FormSubmission> getConditionWrapper(FormSubmissionQuery query) {
        LambdaQueryWrapper<FormSubmission> queryWrapper = new LambdaQueryWrapper<FormSubmission>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<FormSubmissionDTO> entitiesToDTOs(List<FormSubmission> entities) {
        return MyBeanUtils.copyList(entities, FormSubmissionDTO::new, (s, t) -> {

        });
    }
}
