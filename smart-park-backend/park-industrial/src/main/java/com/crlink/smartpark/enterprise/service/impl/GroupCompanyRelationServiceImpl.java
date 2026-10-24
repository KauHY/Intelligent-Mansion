package com.crlink.smartpark.enterprise.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationQueryDTO;
import com.crlink.smartpark.common.feign.MultistageCategoryProvider;
import com.crlink.smartpark.enterprise.model.query.GroupCompanyRelationQuery;
import com.crlink.smartpark.industrial.dto.GroupCompanyRelationDTO;
import com.crlink.smartpark.enterprise.service.GroupCompanyRelationService;
import com.crlink.smartpark.system.dto.company.CompanyQueryDTO;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.field.CompanyFiled;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


/**
 * 公司分组关联表 服务实现类
 * */
@Service
public class GroupCompanyRelationServiceImpl implements GroupCompanyRelationService {

    @Autowired
    private CompanyProvider companyProvider;
    @Autowired
    private MultistageCategoryProvider multistageCategoryProvider;


    @Override
    public PageInfo<GroupCompanyRelationDTO> listGroupCompanyPage(GroupCompanyRelationQuery query) {
        PageInfo<MultistageCategoryRelationDTO> pageInfo = multistageCategoryProvider.listBusinessToCategoryRelation(MyBeanUtils.copy(query, MultistageCategoryRelationQueryDTO::new));
        List<MultistageCategoryRelationDTO> data = pageInfo.getList();
        Set<String> companyIds = data.stream().map(MultistageCategoryRelationDTO::getBusinessId).collect(Collectors.toSet());
        CompanyQueryDTO companyQueryDTO = CompanyQueryDTO.buildingIdsBuildDTO(companyIds, CompanyFiled.NAME
                , CompanyFiled.COMPANY_TYPE, CompanyFiled.INDUSTRY_FIELD, CompanyFiled.ENTERPRISE_SIZE, CompanyFiled.COMPANY_TAXES_TYPE);
        Map<String, Map<String, Object>> companyId2Obj = companyProvider.queryCompanyColumns(companyQueryDTO);
        List<GroupCompanyRelationDTO> list = getList(data, companyId2Obj);
        pageInfo.setList(null);
        PageInfo<GroupCompanyRelationDTO> result = MyBeanUtils.copy(pageInfo, PageInfo::new);
        result.setList(list);
        return result;
    }

    @NotNull
    private List<GroupCompanyRelationDTO> getList(List<MultistageCategoryRelationDTO> data, Map<String, Map<String, Object>> companyId2Obj) {
        List<GroupCompanyRelationDTO> relationDTOS = new ArrayList<>();
        data.forEach(e -> {
            Map<String, Object> company = companyId2Obj.get(e.getBusinessId());
            if (CollectionUtil.isNotEmpty(company)) {
                GroupCompanyRelationDTO groupCompanyRelationDTO = new GroupCompanyRelationDTO();
                groupCompanyRelationDTO.setId(e.getId());
                groupCompanyRelationDTO.setCompanyId(e.getBusinessId());
                groupCompanyRelationDTO.setCompanyName((String) company.get(CompanyFiled.NAME));
                groupCompanyRelationDTO.setCompanyType((Integer) company.get(CompanyFiled.COMPANY_TYPE));
                groupCompanyRelationDTO.setIndustryField((Integer) company.get(CompanyFiled.INDUSTRY_FIELD));
                groupCompanyRelationDTO.setCompanyTaxesType((Integer) company.get(CompanyFiled.COMPANY_TAXES_TYPE));
                groupCompanyRelationDTO.setEnterpriseSize((Integer) company.get(CompanyFiled.ENTERPRISE_SIZE));
                groupCompanyRelationDTO.setGroupId(e.getCategoryId());
                groupCompanyRelationDTO.setGroupName(e.getCategoryName());
                groupCompanyRelationDTO.setParkOrgId(e.getParkOrgId());
                groupCompanyRelationDTO.setParkOrgName(e.getParkOrgName());
                groupCompanyRelationDTO.setRemark(e.getRemark());
                relationDTOS.add(groupCompanyRelationDTO);
            }
        });
        return relationDTOS;
    }

}
