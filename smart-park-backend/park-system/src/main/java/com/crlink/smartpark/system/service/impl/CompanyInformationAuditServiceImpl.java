package com.crlink.smartpark.system.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.company.CompanySaveDTO;
import com.crlink.smartpark.system.enums.Constant;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.CompanyInformation;
import com.crlink.smartpark.system.entity.CompanyInformationAudit;
import com.crlink.smartpark.system.model.query.CompanyInfoAuditDTO;
import com.crlink.smartpark.system.model.query.CompanyInformationAuditQuery;
import com.crlink.smartpark.system.model.CompanyInformationAuditReqDTO;
import com.crlink.smartpark.system.dto.CompanyInformationAuditDTO;
import com.crlink.smartpark.system.service.CompanyInformationAuditService;
import com.crlink.smartpark.system.mapper.CompanyInformationAuditMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.system.service.CompanyInformationService;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.collection.CollectionUtil;

import java.time.LocalDateTime;
import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 企业信息审核 服务实现类
 * */
@Service
public class CompanyInformationAuditServiceImpl extends ServiceImpl<CompanyInformationAuditMapper, CompanyInformationAudit> implements CompanyInformationAuditService {

    @Autowired
    private CompanyService companyService;


    @Autowired
    private CompanyInformationService companyInformationService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<CompanyInformationAudit, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<CompanyInformationAudit> lambdaQuery = this.lambdaQuery().in(CompanyInformationAudit::getId, ids);
        if (flag) {
            lambdaQuery.select(CompanyInformationAudit::getId, sValue);
        }
        Stream<CompanyInformationAudit> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(CompanyInformationAudit::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(CompanyInformationAudit::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateCompanyInformationAudit(CompanyInformationAuditReqDTO reqDTO) {
        checkSaveParam(reqDTO);
        CompanyInformationAudit entity = MyBeanUtils.copy(reqDTO, CompanyInformationAudit::new);
        entity.setBusinessUserId(SystemUtil.getInstance().getCurrentUserId());
        entity.setAuditStatus(Constant.SysCompanyAuditState.AUDIT_RENDING.state);
        entity.setHeadOrgId(SystemUtil.getInstance().getCurrentHeadOrgId());
        return this.saveOrUpdate(entity);
    }


    public void checkSaveParam(CompanyInformationAuditReqDTO reqDTO) {
        LambdaQueryWrapper<CompanyInformation> wrapper = new LambdaQueryWrapper();
        wrapper.eq(CompanyInformation::getName, reqDTO.getName());
        CompanyInformation existCompany = companyInformationService.getOne(wrapper, false);
        if (existCompany != null && !existCompany.getId().equals(reqDTO.getId())) {
            throw new ResolinkRuntimeException("30001");
        }
        wrapper.clear();
        wrapper.eq(CompanyInformation::getCode, reqDTO.getCode());
        existCompany = companyInformationService.getOne(wrapper, false);
        if (existCompany != null && !existCompany.getId().equals(reqDTO.getId())) {
            throw new ResolinkRuntimeException("30002");
        }
    }


    @Override
    public CompanyInformation getDetail() {
        return companyInformationService.lambdaQuery().eq(CompanyInformation::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId()).one();
    }

    @Override
    public CompanyInformationAudit getLastAudit() {
        List<CompanyInformationAudit> informationAuditList = this.lambdaQuery().eq(CompanyInformationAudit::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId())
                .orderByDesc(CompanyInformationAudit::getCreateTime).list();
        if (CollectionUtil.isNotEmpty(informationAuditList)) {
            return informationAuditList.get(0);
        }
        return null;
    }

    /**
     * 通过id查询实体
     */
    @Override
    public CompanyInformationAuditDTO getCompanyInformationAuditById(String id) {
        CompanyInformationAudit entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    @Override
    public Boolean cancel(String id) {
        CompanyInformationAudit entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        entity.setAuditStatus(Constant.SysCompanyAuditState.CANCEL.state);
        entity.setCancelTime(LocalDateTime.now());
        entity.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean audit(CompanyInfoAuditDTO auditDTO) {
        CompanyInformationAudit entity = Optional.ofNullable(getById(auditDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        entity.setAuditStatus(auditDTO.getAuditStatus());
        entity.setAuditMsg(auditDTO.getAuditMsg());
        entity.setAuditUserId(SystemUtil.getInstance().getCurrentUserId());
        entity.setAuditTime(LocalDateTime.now());
        entity.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        //企业端更新企业信息 则更新关联的入驻企业基础信息
        if (Constant.SysCompanyAuditState.AUDIT_PASS.state == auditDTO.getAuditStatus()) {
            List<Company> companyList = companyService.lambdaQuery().eq(Company::getCode, entity.getCode()).list();
            if (CollectionUtil.isNotEmpty(companyList)) {
                companyList.forEach(company -> {
                    company.setName(entity.getName());
                    company.setLicenses(entity.getLicenses());
                    company.setCompanyType(entity.getCompanyType());
                    company.setIndustryField(entity.getIndustryField());
                    company.setCompanyTaxesType(entity.getCompanyTaxesType());
                    company.setEnterpriseSize(entity.getEnterpriseSize());
                    company.setCorporateTax(entity.getCorporateTax());
                    company.setRemark(entity.getRemark());
                    company.setAtts(entity.getAtts());
                    company.setAddress(entity.getAddress());
                });
                companyService.updateBatchById(companyList);
            }
            //更新企业信息表
            CompanyInformation companyInformation = companyInformationService.lambdaQuery().eq(CompanyInformation::getBusinessUserId, entity.getBusinessUserId()).one();
            if (companyInformation == null) {
                companyInformation = new CompanyInformation();
            }
            companyInformation.setBusinessUserId(entity.getBusinessUserId());
            companyInformation.setHeadOrgId(entity.getHeadOrgId());
            companyInformation.setCode(entity.getCode());
            companyInformation.setIndustryField(entity.getIndustryField());
            companyInformation.setCompanyTaxesType(entity.getCompanyTaxesType());
            companyInformation.setEnterpriseSize(entity.getEnterpriseSize());
            companyInformation.setRemark(entity.getRemark());
            companyInformation.setAtts(entity.getAtts());
            companyInformation.setName(entity.getName());
            companyInformation.setLicenses(entity.getLicenses());
            companyInformation.setCompanyType(entity.getCompanyType());
            companyInformation.setCorporateTax(entity.getCorporateTax());
            companyInformation.setAddress(entity.getAddress());
            companyInformationService.saveOrUpdate(companyInformation);
        }
        return this.updateById(entity);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<CompanyInformationAuditDTO> listCompanyInformationAuditPage(CompanyInformationAuditQuery query) {
        query.startPage();
        List<CompanyInformationAudit> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<CompanyInformationAuditDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<CompanyInformationAudit> getConditionWrapper(CompanyInformationAuditQuery query) {
        LambdaQueryWrapper<CompanyInformationAudit> queryWrapper = new LambdaQueryWrapper<CompanyInformationAudit>();
        queryWrapper.eq(CompanyInformationAudit::getHeadOrgId, SystemUtil.getInstance().getCurrentHeadOrgId());
        queryWrapper.like(StringUtils.isNotBlank(query.getName()), CompanyInformationAudit::getName, query.getName());
        queryWrapper.eq(query.getCompanyType() != null, CompanyInformationAudit::getCompanyType, query.getCompanyType());
        queryWrapper.eq(query.getAuditStatus() != null, CompanyInformationAudit::getAuditStatus, query.getAuditStatus())
                .orderByDesc(CompanyInformationAudit::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<CompanyInformationAuditDTO> entitiesToDTOs(List<CompanyInformationAudit> entities) {
        return MyBeanUtils.copyList(entities, CompanyInformationAuditDTO::new, (s, t) -> {

        });
    }
}
