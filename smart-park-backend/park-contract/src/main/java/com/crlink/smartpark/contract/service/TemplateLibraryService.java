package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.entity.TemplateLibrary;
import com.crlink.smartpark.contract.model.query.TemplateLibraryQuery;
import com.crlink.smartpark.contract.model.TemplateLibraryReqDTO;
import com.crlink.smartpark.contract.dto.TemplateLibraryDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 模板字段表 服务类
 * </p>
 * */
public interface TemplateLibraryService extends IService<TemplateLibrary> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<TemplateLibrary, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateTemplateLibrary(TemplateLibraryReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    TemplateLibraryDTO getTemplateLibraryById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<TemplateLibraryDTO> listTemplateLibraryPage(TemplateLibraryQuery query);

    /**
     * 模板字典返回
     * @param type:
     * @return  java.util.Map<java.lang.String,java.lang.Object>
     * @MethodName findByType
     */
    Map<String, Object> findByType(String type);

    /**
     * 根据分组查询对应映射字段
     * @param type:
     * @return  java.util.List<com.crlink.smartpark.contract.dto.TemplateLibraryDTO>
     * @MethodName listTemplateLibraryByType
     */
    List<TemplateLibraryDTO> listTemplateLibraryByType(String type);
}
