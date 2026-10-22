package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.enums.PermissionMarkEnum;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.aop.ResetUserCache;
import com.crlink.smartpark.system.dto.PermissionReqDTO;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Permission;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.service.common.*;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementMenuService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementPermissionService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.enums.PermissionTypeEnum.DATA;
import static com.crlink.smartpark.common.constants.enums.StatusEnum.DISABLE;

@Service
public class ParkManagementPermissionServiceImpl implements ParkManagementPermissionService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;


    @Autowired
    private RoleService roleService;

    @Autowired
    private RsUserRoleRelationService userRoleRelationService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ParkManagementMenuService parkManagementMenuService;


    @Override
    @Transactional
    @ResetUserCache
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean dataPermission(PermissionReqDTO reqDTO) {
        String roleId = reqDTO.getRoleId();
        Set<String> destIds = reqDTO.getDestIds();
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        //当前租户的所有组织Id
        Set<String> curTenantOrgIds = organizationService.lambdaQuery().select(Organization::getId)
                .eq(Organization::getHeadOrgId, headOrgId).list().parallelStream().map(Organization::getId).collect(Collectors.toSet());
        curTenantOrgIds.removeAll(destIds);
        //删除上传操作记录
        permissionService.remove(Wrappers.<Permission>lambdaQuery().eq(Permission::getRoleId, roleId).eq(Permission::getPermissionType, DATA.getType()));

        //禁止权限
        List<Permission> curRoleDisablePermission = curTenantOrgIds.stream().map(t -> {
            Permission permission = new Permission();
            permission.setDestId(t);
            permission.setRoleId(roleId);
            permission.setPermissionType(DATA.getType());
            permission.setStatus(DISABLE.getCode());
            EntityUserUtil.setAll(permission);
            return permission;
        }).collect(Collectors.toList());
        permissionService.saveBatch(curRoleDisablePermission);
        //用于回显示
        List<Permission> markPermission = destIds.stream().map(t -> {
            Permission permission = new Permission();
            permission.setRoleId(roleId);
            permission.setPermissionType(DATA.getType());
            permission.setDestId(t);
            permission.setMark(PermissionMarkEnum.EDIT_MARK.getCode());
            EntityUserUtil.setAll(permission);
            return permission;
        }).collect(Collectors.toList());
        permissionService.saveBatch(markPermission);
        return true;
    }


    //private Set<String> getChildOrganization(String orgId, Map<String, List<Organization>> parentMap, Set<String> result) {
    //    result.add(orgId);
    //    List<Organization> organizations = parentMap.get(orgId);
    //    if (CollectionUtil.isNotEmpty(organizations)) {
    //        organizations.forEach(t -> {
    //            getChildOrganization(t.getId(), parentMap, result);
    //        });
    //    }
    //    return result;
    //}


    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, key = "'echoData_roleId:'+#roleId")
    public Set<String> echoData(String roleId) {
        List<Permission> permissions = permissionService.lambdaQuery().select(Permission::getDestId)
                .eq(Permission::getRoleId, roleId)
                .eq(Permission::getPermissionType, DATA.getType())
                .eq(Permission::getMark, PermissionMarkEnum.EDIT_MARK.getCode()).list();
        Set<String> result = permissions.stream().map(Permission::getDestId).collect(Collectors.toSet());

        // 如果没有手动设置权限，回显该角色下默认的所有权限
        if (CollUtil.isEmpty(result) && CollUtil.isEmpty(getRoleDisabledDataId(roleId))) {
            //result = getCurrentRoleAllOrgId(roleId);
            String curRoleOrgId = roleService.lambdaQuery().select(Role::getOrgId).eq(Role::getId, roleId).one().getOrgId();
            result.add(curRoleOrgId);
        }
        //加上新增的子级组织
        Set<String> newChildOrgIds = editMarkedDataChildOrg(result, roleId);
        result.addAll(newChildOrgIds);
        return result;
    }


    private Set<String> editMarkedDataChildOrg(Set<String> editMarkIds, String roleId) {
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        Set<String> roleDisableOrgIds = getRoleDisabledDataId(roleId);
        Set<String> newChildOrgIds = Sets.newHashSet();
        if (CollectionUtil.isNotEmpty(editMarkIds)) {
            Map<String, List<String>> editMarkId2ChildId = organizationService.lambdaQuery().select(Organization::getId, Organization::getParentId)
                    .in(Organization::getParentId, editMarkIds).eq(Organization::getHeadOrgId, headOrgId).list().parallelStream()
                    .collect(Collectors.groupingBy(Organization::getParentId, Collectors.mapping(Organization::getId, Collectors.toList())));
            if (editMarkId2ChildId != null) {
                //新增的子级组织Id
                newChildOrgIds = editMarkId2ChildId.values().parallelStream().flatMap(t -> t.parallelStream())
                        .filter(orgId -> !roleDisableOrgIds.contains(orgId) && !editMarkIds.contains(orgId)).collect(Collectors.toSet());
            }
        }
        return newChildOrgIds;
    }

    private Set<String> getRoleDisabledDataId(String roleId) {
        Set<String> roleDisableOrgIds = permissionService.lambdaQuery()
                .select(Permission::getDestId)
                .eq(Permission::getPermissionType, DATA.getType())
                .eq(Permission::getStatus, DISABLE.getCode())
                .eq(Permission::getRoleId, roleId)
                .list().parallelStream().map(Permission::getDestId).collect(Collectors.toSet());
        return roleDisableOrgIds;
    }


}
