package com.crlink.smartpark.system.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.common.CacheUtil;
import com.crlink.smartpark.system.enums.Constant;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.model.CompanyAuditDetailVo;
import com.crlink.smartpark.system.model.query.CompanyAuditQuery;
import com.crlink.smartpark.system.model.CompanyAuditReqDTO;
import com.crlink.smartpark.system.dto.CompanyAuditDTO;
import com.crlink.smartpark.system.model.query.CompanyInfoAuditDTO;
import com.crlink.smartpark.system.service.CompanyAuditService;
import com.crlink.smartpark.system.mapper.CompanyAuditMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.system.service.CompanyInformationAuditService;
import com.crlink.smartpark.system.service.CompanyInformationService;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
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
 * 企业入驻审核 服务实现类
 * */
@Service
public class CompanyAuditServiceImpl extends ServiceImpl<CompanyAuditMapper, CompanyAudit> implements CompanyAuditService {

    @Autowired
    private CompanyInformationService companyInformationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyInformationAuditService companyInformationAuditService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<CompanyAudit, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<CompanyAudit> lambdaQuery = this.lambdaQuery().in(CompanyAudit::getId, ids);
        if (flag) {
            lambdaQuery.select(CompanyAudit::getId, sValue);
        }
        Stream<CompanyAudit> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(CompanyAudit::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(CompanyAudit::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateCompanyAudit(CompanyAuditReqDTO reqDTO) {
        CompanyInformation companyInformation = companyInformationService.lambdaQuery().eq(CompanyInformation::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId()).one();
        if (companyInformation == null) {
            throw new ResolinkRuntimeException("700002");
        }
        int countCompany = companyService.lambdaQuery().eq(Company::getParkOrgId, reqDTO.getParkOrgId()).eq(Company::getCode, companyInformation.getCode()).count();
        int countCompanyAudit = this.lambdaQuery().eq(CompanyAudit::getParkOrgId, reqDTO.getParkOrgId()).eq(CompanyAudit::getCompanyInformationId, companyInformation.getId())
                .eq(CompanyAudit::getAuditStatus, Constant.SysCompanyAuditState.AUDIT_RENDING.state).count();
        if (countCompany > 0 || countCompanyAudit > 0) {
            throw new ResolinkRuntimeException("700003");
        }
        CompanyAudit entity = MyBeanUtils.copy(reqDTO, CompanyAudit::new);
        entity.setBusinessUserId(SystemUtil.getInstance().getCurrentUserId());
        entity.setCompanyInformationId(companyInformation.getId());
        entity.setApplyTime(LocalDateTime.now());
        entity.setAuditStatus(Constant.SysCompanyAuditState.AUDIT_RENDING.state);
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean cancel(String id) {
        CompanyAudit entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        entity.setAuditStatus(Constant.SysCompanyAuditState.CANCEL.state);
        entity.setCancelTime(LocalDateTime.now());
        entity.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        return this.saveOrUpdate(entity);
    }

    @Override
    public List<Company> settled() {
        CompanyInformation companyInformation = companyInformationService.lambdaQuery().eq(CompanyInformation::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId()).one();
        int count = companyInformationAuditService.lambdaQuery().eq(CompanyInformationAudit::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId()).eq(CompanyInformationAudit::getAuditStatus, Constant.SysCompanyAuditState.AUDIT_RENDING.state).count();
        if (companyInformation == null) {
            return null;
        }
        if (companyInformation == null && count > 0) {
            return null;
        }
        List<Company> companyList = companyService.lambdaQuery().eq(Company::getCode, companyInformation.getCode()).list();
        return companyList;
    }

    @Override
    public Boolean getFlag() {
        CompanyInformation companyInformation = companyInformationService.lambdaQuery().eq(CompanyInformation::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId()).one();
        if (companyInformation == null) {
            throw new ResolinkRuntimeException("700002");
        }
        return true;
    }

    @Override
    public Boolean audit(CompanyInfoAuditDTO auditDTO) {
        CompanyAudit entity = Optional.ofNullable(getById(auditDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        entity.setAuditUserId(SystemUtil.getInstance().getCurrentUserId());
        entity.setAuditStatus(auditDTO.getAuditStatus());
        entity.setAuditMsg(auditDTO.getAuditMsg());
        entity.setAuditTime(LocalDateTime.now());
        entity.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        //审核通过则插入企业入驻记录
        if (Constant.SysCompanyAuditState.AUDIT_PASS.state == auditDTO.getAuditStatus()) {
            CompanyInformation companyInformation = Optional.ofNullable(companyInformationService.getById(entity.getCompanyInformationId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            Company company = new Company();
            company.setParkOrgId(entity.getParkOrgId());
            company.setCode(companyInformation.getCode());
            company.setIndustryField(companyInformation.getIndustryField());
            company.setCompanyTaxesType(companyInformation.getCompanyTaxesType());
            company.setEnterpriseSize(companyInformation.getEnterpriseSize());
            company.setName(companyInformation.getName());
            company.setLicenses(companyInformation.getLicenses());
            company.setCompanyType(companyInformation.getCompanyType());
            company.setRemark(companyInformation.getRemark());
            company.setAtts(companyInformation.getAtts());
            company.setCorporateTax(companyInformation.getCorporateTax());
            company.setAddress(companyInformation.getAddress());
            company.setSource(DictConstant.AddDataSource.ENTERPRISE_ADDED.getCode());
            company.setCreateTime(entity.getAuditTime());
            company.setContacts(entity.getContactPerson());
            company.setTel(entity.getContactPhone());
            companyService.save(company);
        }
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public CompanyAuditDetailVo getCompanyAuditById(String id) {
        CompanyAudit entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        CompanyAuditDetailVo companyAuditDetailVo = MyBeanUtils.copy(entity, CompanyAuditDetailVo::new);
        companyAuditDetailVo.setCompanyInformation(companyInformationService.getById(entity.getCompanyInformationId()));
        return companyAuditDetailVo;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<CompanyAuditDTO> listCompanyAuditPage(CompanyAuditQuery query) {
        query.startPage();
        Set<String> companyIds = companyInformationService.lambdaQuery().like(StringUtils.isNotBlank(query.getName()), CompanyInformation::getName, query.getName())
                .eq(query.getCompanyType() != null, CompanyInformation::getCompanyType, query.getCompanyType())
                .list().parallelStream().map(CompanyInformation::getId).collect(Collectors.toSet());
        MyPageInfoUtils.isEmptyPage(companyIds);
        if (CollectionUtil.isNotEmpty(companyIds)) {
            query.setCompanyIds(companyIds);
        }
        List<CompanyAudit> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<CompanyAuditDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<CompanyAudit> getConditionWrapper(CompanyAuditQuery query) {
        Set<String> parkOrgIds;
        if (SystemUtil.getInstance().getSource().equals(Constants.USER_BUSINESS_SOURCE)) {
            CompanyInformation companyInformation = companyInformationService.lambdaQuery().select(CompanyInformation::getId).eq(CompanyInformation::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId()).one();
            MyPageInfoUtils.isEmptyPage(companyInformation);
            if (companyInformation != null) {
                query.setCompanyInformationId(companyInformation.getId());

            }
            parkOrgIds = SystemUtil.getInstance().getTenantParkOrgIds();
        } else {
            parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
        }
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<CompanyAudit> queryWrapper = new LambdaQueryWrapper<CompanyAudit>();
        if (CollectionUtil.isNotEmpty(query.getCompanyIds())) {
            queryWrapper.in(CompanyAudit::getCompanyInformationId, query.getCompanyIds());
        }
        queryWrapper.eq(StringUtils.isNotBlank(query.getCompanyInformationId()), CompanyAudit::getCompanyInformationId, query.getCompanyInformationId());
        queryWrapper.eq(query.getAuditStatus() != null, CompanyAudit::getAuditStatus, query.getAuditStatus());
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), CompanyAudit::getParkOrgId, query.getParkOrgId());
        queryWrapper.in(CompanyAudit::getParkOrgId, parkOrgIds).orderByDesc(CompanyAudit::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<CompanyAuditDTO> entitiesToDTOs(List<CompanyAudit> entities) {
        return MyBeanUtils.copyList(entities, CompanyAuditDTO::new, (s, t) -> {

        });
    }
}
