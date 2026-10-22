package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.controller.request.form.FormSubmitReqDTO;
import com.crlink.smartpark.customer.dto.FormEditorsDTO;
import com.crlink.smartpark.customer.dto.FormSubmissionDTO;
import com.crlink.smartpark.customer.entity.FormSubmission;
import com.crlink.smartpark.customer.model.query.FormSubmissionQuery;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 表单提交内容 服务类
 * </p>
 * */
public interface FormSubmissionService extends IService<FormSubmission> {


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    FormSubmissionDTO getFormSubmissionById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<FormSubmissionDTO> listFormSubmissionPage(FormSubmissionQuery query);

    Boolean submitForm(FormSubmitReqDTO reqDTO);

    FormEditorsDTO getSubmitDetail(String formId, String employeeId);

}
