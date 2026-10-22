package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.entity.RsUser;
import com.crlink.smartpark.system.entity.RsUserRoleRelation;
import com.crlink.smartpark.system.mapper.RsUserMapper;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.RoleService;
import com.crlink.smartpark.system.service.common.RsUserRoleRelationService;
import com.crlink.smartpark.system.service.common.RsUserService;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 * */
@Service
public class RsUserServiceImpl extends ServiceImpl<RsUserMapper, RsUser> implements RsUserService {

    @Autowired
    private RsUserRoleRelationService roleRelationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService organizationService;


    @Override
    public Map<String, ?> id2ValueMap(Set<String> ids, SFunction<RsUser, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> userId2ValueMap = this.lambdaQuery()
                .select(RsUser::getId, sValue)
                .in(RsUser::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(RsUser::getId, sValue));
        return userId2ValueMap;
    }


    /**
     * 分页查询
     *
     * @param rsUserQueryDTO 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<RsUserInfoVO> listPage(Set<String> orgIds, RsUserQueryDTO rsUserQueryDTO) {
        rsUserQueryDTO.startPage();
        List<RsUserInfoVO> users = baseMapper.listPage(rsUserQueryDTO, orgIds);
        //userId orgId关系
        if (CollectionUtil.isNotEmpty(users)) {
            Map<String, String> userId2OrgIdMap = users.parallelStream().collect(Collectors.toMap(RsUserInfoVO::getUserId, RsUserInfoVO::getOrgId));
            //orgId OrgName
            Map<String, String> orgId2OrgNameMap = (Map<String, String>) organizationService.id2ValueMap(userId2OrgIdMap.values(), Organization::getOrgName);
            //userId 对应的角色列表
            Map<String, List<String>> userId2RoleIdsMap = roleRelationService.lambdaQuery().select(RsUserRoleRelation::getRoleId, RsUserRoleRelation::getUserId)
                    .in(RsUserRoleRelation::getUserId, userId2OrgIdMap.keySet()).list().parallelStream()
                    .collect(Collectors.groupingBy(RsUserRoleRelation::getUserId, Collectors.mapping(RsUserRoleRelation::getRoleId, Collectors.toList())));
            List<String> roleIds = userId2RoleIdsMap.values().stream().flatMap(t -> t.stream()).collect(Collectors.toList());
            Map<String, String> roleId2NameMap = roleService.lambdaQuery().select(Role::getId, Role::getRoleName)
                    .in(Role::getId, roleIds).list().stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));
            users.forEach(t -> {
                String orgId = userId2OrgIdMap.get(t.getUserId());
                t.setOrgName(orgId2OrgNameMap.get(orgId));
                List<String> roleIdList = userId2RoleIdsMap.get(t.getUserId());
                if (CollUtil.isNotEmpty(roleIdList)) {
                    t.setRoleIds(roleIdList);
                    List<String> names = roleIdList.parallelStream().map(roleId -> roleId2NameMap.get(roleId)).collect(Collectors.toList());
                    t.setRoleNames(names);
                }
            });
        }
        return rsUserQueryDTO.createPageInfo(users);
    }


}
