package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.entity.Template;
import com.crlink.smartpark.contract.model.query.TemplateQuery;
import com.crlink.smartpark.contract.model.TemplateReqDTO;
import com.crlink.smartpark.contract.dto.TemplateDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 合同模板管理 服务类
 * </p>
 * */
public interface TemplateService extends IService<Template> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Template, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateTemplate(TemplateReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    TemplateDTO getTemplateById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<TemplateDTO> listTemplatePage(TemplateQuery query);
}
