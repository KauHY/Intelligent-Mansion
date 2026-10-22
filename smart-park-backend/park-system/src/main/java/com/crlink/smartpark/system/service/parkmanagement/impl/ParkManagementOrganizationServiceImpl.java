package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.aop.ResetUserCache;
import com.crlink.smartpark.system.dto.organization.OrgDTO;
import com.crlink.smartpark.system.dto.organization.OrgQuery;
import com.crlink.smartpark.system.dto.organization.OrgRespVO;
import com.crlink.smartpark.system.entity.Building;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Park;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.BuildingService;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementOrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.ParkService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.enums.OrgCategoryEnum.HEAD_COMPANY;
import static com.crlink.smartpark.common.constants.enums.OrgTypeEnum.*;

@Service
public class ParkManagementOrganizationServiceImpl implements ParkManagementOrganizationService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private CompanyService companyService;


    @Override
    @ResetUserCache
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean createOrganization(OrgDTO orgDTO) {
        checkOrgName(orgDTO);
        Organization organization = new Organization();
        BeanUtils.copyProperties(orgDTO, organization);
        EntityUserUtil.setAll(organization);
        Organization parent = organizationService.lambdaQuery()
                .select(Organization::getHeadOrgId, Organization::getLevel, Organization::getOrgType)
                .eq(Organization::getId, orgDTO.getParentId()).one();
        if (PARK.getCode() == parent.getOrgType()) {
            throw new ResolinkRuntimeException("20018");
        }
        organization.setLevel(parent.getLevel() + 1);
        organization.setHeadOrgId(parent.getHeadOrgId());
        organizationService.save(organization);
        if (PARK.getCode() == organization.getOrgType()) {
            Park park = new Park();
            park.setParkOrgId(organization.getId());
            parkService.save(park);
        }
        return true;
    }

    @Override
    @ResetUserCache
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean updateOrganization(OrgDTO orgDTO) {
        checkOrgName(orgDTO);
        Organization organization = new Organization();
        organization.setId(orgDTO.getId());
        organization.setOrgName(orgDTO.getOrgName());
//        organization.setOrgType(orgDTO.getOrgType());
        organization.setDescription(orgDTO.getDescription());
        EntityUserUtil.setUpdateBy(organization);
        return organizationService.updateById(organization);
    }

    @Override
    @ResetUserCache
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean removeOrganizationById(String id) {
        checkDeleteOrg(id);
        CompletableFuture.runAsync(() -> {
            organizationManageService.removeUserRelationInfoByOrgId(id);
        });
        return organizationService.removeById(id);
    }


    @Override
    public PageInfo<OrgRespVO> listOrganizationPage(OrgQuery query) {
        Set<String> orgIds = organizationManageService.listOrgIdsByUserId(SystemUtil.getInstance().getCurrentUserId());
        MyPageInfoUtils.isEmptyPage(orgIds);
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Organization::getId, orgIds);
        queryWrapper.like(StringUtils.isNotBlank(query.getOrgName()), Organization::getOrgName, query.getOrgName());
        Organization parentOrgType = organizationService.lambdaQuery().select(Organization::getOrgType).eq(Organization::getId, query.getParentId()).one();
        MyPageInfoUtils.isEmptyPage(parentOrgType);
        Integer orgType = parentOrgType.getOrgType();
        if (Objects.equals(orgType, PARK.getCode())) {
            queryWrapper.eq(Organization::getId, query.getParentId());
        } else {
            queryWrapper.eq(Organization::getParentId, query.getParentId());
            if (HEAD_COMPANY.getCode().equals(query.getParentId())) {
                queryWrapper.eq(Organization::getOrgType, COMPANY_ORGANIZATION.getCode());
            }
        }

        queryWrapper.orderByDesc(Organization::getCreateTime);
        query.startPage();
        List<Organization> list = organizationService.list(queryWrapper);
        Set<String> parentIds = list.parallelStream().map(Organization::getParentId).collect(Collectors.toSet());

        List<OrgRespVO> respVOS = list.stream().map(t -> {
            OrgRespVO orgRespVO = new OrgRespVO();
            orgRespVO.setId(t.getId());
            orgRespVO.setOrgName(t.getOrgName());
            if (t.getParentId().equals(HEAD_COMPANY.getCode())) {
                orgRespVO.setOrgType(TENANT.getCode());
                orgRespVO.setParentOrgName(Message.code("TENANT_HIGHER_ORG").getDisplayText());
            } else {
                Organization parent = organizationService.lambdaQuery().select(Organization::getOrgName).eq(Organization::getId, t.getParentId()).one();
                orgRespVO.setParentOrgName(parent.getOrgName());
                if (t.getOrgType().equals(PARK.getCode())) {
                    orgRespVO.setOrgType(PARK.getCode());
                } else {
                    orgRespVO.setOrgType(COMPANY_ORGANIZATION.getCode());
                }
            }
            orgRespVO.setParentId(t.getParentId());
            orgRespVO.setCreateTime(t.getCreateTime());
            orgRespVO.setDescription(t.getDescription());
            return orgRespVO;
        }).collect(Collectors.toList());
        return query.createPageInfo(respVOS);
    }


    private void checkOrgName(OrgDTO orgDTO) {
        String parentId = orgDTO.getParentId();
        String orgName = orgDTO.getOrgName();
        String headOrgId = organizationService.lambdaQuery().select(Organization::getHeadOrgId).eq(Organization::getId, parentId).one().getHeadOrgId();
        Integer count = organizationService.lambdaQuery().eq(Organization::getOrgName, orgName)
                .ne(StringUtils.isNotBlank(orgDTO.getId()), Organization::getId, orgDTO.getId())
                .eq(Organization::getHeadOrgId, headOrgId).count();
        if (count > 0) {
            throw new ResolinkRuntimeException(Message.code("20010"));
        }
    }

    /**
     * 当所选组织有下级组织,检验园区是否已被入驻企业/楼栋房屋所关联,不允许删除
     *
     * @param parkOrgId
     */
    private void checkDeleteOrg(String parkOrgId) {
        List<Organization> organizations = organizationService.lambdaQuery().select(Organization::getId).eq(Organization::getParentId, parkOrgId).list();
        if (CollectionUtil.isNotEmpty(organizations)) {
            throw new ResolinkRuntimeException(Message.code("20015"));
        }
        Integer buildingCount = buildingService.lambdaQuery().eq(Building::getParkOrgId, parkOrgId).last("limit 1").count();
        Integer companyCount = companyService.lambdaQuery().eq(Company::getParkOrgId, parkOrgId).last("limit 1").count();
        if (buildingCount > 0 || companyCount > 0) {
            throw new ResolinkRuntimeException("20022");
        }
    }

}
