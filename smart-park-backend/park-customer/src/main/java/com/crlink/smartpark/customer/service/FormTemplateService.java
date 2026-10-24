package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.dto.EditorsDTO;
import com.crlink.smartpark.customer.dto.FormTemplateDTO;
import com.crlink.smartpark.customer.entity.FormTemplate;
import com.crlink.smartpark.customer.model.FormTemplateReqDTO;
import com.crlink.smartpark.customer.model.query.FormTemplateQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单模板 服务类
 * </p>
 * */
public interface FormTemplateService extends IService<FormTemplate> {


    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<FormTemplate, T> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateFormTemplate(FormTemplateReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    FormTemplateDTO getFormTemplateById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<FormTemplateDTO> listFormTemplatePage(FormTemplateQuery query);

    Boolean editFormTemplate(EditorsDTO reqDTO);

    Boolean removeFormTemplateById(String id);

    List<DictVO> listFormTemplateDict(String parkOrgId);
}
