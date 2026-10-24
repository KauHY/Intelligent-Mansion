package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.dto.FormDTO;
import com.crlink.smartpark.customer.dto.FormEditorsDTO;
import com.crlink.smartpark.customer.entity.Form;
import com.crlink.smartpark.customer.entity.FormSubmission;
import com.crlink.smartpark.customer.entity.FormTemplate;
import com.crlink.smartpark.customer.mapper.FormMapper;
import com.crlink.smartpark.customer.model.FormReqDTO;
import com.crlink.smartpark.customer.model.dto.FormUpdateStatusReqDTO;
import com.crlink.smartpark.customer.model.query.FormQuery;
import com.crlink.smartpark.customer.service.FormService;
import com.crlink.smartpark.customer.service.FormSubmissionService;
import com.crlink.smartpark.customer.service.FormTemplateService;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.customer.common.FormConstant.FormListStatusEnum.*;
import static com.crlink.smartpark.customer.common.QuestionnaireConstant.FormStatusEnum.COLLECTING;
import static com.crlink.smartpark.customer.common.QuestionnaireConstant.FormStatusEnum.END;


/**
 * 信息统计表单 服务实现类
 * */
@Service
public class FormServiceImpl extends ServiceImpl<FormMapper, Form> implements FormService {

    @Autowired
    private FormSubmissionService formSubmissionService;
    @Autowired
    private FormTemplateService formTemplateService;


    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Form, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(Form::getId, sValue)
                .in(Form::getId, ids).list().parallelStream()
                .collect(Collectors.toMap(Form::getId, sValue));
        return result;
    }

    @Override
    public List<String> getEditorIdsById(String id) {
        Map<String, String> map = this.id2ValueMap(Sets.newHashSet(id), Form::getTemplateId);
        String templateId = map.get(id);
        List<String> editorIds = formTemplateService.id2ValueMap(Sets.newHashSet(templateId), FormTemplate::getEditorIds).get(templateId);
        return editorIds;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateForm(FormReqDTO reqDTO) {
        Form entity = MyBeanUtils.copy(reqDTO, Form::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public FormDTO getFormById(String id) {
        Form entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<FormDTO> listFormPage(FormQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.startPage();
        List<Form> list = this.list(getConditionWrapper(query, parkOrgIds));
        MyPageInfoUtils.isEmptyPage(list);
        List<FormDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean updateStatus(FormUpdateStatusReqDTO reqDTO) {
        Form one = this.getById(reqDTO.getId());
        Integer originStatus = one.getStatus();
        Integer cycle = one.getCycle();
        if (reqDTO.getStatus() == COLLECTING.status && isOverdue(one)) {
            throw new ResolinkRuntimeException("50100");
        } else if (reqDTO.getStatus() == END.status && originStatus != COLLECTING.status) {
            throw new ResolinkRuntimeException("10023");
        }
        one.setStatus(reqDTO.getStatus());
        EntityUserUtil.setUpdateBy(one);
        return this.updateById(one);
    }

    @Override
    @Id2objHandle
    public PageInfo<FormEditorsDTO> wxFormList(Integer status, BaseQuery<String> query) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        EmployeeInfoVO employeeInfo = SystemUtil.getInstance().getEmployeeInfo(employeeId);
        String companyId = employeeInfo.getCompanyId();
        String parkOrgId = employeeInfo.getParkOrgId();
        List<FormEditorsDTO> result = null;
        if (status == FILL_FORM.status) {
            result = getFillForms(parkOrgId, companyId, employeeId, query);
        } else if (status == ME_SUBMITTED.status) {
            query.startPage();
            result = this.getBaseMapper().querySubmittedForms(employeeId);
        } else if (status == ME_RECEIVED.status) {
            query.startPage();
            result = this.getBaseMapper().queryReceivedForms(employeeId, query.getParam());
        }
        return query.createPageInfo(result);
    }

    private List<FormEditorsDTO> getFillForms(String parkOrgId, String companyId, String employeeId, BaseQuery<String> query) {
        Set<String> submittedFormIds = formSubmissionService.lambdaQuery().select(FormSubmission::getFormId)
                .eq(FormSubmission::getEmployeeId, employeeId).list()
                .parallelStream().map(FormSubmission::getFormId)
                .collect(Collectors.toSet());
        LambdaQueryWrapper<Form> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Form::getId, Form::getTitle, Form::getDeadlineTime, Form::getDeadlineNum, Form::getCycle, Form::getStatus);
        wrapper.eq(Form::getParkOrgId, parkOrgId);
        wrapper.in(Form::getStatus, COLLECTING.status, END.status);
        wrapper.notIn(CollUtil.isNotEmpty(submittedFormIds), Form::getId, submittedFormIds);
        String sql = new StringBuilder(" ( json_contains(company_ids,").append("'\"").append(companyId).append("\"'").append(")").append(" or ")
                .append("json_contains(employee_ids,").append("'\"").append(employeeId).append("\"'").append(")")
                .append(")")
                .toString();
        wrapper.apply(sql);
        wrapper.orderByDesc(Form::getCreateTime);
        query.startPage();
        List<Form> list = this.list(wrapper);
        List<FormEditorsDTO> result = list.parallelStream().map(t -> {
            FormEditorsDTO dto = new FormEditorsDTO();
            dto.setId(t.getId());
            dto.setTitle(t.getTitle());
            dto.setCreateTime(getDeadlineTime(t));
            dto.setStatus(getFormStatus(t));
            dto.setCycle(t.getCycle());
            return dto;
        }).collect(Collectors.toList());
        return result;
    }

    private Integer getFormStatus(Form form) {
        Integer status = form.getStatus();
        if (status == COLLECTING.status && isOverdue(form)) {
            return END.status;
        }
        return status;
    }

    //private LocalDateTime getDeadlineTime(Form form) {
    //    Integer cycle = form.getCycle();
    //    String deadlineTime = form.getDeadlineTime();
    //    Integer deadlineNum = form.getDeadlineNum();
    //    LocalDateTime now = LocalDateTime.now();
    //    if (cycle == EVERY_DAY.type) {
    //        LocalTime deadline = LocalTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
    //        return LocalDateTime.of(LocalDate.now(), deadline);
    //    } else if (cycle == EVERY_WEEK.type) {
    //        LocalTime deadline = LocalTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
    //        int difference = deadlineNum - now.getDayOfWeek().getValue();
    //        return now.plusDays(difference);
    //    } else if (cycle == EVERY_MONTH.type) {
    //        LocalTime deadline = LocalTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
    //        LocalDate localDate = LocalDate.of(now.getYear(), now.getMonth(), deadlineNum);
    //        LocalDateTime dateTime = LocalDateTime.of(localDate, deadline);
    //        return dateTime;
    //    } else {
    //        LocalDateTime dateTime = LocalDateTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    //        return dateTime;
    //    }
    //}

    //是否超期
    private boolean isOverdue(Form form) {
        return getDeadlineTime(form).isBefore(LocalDateTime.now());
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<Form> getConditionWrapper(FormQuery query, Set<String> parkOrgIds) {
        LambdaQueryWrapper<Form> queryWrapper = new LambdaQueryWrapper<Form>();
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), Form::getParkOrgId, query.getParkOrgId());
        queryWrapper.like(StringUtils.isNotBlank(query.getTitle()), Form::getTitle, query.getTitle());
        queryWrapper.in(StringUtils.isNotBlank(query.getParkOrgId()), Form::getParkOrgId, parkOrgIds);
        queryWrapper.orderByDesc(Form::getUpdateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<FormDTO> entitiesToDTOs(List<Form> entities) {
        return MyBeanUtils.copyList(entities, FormDTO::new, (s, t) -> {
            t.setStatus(getFormStatus(s));
        });
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().getDayOfWeek().getValue());
        System.out.println(LocalDateTime.now().getDayOfMonth());
        System.out.println(LocalDateTime.now().plusDays(-3));
    }
}
