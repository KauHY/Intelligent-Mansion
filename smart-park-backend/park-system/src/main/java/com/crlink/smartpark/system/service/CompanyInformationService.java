package com.crlink.smartpark.system.service;

import com.crlink.smartpark.system.entity.CompanyInformation;
import com.crlink.smartpark.system.model.query.CompanyInformationQuery;
import com.crlink.smartpark.system.model.CompanyInformationReqDTO;
import com.crlink.smartpark.system.dto.CompanyInformationDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 企业信息 服务类
 * </p>
 * */
public interface CompanyInformationService extends IService<CompanyInformation> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<CompanyInformation, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateCompanyInformation(CompanyInformationReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    CompanyInformationDTO getCompanyInformationById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<CompanyInformationDTO> listCompanyInformationPage(CompanyInformationQuery query);

    Set<String> listCodeByUserId();
}
