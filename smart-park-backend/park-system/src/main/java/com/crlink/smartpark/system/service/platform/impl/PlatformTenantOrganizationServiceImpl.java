package com.crlink.smartpark.system.service.platform.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.enums.OrgTypeEnum;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.aop.ResetUserCache;
import com.crlink.smartpark.system.dto.event.CopyMenuEvent;
import com.crlink.smartpark.system.dto.event.TenantRemoveMenuEvent;
import com.crlink.smartpark.system.dto.platform.TenantOrganizationVO;
import com.crlink.smartpark.system.dto.platform.TenantReqDTO;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.query.TenantQuery;
import com.crlink.smartpark.system.service.common.*;
import com.crlink.smartpark.system.service.platform.PlatformTenantOrganizationService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.enums.OrgCategoryEnum.HEAD_COMPANY;

@Service
public class PlatformTenantOrganizationServiceImpl implements PlatformTenantOrganizationService {

    public static final String ADMIN_NAME = "超级管理员";
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RsUserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RsUserRoleRelationService userRoleRelationService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public PageInfo<TenantOrganizationVO> listTenantPage(BaseQuery condition) {
        TenantQuery tenantQuery = (TenantQuery) condition;
        String orgName = tenantQuery.getOrgName();
        Integer status = tenantQuery.getStatus();
        tenantQuery.startPage();
        List<Organization> organizations = organizationService.lambdaQuery()
                .eq(Organization::getParentId, HEAD_COMPANY.getCode())
                .like(StringUtils.isNotBlank(orgName), Organization::getOrgName, orgName)
                .eq(null != status, Organization::getStatus, status)
                .orderByDesc(Organization::getCreateTime).list();
        MyPageInfoUtils.isEmptyPage(organizations);
        List<TenantOrganizationVO> vos = organizationToTenantOrganizationVO(organizations);
        return tenantQuery.createPageInfo(vos);
    }

    /**
     * 查询租户详情
     *
     * @param id
     * @return
     */
    @Override
    public TenantOrganizationVO getTenantInfoById(String id) {
        Organization organization = organizationService.getById(id);
        TenantOrganizationVO result = organizationToTenantOrganizationVO(Lists.newArrayList(organization)).get(0);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean removeTenantById(String id) {
        List<Organization> organizations = organizationService.lambdaQuery().select(Organization::getId).eq(Organization::getParentId, id).list();
        if (CollectionUtil.isNotEmpty(organizations)) {
            throw new ResolinkRuntimeException(Message.code("20014"));
        }
        //删除该租户菜单
        applicationContext.publishEvent(new TenantRemoveMenuEvent(this, id));
        //删除角色、用户
        CompletableFuture.runAsync(() -> {
            organizationManageService.removeUserRelationInfoByOrgId(id);
        });
        return organizationService.removeById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ResetUserCache
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public void create(TenantReqDTO tenantCreateReqDTO) {
        String orgName = tenantCreateReqDTO.getOrgName();
        checkOrgName(null, orgName);
        checkAppIdExist(null, tenantCreateReqDTO.getCAppId(), tenantCreateReqDTO.getBAppId());
        //创建组织
        Organization organization = MyBeanUtils.copy(tenantCreateReqDTO, Organization::new);
        DefaultIdentifierGenerator generator = new DefaultIdentifierGenerator();
        Long id = generator.nextId(organization);
        organization.setId(id.toString());
        organization.setHeadOrgId(id.toString());
        organization.setParentId(HEAD_COMPANY.getCode());
        organization.setLevel(TENANT_LEVEL);
        organization.setOrgType(OrgTypeEnum.COMPANY_ORGANIZATION.getCode());
        EntityUserUtil.setAll(organization);
        organizationService.save(organization);
        //租户的用户信息
        //获取本对象的代理对象，再进行调用;获取到xxxService的代理类，再调用事务方法，强行经过代理类，激活事务切面。
        ((PlatformTenantOrganizationServiceImpl) AopContext.currentProxy()).createTenantUserInfo(organization.getId(), tenantCreateReqDTO);
        //创建菜单
        applicationContext.publishEvent(new CopyMenuEvent(this, organization.getId()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE}, allEntries = true)
    public void update(TenantReqDTO updateReqDTO) {
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        String orgId = updateReqDTO.getId();
        checkOrgName(orgId, updateReqDTO.getOrgName());
        checkAppIdExist(orgId, updateReqDTO.getCAppId(), updateReqDTO.getBAppId());
        Organization organization = SecretDesensitization(updateReqDTO);
        organization.setUpdateBy(currentUserId);
        organizationService.updateById(organization);
        RsUserInfo userInfo = userInfoService.lambdaQuery().select(RsUserInfo::getId, RsUserInfo::getUserId)
                .eq(RsUserInfo::getOrgId, orgId)
                .eq(RsUserInfo::getAdminFlag, true).last("limit 1")
                .one();
        checkUserNameAndMobile(userInfo.getUserId(), updateReqDTO.getTenantUserName(), updateReqDTO.getContactUserMobile(), TENANT_USER);
        RsUser user = new RsUser();
        user.setId(userInfo.getUserId());
        user.setUsername(updateReqDTO.getTenantUserName());
        user.setUpdateBy(currentUserId);
        userService.updateById(user);
        userInfo.setRealName(updateReqDTO.getContactUserName());
        userInfo.setMobile(updateReqDTO.getContactUserMobile());
        userInfo.setUpdateBy(currentUserId);
        userInfoService.updateById(userInfo);
    }


    /**
     * 密钥脱敏
     *
     * @param updateReqDTO
     * @return
     */
    private Organization SecretDesensitization(TenantReqDTO updateReqDTO) {
        Organization originOrganization = organizationService.lambdaQuery()
                .select(Organization::getBAppSecret, Organization::getCAppSecret)
                .eq(Organization::getId, updateReqDTO.getId()).one();
        Organization copyOrganization = MyBeanUtils.copy(updateReqDTO, Organization::new);
        if (StringUtils.contains(updateReqDTO.getBAppSecret(), "*")) {
            copyOrganization.setBAppSecret(originOrganization.getBAppSecret());
        }
        if (StringUtils.contains(updateReqDTO.getCAppSecret(), "*")) {
            copyOrganization.setCAppSecret(originOrganization.getCAppSecret());
        }
        return copyOrganization;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createTenantUserInfo(String headOrgId, TenantReqDTO tenantCreateReqDTO) {
        checkUserNameAndMobile(null, tenantCreateReqDTO.getTenantUserName(), tenantCreateReqDTO.getContactUserMobile(), TENANT_USER);
        //创建用户
        RsUser rsUser = toUserEntity(tenantCreateReqDTO);
        userService.save(rsUser);
        userInfoService.save(toUserInfoEntity(rsUser.getId(), headOrgId, tenantCreateReqDTO.getContactUserName(), tenantCreateReqDTO.getContactUserMobile()));
        Role role = toRoleEntity(headOrgId);
        roleService.save(role);
        userRoleRelationService.save(toUserRoleRelation(role.getId(), rsUser.getId()));
    }


    private List<TenantOrganizationVO> organizationToTenantOrganizationVO(List<Organization> organizations) {
        Set<String> orgIds = organizations.parallelStream().map(Organization::getId).collect(Collectors.toSet());
        Map<String, RsUserInfo> orgId2UserInfoMap = userInfoService.lambdaQuery()
                .select(RsUserInfo::getUserId, RsUserInfo::getOrgId, RsUserInfo::getRealName, RsUserInfo::getMobile)
                .eq(RsUserInfo::getAdminFlag, true).in(RsUserInfo::getOrgId, orgIds).list()
                .parallelStream().collect(Collectors.toMap(RsUserInfo::getOrgId, Function.identity()));
        Map<String, String> userId2UsernameMap = Collections.EMPTY_MAP;

        if (ObjectUtil.isNotEmpty(orgId2UserInfoMap)) {
            Set<String> userIds = orgId2UserInfoMap.values().parallelStream().map(RsUserInfo::getUserId).collect(Collectors.toSet());
            userId2UsernameMap = userService.lambdaQuery().select(RsUser::getId, RsUser::getUsername).in(RsUser::getId, userIds)
                    .list().parallelStream().collect(Collectors.toMap(RsUser::getId, RsUser::getUsername));
        }
        Map<String, String> finalUserId2UsernameMap = userId2UsernameMap;
        List<TenantOrganizationVO> vos = organizations.stream().map(t -> toTenantOrganizationVO(orgId2UserInfoMap, finalUserId2UsernameMap, t)).collect(Collectors.toList());
        return vos;
    }

    private TenantOrganizationVO toTenantOrganizationVO(Map<String, RsUserInfo> orgId2UserInfoMap, Map<String, String> userId2UsernameMap, Organization organization) {
        TenantOrganizationVO vo = new TenantOrganizationVO();
        BeanUtils.copyProperties(organization, vo);
        RsUserInfo userInfo = orgId2UserInfoMap.get(organization.getId());
        if (userInfo != null) {
            String userId = userInfo.getUserId();
            vo.setTenantUserName(userId2UsernameMap.get(userId));
            vo.setContactUserName(userInfo.getRealName());
            vo.setContactUserMobile(userInfo.getMobile());
        }
        if (StringUtils.isNotBlank(organization.getBAppSecret())) {
            vo.setBAppSecret(StrUtil.hide(organization.getBAppSecret(), 0, organization.getBAppSecret().length()));
        }
        if (StringUtils.isNotBlank(organization.getCAppSecret())) {
            vo.setCAppSecret(StrUtil.hide(organization.getCAppSecret(), 0, organization.getCAppSecret().length()));
        }
        return vo;
    }

    private Role toRoleEntity(String headOrgId) {
        Role role = new Role();
        role.setOrgId(headOrgId);
        role.setHeadOrgId(headOrgId);
        role.setAdminFlag(true);
        role.setRoleName(ADMIN_NAME);
        EntityUserUtil.setAll(role);
        return role;
    }

    private RsUserRoleRelation toUserRoleRelation(String roleId, String userId) {
        RsUserRoleRelation relation = new RsUserRoleRelation();
        relation.setUserId(userId);
        relation.setRoleId(roleId);
        return relation;
    }

    private RsUser toUserEntity(TenantReqDTO tenantCreateReqDTO) {
        RsUser user = new RsUser();
        //Todo 通过短信下发生成的密码
        //密码加密,密码前、后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256("12345678a" + WEB_SALT);
        encodePwd = StringEncodeUtil.Str2SHA256(encodePwd + SYSTEM_SALT);
        user.setPassword(encodePwd);
        user.setUsername(tenantCreateReqDTO.getTenantUserName());
        user.setUserType(TENANT_USER);
        EntityUserUtil.setAll(user);
        return user;
    }

    private RsUserInfo toUserInfoEntity(String userId, String headOrgId, String realName, String userMobile) {
        RsUserInfo userInfo = new RsUserInfo();
        userInfo.setUserId(userId);
        userInfo.setAdminFlag(true);
        userInfo.setOrgId(headOrgId);
        userInfo.setMobile(userMobile);
        userInfo.setRealName(realName);
        EntityUserUtil.setAll(userInfo);
        return userInfo;
    }

    /**
     * 检验租户名称是否重复
     *
     * @param id
     * @param orgName
     */
    private void checkOrgName(String id, String orgName) {
        Organization one = organizationService.lambdaQuery().select(Organization::getId)
                .ne(StringUtils.isNotBlank(id), Organization::getId, id).eq(Organization::getOrgName, orgName).last("limit 1").one();
        if (Objects.nonNull(one)) {
            throw new ResolinkRuntimeException(Message.code("20012"));
        }
    }

    /**
     * 校验 appId 是否已经存在数据库
     *
     * @param id
     * @param cAppId
     * @param bAppId
     */
    private void checkAppIdExist(String id, String cAppId, String bAppId) {
        if (StringUtils.isNotBlank(cAppId) || StringUtils.isNotBlank(bAppId)) {
            Organization organization = organizationService.lambdaQuery().select(Organization::getId).ne(StringUtils.isNotBlank(id), Organization::getId, id)
                    .and(t -> t.eq(Organization::getCAppId, cAppId).or().eq(Organization::getBAppId, bAppId))
                    .last("limit 1").one();
            if (Objects.nonNull(organization)) {
                throw new ResolinkRuntimeException("20021");
            }
        }
    }

    /**
     * 检验租户用户名和手机号是否重复
     *
     * @param userId
     * @param userName
     * @param mobile
     */
    private void checkUserNameAndMobile(String userId, String userName, String mobile, Integer userType) {
        Integer countName = userService.lambdaQuery()
                .ne(StringUtils.isNotBlank(userId), RsUser::getId, userId)
                .eq(Objects.nonNull(userType), RsUser::getUserType, userType)
                .eq(RsUser::getUsername, userName).count();
        Integer countMobile = userInfoService.lambdaQuery()
                .ne(StringUtils.isNotBlank(userId), RsUserInfo::getUserId, userId)
                .eq(RsUserInfo::getMobile, mobile).count();
        if ((countName + countMobile) > 1) {
            throw new ResolinkRuntimeException("40018");
        }
        if (countName > 0) {
            throw new ResolinkRuntimeException(Message.code("40001"));
        }
        if (countMobile > 0) {
            throw new ResolinkRuntimeException(Message.code("40002"));
        }
    }
}
