package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.core.cache.CacheKeyUserId;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.SortUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.organization.OrgDTO;
import com.crlink.smartpark.system.dto.organization.OrgRespVO;
import com.crlink.smartpark.system.dto.organization.OrgStructureVO;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.service.common.*;
import com.crlink.smartpark.system.service.parkmanagement.ParkService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.enums.OrgTypeEnum.PARK;
import static com.crlink.smartpark.common.constants.enums.PermissionTypeEnum.DATA;


@Slf4j
@Service
public class OrganizationManageServiceImpl implements OrganizationManageService {

    public static final String PARENT_ID_IS_ZERO = "0";

    public static final String PARENT_ID_IS_SPECIAL = "-1";

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RsUserRoleRelationService userRoleRelationService;

    @Autowired
    private RsUserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ParkService parkService;

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, key = "'getAllParkOrgIdsByOrgId_orgId:'+#orgId+'_userId:'+#userId")
    public Set<String> getAllParkOrgIdsByOrgId(String orgId, String userId) {
        List<Organization> childOrganizations = this.listChildOrganizationByUserId(orgId, userId);
        if (CollectionUtil.isNotEmpty(childOrganizations)) {
            Set<String> parkOrgIds = childOrganizations.stream()
                    .filter(o -> Objects.nonNull(o) && Objects.equals(PARK.getCode(), o.getOrgType()))
                    .map(Organization::getId).collect(Collectors.toSet());
            return parkOrgIds;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, key = "'listOrgIdsByUserId_userId:'+#userId")
    public Set<String> listOrgIdsByUserId(String userId) {
        Set<String> orgIds = this.listOrganizationByUserId(userId).stream().map(t -> t.getId()).collect(Collectors.toSet());
        return orgIds;
    }

    /**
     * 获取当前用户拥有权限的园区ID集合
     *
     * @param userId
     * @return
     */
    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, key = "'parkOrgIds_userId:'+#userId")
    public Set<String> listParkOrgIdsByUserId(String userId) {
        Set<String> parkOrgIds = this.listOrganizationByUserId(userId).stream()
                .filter(t -> t.getOrgType().equals(PARK.getCode()))
                .map(t -> t.getId()).collect(Collectors.toSet());
        return parkOrgIds;
    }


    @Override
    public List<Organization> listChildOrganizationByUserId(String orgId, String userId) {
//        String userId = SystemUtil.getInstance().getCurrentUserId();
        Set<Organization> organizations = this.listOrganizationByUserId(userId);
        Map<String, Organization> idMap = organizations.stream().collect(Collectors.toMap(Organization::getId, o -> o));
        Map<String, List<Organization>> parentIdMap = organizations.stream().collect(Collectors.groupingBy(Organization::getParentId));
        List<Organization> result = Lists.newArrayList();
        recursionOrgChild(orgId, idMap, parentIdMap, result);
        return result;
    }

    private void recursionOrgChild(String currentOrgId, Map<String, Organization> idMap, Map<String, List<Organization>> map, List<Organization> result) {
        result.add(idMap.get(currentOrgId));
        List<Organization> organizations = map.get(currentOrgId);
        if (CollectionUtil.isNotEmpty(organizations)) {
            organizations.forEach(o -> {
                recursionOrgChild(o.getId(), idMap, map, result);
            });
        }
    }


    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, keyGenerator = CacheKeyGenerator)
    @CacheKeyUserId
    public List<OrgDTO> listOrgStructureByUserId(String parentId) {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        RsUserLoginVO userInfo = SystemUtil.getInstance().getCurrentUserInfo();
        String orgId = userInfo.getOrgId();
        //查询当前层级
        if (StringUtils.isBlank(parentId)) {
            Organization o = organizationService.lambdaQuery().select(Organization::getId, Organization::getOrgName, Organization::getOrgType, Organization::getParentId).eq(Organization::getId, orgId).one();
            OrgDTO orgDTO = new OrgDTO(o.getId(), o.getOrgName(), o.getOrgType());
            orgDTO.setParentId(o.getParentId());
            return Lists.newArrayList(orgDTO);
        }
        String finalParentId = parentId;
        List<OrgDTO> OrgDTOS = listOrganizationByUserId(userId).stream()
                .filter(t -> t.getParentId().equals(finalParentId))
                .map(t -> new OrgDTO(t.getId(), t.getOrgName(), t.getOrgType()))
                .sorted((v1, v2) -> Collator.getInstance(Locale.CHINA).compare(v1.getOrgName(), v2.getOrgName()))
                .collect(Collectors.toList());
        return OrgDTOS;
    }


    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, keyGenerator = CacheKeyGenerator)
    public OrgStructureVO listOrganization(BaseQuery<String> query) {
        String orgName = query.getParam();
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Set<String> orgIds = this.listOrgIdsByUserId(userId);
        List<Organization> organizations = organizationService.lambdaQuery().select(Organization::getId, Organization::getOrgName, Organization::getParentId, Organization::getOrgType, Organization::getLevel)
                .like(StringUtils.isNotBlank(orgName), Organization::getOrgName, orgName).in(Organization::getId, orgIds).list();
        organizations.sort(Comparator.comparing(Organization::getLevel, Comparator.reverseOrder()));
        Set<Organization> permissionOrgs = this.listOrganizationByUserId(userId);
        Map<String, List<Organization>> listMap = permissionOrgs.stream().collect(Collectors.groupingBy(Organization::getId));
        Map<Integer, List<Organization>> permissionOrgLevel = permissionOrgs.stream().collect(Collectors.groupingBy(Organization::getLevel));
        Optional<Integer> firstLevel = permissionOrgLevel.keySet().stream().sorted(Comparator.comparing(e -> e)).findFirst();
//        找出当前用户最高组织
        if (firstLevel.isPresent()) {
            OrgStructureVO highestNode = new OrgStructureVO();
            Organization organization = permissionOrgLevel.get(firstLevel.get()).get(0);
            highestNode.setId(organization.getId());
            highestNode.setOrgName(organization.getOrgName());
            highestNode.setOrgType(organization.getOrgType());
            highestNode.setParentId(organization.getParentId());
            HashMap<String, OrgStructureVO> map = Maps.newHashMap();
            organizations.forEach(t -> {
                //当前节点是否已经放入map
                OrgStructureVO vo = map.get(t.getId());
                if (vo == null) {
                    vo = new OrgStructureVO();
                    vo.setId(t.getId());
                    vo.setOrgName(t.getOrgName());
                    vo.setParentId(t.getParentId());
                    vo.setOrgType(t.getOrgType());
                    map.put(vo.getId(), vo);
                }
                //递归构建上级节点
                recursion1(map, vo, listMap);
            });
            map.forEach((k, v) -> {
                if (v.getParentId().equals(highestNode.getId())) {
                    highestNode.getChildren().add(v);
                }
            });
            return highestNode;
        }
        return null;
    }

    private void recursion1(HashMap<String, OrgStructureVO> map, OrgStructureVO vo, Map<String, List<Organization>> groupOrg) {
        if (CollectionUtil.isEmpty(groupOrg.get(vo.getParentId()))) {
            return;
        }
        OrgStructureVO p = map.get(vo.getParentId());
        if (p == null && CollectionUtil.isNotEmpty(groupOrg.get(vo.getParentId()))) {
            Organization parent = groupOrg.get(vo.getParentId()).get(0);
            p = new OrgStructureVO();
            p.setId(parent.getId());
            p.setParentId(parent.getParentId());
            p.setOrgType(parent.getOrgType());
            p.setOrgName(parent.getOrgName());
        }
        p.getChildren().add(vo);
        map.put(p.getId(), p);
        recursion1(map, p, groupOrg);
    }


    @Override
    public Set<Organization> listOrganizationByUserId(String userId) {
        Set<Organization> organizations = Sets.newHashSet();
        Set<String> roleIds = userRoleRelationService.lambdaQuery().select(RsUserRoleRelation::getRoleId).eq(RsUserRoleRelation::getUserId, userId).list()
                .stream().map(RsUserRoleRelation::getRoleId).collect(Collectors.toSet());
        for (String roleId : roleIds) {
            Set<Organization> structureByRole = this.listStructureByRole(roleId);
            organizations.addAll(structureByRole);
        }
        return organizations;
    }

    //@Override
    private Set<Organization> listStructureByRole(String roleId) {
        Role role = roleService.lambdaQuery().select(Role::getId, Role::getOrgId).eq(Role::getId, roleId).one();
        if (role == null) {
            return Sets.newHashSet();
        }
        Set<String> disableOrgIds = permissionService.lambdaQuery().eq(Permission::getRoleId, role.getId()).eq(Permission::getStatus, StatusEnum.DISABLE.getCode())
                .eq(Permission::getPermissionType, DATA.getType()).list().stream().map(t -> t.getDestId()).collect(Collectors.toSet());
        Set<String> disableTreeOrgIds = Sets.newHashSet();
        //查出的禁用节点及其子节点
        disableOrgIds.forEach(t -> getAllOrgIdsRecursion(disableTreeOrgIds, t));
        String orgId = role.getOrgId();
        Set<String> thisRoleOrgIds = Sets.newHashSet();
        ////查出当前节点及其包含的子节点
        //getAllOrgIdsRecursion(thisRoleOrgIds, orgId);
        ////移除禁用的节点
        //thisRoleOrgIds.removeAll(disableTreeOrgIds);
        getEnabledOrgIds(thisRoleOrgIds, orgId, disableOrgIds);
        Set<Organization> allOrganization = Collections.emptySet();
        if (CollectionUtil.isNotEmpty(thisRoleOrgIds)) {
            allOrganization = organizationService.lambdaQuery().in(Organization::getId, thisRoleOrgIds).list().stream().collect(Collectors.toSet());
        }
        return allOrganization;
    }

    private void getEnabledOrgIds(Set<String> thisRoleOrgIds, String curOrgId, Set<String> disableOrgIds) {
        thisRoleOrgIds.add(curOrgId);
        Set<String> orgIdSet = organizationService.lambdaQuery().select(Organization::getId).eq(Organization::getParentId, curOrgId).list().stream().map(Organization::getId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(orgIdSet)) {
            orgIdSet.forEach(t -> {
                if (!disableOrgIds.contains(t)) {
                    thisRoleOrgIds.add(t);
                    getAllOrgIdsRecursion(thisRoleOrgIds, t);
                }
            });
        }
    }

    private void getAllOrgIdsRecursion(Set<String> orgIds, String curOrgId) {
        orgIds.add(curOrgId);
        Set<String> orgIdSet = organizationService.lambdaQuery().select(Organization::getId).eq(Organization::getParentId, curOrgId).list().stream().map(Organization::getId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(orgIdSet)) {
            orgIds.addAll(orgIdSet);
            orgIdSet.forEach(t -> {
                getAllOrgIdsRecursion(orgIds, t);
            });
        }
    }


    @Override
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean updateStatus(UpdateStatusReqDTO statusDTO) {
        Organization organization = new Organization();
        organization.setId(statusDTO.getId());
        organization.setStatus(statusDTO.getStatus());
        organization.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        return organizationService.updateById(organization);
    }


    @Override
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean removeUserRelationInfoByOrgId(String orgId) {
        Set<String> userIds = userInfoService.lambdaQuery().select(RsUserInfo::getUserId).eq(RsUserInfo::getOrgId, orgId).list().parallelStream().map(RsUserInfo::getUserId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(userIds)) {
            userService.remove(Wrappers.<RsUser>lambdaQuery().in(RsUser::getId, userIds));
            userInfoService.remove(Wrappers.<RsUserInfo>lambdaQuery().in(RsUserInfo::getUserId, userIds));
            userRoleRelationService.remove(Wrappers.<RsUserRoleRelation>lambdaQuery().in(RsUserRoleRelation::getUserId, userIds));
        }
        return roleService.remove(Wrappers.<Role>lambdaQuery().eq(Role::getOrgId, orgId));
    }

    @Override
//    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, key = "'getTenantParkOrgIds_headOrgId:'+#headOrgId")
    public Set<String> getTenantParkOrgIds(Set<String> headOrgIds) {
        Set<String> result = organizationService.lambdaQuery().select(Organization::getId)
                .in(Organization::getHeadOrgId, headOrgIds)
                .eq(Organization::getOrgType, PARK.getCode())
                .eq(Organization::getStatus, StatusEnum.ENABLE.getCode())
                .list()
                .parallelStream().map(Organization::getId).collect(Collectors.toSet());
        return result;
    }

    @Override
    public Set<String> getTenantParkOrgIds() {
        Set<String> result = organizationService.lambdaQuery().select(Organization::getId)
                .eq(Organization::getOrgType, PARK.getCode())
                .eq(Organization::getStatus, StatusEnum.ENABLE.getCode())
                .list()
                .parallelStream().map(Organization::getId).collect(Collectors.toSet());
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, key = "'getTenantParkOrgDict_headOrgId:'+#headOrgId")
    public List<DictVO> getTenantParkOrgDict(String headOrgId) {
        List<DictVO> list = organizationService.lambdaQuery().select(Organization::getId, Organization::getOrgName)
                .eq(Organization::getHeadOrgId, headOrgId)
                .eq(Organization::getOrgType, PARK.getCode())
                .eq(Organization::getStatus, StatusEnum.ENABLE.getCode())
                .list()
                .parallelStream().map(t -> new DictVO(t.getOrgName(), t.getId())).collect(Collectors.toList());
        list = SortUtils.pinYinSort(list, DictVO::getLabel);
        return list;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_ORGANIZATION, key = "'getOrganizationById_id:'+#id")
    public OrgRespVO getOrganizationById(String id) {
        Organization organization = organizationService.getById(id);
        String parentId = organization.getParentId();
        OrgRespVO result = MyBeanUtils.copy(organization, OrgRespVO::new);
        if (PARENT_ID_IS_SPECIAL.equals(parentId) || PARENT_ID_IS_ZERO.equals(parentId)) {
            result.setParentOrgName("无");
        } else {
            Map<String, String> parentOrgId2NameMap = (Map<String, String>) organizationService.id2ValueMap(Sets.newHashSet(parentId), Organization::getOrgName);
            result.setParentOrgName(parentOrgId2NameMap.get(parentId));
        }
        return result;
    }


    private Set<String> getChildOrganization(String orgId, Map<String, List<String>> parentId2IdMap, Set<String> result) {
        result.add(orgId);
        List<String> childOrgIds = parentId2IdMap.get(orgId);
        if (CollectionUtil.isNotEmpty(childOrgIds)) {
            childOrgIds.forEach(t -> {
                getChildOrganization(t, parentId2IdMap, result);
            });
        }
        return result;
    }


}
