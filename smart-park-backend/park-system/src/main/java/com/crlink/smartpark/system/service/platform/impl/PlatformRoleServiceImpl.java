package com.crlink.smartpark.system.service.platform.impl;

import com.crlink.smartpark.system.dto.role.CreateRoleReqDTO;
import com.crlink.smartpark.system.dto.role.RoleVO;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.query.RoleQuery;
import com.crlink.smartpark.system.service.common.RoleManageService;
import com.crlink.smartpark.system.service.platform.PlatformRoleService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.Constants.PLATFORM_ORG_ID;

@Service
public class PlatformRoleServiceImpl implements PlatformRoleService {

    @Autowired
    private RoleManageService roleManageService;

    @Override
    public PageInfo<DictVO> listPlatformRoles(RoleQuery roleQuery) {
        return roleManageService.listRoleDict(Sets.newHashSet(PLATFORM_ORG_ID), roleQuery);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ORGANIZATION, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean createOrUpdateSysRole(CreateRoleReqDTO reqDTO) {

        Role role = new Role();
        role.setId(reqDTO.getId());
        role.setOrgId(PLATFORM_ORG_ID);
        role.setHeadOrgId(PLATFORM_ORG_ID);
        role.setAdminFlag(false);
        role.setRoleName(reqDTO.getRoleName());
        role.setRoleDescription(reqDTO.getRoleDescription());
        return roleManageService.saveOrUpdateRole(role, reqDTO.getUserIds());
    }

    @Override
    public PageInfo<RoleVO> pageRoles(RoleQuery roleQuery) {
        PageInfo<RoleVO> result = roleManageService.pageRoles(Sets.newHashSet(PLATFORM_ORG_ID), roleQuery);
        return result;
    }
}
