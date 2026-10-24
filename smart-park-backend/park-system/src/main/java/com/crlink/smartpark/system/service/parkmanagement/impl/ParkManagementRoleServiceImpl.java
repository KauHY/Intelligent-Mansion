package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.role.CreateRoleReqDTO;
import com.crlink.smartpark.system.dto.role.RoleVO;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.query.RoleQuery;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.RoleManageService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementRoleService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.crlink.smartpark.common.constants.CacheConstant.*;

@Service
public class ParkManagementRoleServiceImpl implements ParkManagementRoleService {

    @Autowired
    private RoleManageService roleManageService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public PageInfo<DictVO> listPlatformRoles(RoleQuery roleQuery) {
        Set<String> orgIds = organizationManageService.listOrgIdsByUserId(SystemUtil.getInstance().getCurrentUserId());
        if (CollectionUtil.isEmpty(orgIds)) {
            return new PageInfo<>();
        }
        return roleManageService.listRoleDict(orgIds, roleQuery);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean createOrUpdateSysRole(CreateRoleReqDTO reqDTO) {
        Assert.notNull(reqDTO.getOrgId(), "orgId is null");
        Role role = new Role();
        role.setId(reqDTO.getId());
        role.setOrgId(reqDTO.getOrgId());
        String headOrgId = organizationService.lambdaQuery().select(Organization::getHeadOrgId).eq(Organization::getId, reqDTO.getOrgId()).one().getHeadOrgId();
        role.setHeadOrgId(headOrgId);
        role.setAdminFlag(false);
        role.setRoleName(reqDTO.getRoleName());
        role.setRoleDescription(reqDTO.getRoleDescription());
        return roleManageService.saveOrUpdateRole(role, reqDTO.getUserIds());
    }

    @Override
    public PageInfo<RoleVO> pageRoles(RoleQuery roleQuery) {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Set<String> orgIds = organizationManageService.listOrgIdsByUserId(userId);
        if (CollectionUtil.isEmpty(orgIds)) {
            return new PageInfo<>();
        }
        PageInfo<RoleVO> result = roleManageService.pageRoles(orgIds, roleQuery);
        return result;
    }
}
