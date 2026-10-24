package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.role.RoleVO;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.entity.RsUser;
import com.crlink.smartpark.system.entity.RsUserRoleRelation;
import com.crlink.smartpark.system.query.RoleQuery;
import com.crlink.smartpark.system.service.common.*;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DICT_PAGE_SIZE;

@Service
public class RoleManageServiceImpl implements RoleManageService {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RsUserRoleRelationService userRoleRelationService;
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private RsUserService userService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRole(String roleId) {
        cannotUpdateOrDeleteAdminRole(roleId);
        Integer count = userRoleRelationService.lambdaQuery().eq(RsUserRoleRelation::getRoleId, roleId).count();
        if (count > 0) {
            throw new ResolinkRuntimeException("40020");
        }
        userRoleRelationService.remove(new LambdaQueryWrapper<RsUserRoleRelation>().eq(RsUserRoleRelation::getRoleId, roleId));
        return roleService.removeById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(UpdateStatusReqDTO reqDTO) {
        cannotUpdateOrDeleteAdminRole(reqDTO.getId());
        String userId = SystemUtil.getInstance().getCurrentUserId();
        LambdaUpdateWrapper<RsUserRoleRelation> relationUpdateWrapper = new UpdateWrapper<RsUserRoleRelation>().lambda()
                .set(RsUserRoleRelation::getStatus, reqDTO.getStatus())
                .set(RsUserRoleRelation::getUpdateBy, userId)
                .eq(RsUserRoleRelation::getRoleId, reqDTO.getId());
        userRoleRelationService.update(relationUpdateWrapper);
        LambdaUpdateWrapper<Role> roleUpdateWrapper = new UpdateWrapper<Role>().lambda()
                .set(Role::getStatus, reqDTO.getStatus())
                .set(Role::getUpdateBy, userId)
                .eq(Role::getId, reqDTO.getId());
        return roleService.update(roleUpdateWrapper);
//        return true;
    }


    @Override
    public PageInfo<RoleVO> pageRoles(Set<String> orgIds, RoleQuery roleQuery) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(roleQuery.getStatus() != null, Role::getStatus, roleQuery.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(roleQuery.getRoleName()), Role::getRoleName, roleQuery.getRoleName());
        queryWrapper.in(Role::getOrgId, orgIds);
        queryWrapper.orderByDesc(Role::getCreateTime);
        roleQuery.startPage();
        List<Role> roles = roleService.list(queryWrapper);
        if (CollectionUtil.isEmpty(roles)) {
            return new PageInfo<>();
        }
        List<RoleVO> roleVOS = role2RoleVO(roles);
        return roleQuery.createPageInfo(roleVOS);
    }

    @Override
    public PageInfo<DictVO> listRoleDict(Set<String> orgIds, RoleQuery roleQuery) {
        roleQuery.setPageSize(DICT_PAGE_SIZE);
        String roleName = roleQuery.getRoleName();
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Role::getId, Role::getRoleName)
                .in(Role::getOrgId, orgIds)
                .eq(Role::getStatus, StatusEnum.ENABLE.getCode())
                .like(StringUtils.isNotBlank(roleName), Role::getRoleName, roleName);
        roleQuery.startPage();
        List<Role> list = roleService.list(queryWrapper);
        List<DictVO> roleDictList = list.stream().map(t -> new DictVO(t.getRoleName(), t.getId())).collect(Collectors.toList());
        return roleQuery.createPageInfo(roleDictList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateRole(Role role, List<String> userIds) {
        cannotUpdateOrDeleteAdminRole(role.getId());
        checkRole(role);
        if (role.getId() != null) {
            EntityUserUtil.setUpdateBy(role);
            userRoleRelationService.remove(new LambdaQueryWrapper<RsUserRoleRelation>().eq(RsUserRoleRelation::getRoleId, role.getId()));
        } else {
            EntityUserUtil.setAll(role);
        }
        roleService.saveOrUpdate(role);
        List<RsUserRoleRelation> rsUserRoleRelations = userIds.stream().map(t -> {
            RsUserRoleRelation rsUserRoleRelation = new RsUserRoleRelation();
            rsUserRoleRelation.setUserId(t);
            rsUserRoleRelation.setRoleId(role.getId());
            EntityUserUtil.setAll(rsUserRoleRelation);
            return rsUserRoleRelation;
        }).collect(Collectors.toList());
        return userRoleRelationService.saveBatch(rsUserRoleRelations);
    }

    @Override
    public void cannotUpdateOrDeleteAdminRole(String roleId) {
        if (StringUtils.isNotBlank(roleId)) {
            Role role = roleService.getOne(Wrappers.<Role>lambdaQuery().select(Role::getId, Role::getAdminFlag).eq(Role::getId, roleId), false);
            if (role != null && role.getAdminFlag()) {
                throw new ResolinkRuntimeException("40017");
            }
        }
    }

    private void checkRole(Role role) {
        Integer roleCount = roleService.lambdaQuery().ne(role.getId() != null, Role::getId, role.getId()).eq(Role::getRoleName, role.getRoleName()).eq(Role::getHeadOrgId, role.getHeadOrgId()).count();
        if (roleCount > 0) {
            throw new ResolinkRuntimeException(Message.code("40011", role.getRoleName()));
        }
    }

    private List<RoleVO> role2RoleVO(List<Role> roles) {
        Map<String, String> roleId2OrgIdMap = roles.stream().collect(Collectors.toMap(Role::getId, Role::getOrgId));
        Map<String, String> orgId2OrgNameMap = organizationService.lambdaQuery().select(Organization::getId, Organization::getOrgName).in(Organization::getId, roleId2OrgIdMap.values()).list()
                .parallelStream().collect(Collectors.toMap(Organization::getId, Organization::getOrgName));
        Map<String, List<String>> roleId2UserIds = userRoleRelationService.lambdaQuery().select(RsUserRoleRelation::getRoleId, RsUserRoleRelation::getUserId).in(RsUserRoleRelation::getRoleId, roleId2OrgIdMap.keySet()).list()
                .parallelStream().collect(Collectors.groupingBy(RsUserRoleRelation::getRoleId, Collectors.mapping(RsUserRoleRelation::getUserId, Collectors.toList())));
        Map<String, String> userId2usernameMap = Collections.emptyMap();
        //角色对应的用户可能为空
        if (roleId2UserIds != null) {
            List<String> userIds = roleId2UserIds.values().parallelStream().flatMap(t -> t.stream()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(userIds)) {
                userId2usernameMap = userService.lambdaQuery().select(RsUser::getId, RsUser::getUsername).in(RsUser::getId, userIds).list().parallelStream().collect(Collectors.toMap(RsUser::getId, RsUser::getUsername));
            }
        }
        Map<String, String> finalUserId2usernameMap = userId2usernameMap;
        List<RoleVO> roleVOS = roles.stream().map(t -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(t, roleVO);
            String orgId = roleId2OrgIdMap.get(t.getId());
            roleVO.setOrgName(orgId2OrgNameMap.get(orgId));
            if (roleId2UserIds != null) {
                List<DictVO> users = roleId2UserIds.getOrDefault(t.getId(), Collections.emptyList()).parallelStream().map(uid -> new DictVO(finalUserId2usernameMap.get(uid), uid)).collect(Collectors.toList());
                roleVO.setUsers(users);
            }
            return roleVO;
        }).collect(Collectors.toList());
        return roleVOS;
    }


}
