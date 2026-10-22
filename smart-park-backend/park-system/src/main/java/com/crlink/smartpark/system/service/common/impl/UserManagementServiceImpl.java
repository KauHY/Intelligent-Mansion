package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.SmsConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.user.*;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.service.common.*;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.CACHE_GROUP_EMPLOYEE;
import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.Constants.Redis.USER_ACCESS_TOKEN_PREFIX;
import static com.crlink.smartpark.common.constants.Constants.Redis.USER_INFO_PREFIX;

@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private RsUserService userService;
    @Autowired
    private RsUserRoleRelationService userRoleRelationService;
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RsUserRoleRelationService roleRelationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_GROUP_EMPLOYEE, allEntries = true)
    public Boolean saveUserInfo(RsUserCreateDTO userReq) {
        //校验手机号不重复
        int countByMobile = userInfoService.count(Wrappers.<RsUserInfo>query().lambda().eq(RsUserInfo::getOrgId, userReq.getOrgId()).eq(RsUserInfo::getMobile, userReq.getMobile()));
        if (countByMobile > 0) {
            throw new ResolinkRuntimeException("40002");
        }
        // TODO: 密码生成即发送
        String pwd = MyStrUtil.genRandomNum();
        //密码加密,密码前、后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(pwd + WEB_SALT);
        encodePwd = StringEncodeUtil.Str2SHA256(encodePwd + SYSTEM_SALT);
        RsUser rsUser = new RsUser();
        rsUser.setUsername(userReq.getUsername());
        rsUser.setPassword(encodePwd);
        rsUser.setUserType(userReq.getUserType());
        EntityUserUtil.setAll(rsUser);
        if (!userService.save(rsUser)) {
            throw new ResolinkRuntimeException("10002");
        }
        RsUserInfo rsUserInfo = new RsUserInfo();
        rsUserInfo.setUserId(rsUser.getId());
        rsUserInfo.setOrgId(userReq.getOrgId());
        rsUserInfo.setAdminFlag(false);
        rsUserInfo.setRealName(userReq.getRealName());
        rsUserInfo.setMobile(userReq.getMobile());
        EntityUserUtil.setAll(rsUserInfo);
        userInfoService.save(rsUserInfo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", rsUser.getUsername());
        jsonObject.put("password", pwd);
        CompletableFuture.runAsync(() -> {
            SendSmsUtil.sendSms(SmsConstant.SIGN_NAME, SmsConstant.PARK_PWD_CODE, userReq.getMobile(),
                    JSONUtil.toJsonStr(jsonObject));
        });
        //关联角色
        List<RsUserRoleRelation> relations = userReq.getRoleIds().parallelStream().map(t -> {
            RsUserRoleRelation userRole = new RsUserRoleRelation();
            userRole.setUserId(rsUser.getId());
            userRole.setRoleId(t);
            return userRole;
        }).collect(Collectors.toList());
        return userRoleRelationService.saveBatch(relations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserInfo(UserReqDTO userReq) {
        String curUserId = SystemUtil.getInstance().getCurrentUserId();
        int countByMobile = userInfoService.count(Wrappers.<RsUserInfo>query().lambda().ne(RsUserInfo::getUserId, userReq.getUserId()).eq(RsUserInfo::getOrgId, userReq.getOrgId()).eq(RsUserInfo::getMobile, userReq.getMobile()));
        if (countByMobile > 0) {
            throw new ResolinkRuntimeException("40002");
        }
        userService.update(new UpdateWrapper<RsUser>().lambda().set(RsUser::getUsername, userReq.getUsername()).set(RsUser::getUpdateBy, curUserId).eq(RsUser::getId, userReq.getUserId()));
        UpdateWrapper<RsUserInfo> updateUserInfoWrapper = new UpdateWrapper<>();
        updateUserInfoWrapper.lambda()
                .set(RsUserInfo::getMobile, userReq.getMobile())
                .set(RsUserInfo::getRealName, userReq.getRealName())
                .set(RsUserInfo::getOrgId, userReq.getOrgId())
                .set(RsUserInfo::getUpdateBy, curUserId)
                .eq(RsUserInfo::getUserId, userReq.getUserId());
        userInfoService.update(updateUserInfoWrapper);
        userRoleRelationService.remove(Wrappers.<RsUserRoleRelation>lambdaQuery().eq(RsUserRoleRelation::getUserId, userReq.getUserId()));
        //关联角色
        List<RsUserRoleRelation> relations = userReq.getRoleIds().parallelStream().map(t -> {
            RsUserRoleRelation userRole = new RsUserRoleRelation();
            userRole.setUserId(userReq.getUserId());
            userRole.setRoleId(t);
            return userRole;
        }).collect(Collectors.toList());
        userRoleRelationService.saveBatch(relations);
        //如果更改的用户时当前登录的用户，重置用户信息
        CompletableFuture.runAsync(() -> {
            if (Objects.equals(curUserId, userReq.getUserId())) {
                RsUserLoginVO userInfo = SystemUtil.getInstance().getCurrentUserInfo();
                userInfo.setUsername(userReq.getUsername());
                userInfo.setRealName(userReq.getRealName());
                userInfo.setMobile(userReq.getMobile());
                userInfo.setOrgId(userReq.getOrgId());
                if (CollectionUtil.isNotEmpty(userReq.getRoleIds())) {
                    List<DictVO> userRoles = roleService.lambdaQuery().select(Role::getId, Role::getRoleName).in(Role::getId, userReq.getRoleIds()).list()
                            .parallelStream().map(role -> new DictVO(role.getRoleName(), role.getId())).collect(Collectors.toList());
                    userInfo.setUserRoles(userRoles);
                }
                String key = USER_INFO_PREFIX.getBuildCode(SystemUtil.getInstance().getCurrentTokenId());
                redisUtil.setObject(key, userInfo, Constants.OVERTIME, TimeUnit.MINUTES);
                //缓存用户的所有园区ID
                //Set<String> parkOrgIds = organizationManageService.listParkOrgIdsByUserId(curUserId);
                //redisUtil.setObject(Constants.Redis.USER_PARK_ORG_IDS.getBuildCode(curUserId), parkOrgIds);
            }
        });
        return true;

    }

    @Override
    public RsUserLoginVO checkUser(int userType, RsAuthLoginDTO rsUser) {
        String oldValidateCode = (String) redisUtil.getObject(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(rsUser.getValidateCodeKey()));
        if (!rsUser.getValidateCode().equalsIgnoreCase(oldValidateCode)) {
            redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(rsUser.getValidateCodeKey()));
            throw new ResolinkRuntimeException("40004");
        }
        redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(rsUser.getValidateCodeKey()));
        // 密码后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(rsUser.getPassword() + SYSTEM_SALT);
        RsUser user = userService.getOne(Wrappers.<RsUser>query().lambda().eq(RsUser::getUsername, rsUser.getUsername()).eq(RsUser::getPassword, encodePwd).eq(RsUser::getUserType, userType));
        // 校验用户
        if (user == null) {
            throw new ResolinkRuntimeException("40005");
        }
        //租户id
        String headOrgId = PLATFORM_ORG_ID;
        if (userType == TENANT_USER) {
            String orgId = userInfoService.lambdaQuery().select(RsUserInfo::getOrgId).eq(RsUserInfo::getUserId, user.getId()).one().getOrgId();
            headOrgId = Optional.ofNullable(organizationService.lambdaQuery().select(Organization::getHeadOrgId).eq(Organization::getId, orgId).one())
                    .map(Organization::getHeadOrgId).orElseThrow(() -> new ResolinkRuntimeException("20023"));
            //校验租户状态
            if (Status.DISABLE.code.equals(organizationService.id2ValueMap(Sets.newHashSet(headOrgId), Organization::getStatus).get(headOrgId))) {
                throw new ResolinkRuntimeException("20013");
            }
        }
        // 校验用户状态
        if (!Status.NORMAL.code.equals(user.getStatus())) {
            throw new ResolinkRuntimeException("40006");
        }
        //校验用户角色,
        Set<String> roleIds = roleRelationService.list(Wrappers.<RsUserRoleRelation>lambdaQuery().select(RsUserRoleRelation::getRoleId).eq(RsUserRoleRelation::getUserId, user.getId()))
                .stream().map(RsUserRoleRelation::getRoleId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new ResolinkRuntimeException("40007");
        }
        RsUserInfo rsUserInfo = userInfoService.getOne(Wrappers.<RsUserInfo>lambdaQuery().eq(RsUserInfo::getUserId, user.getId()));
        RsUserLoginVO sysUser = MyBeanUtils.copy(user, RsUserLoginVO::new, "password");
        sysUser.setOrgId(rsUserInfo.getOrgId());
        sysUser.setRealName(rsUserInfo.getRealName());
        sysUser.setMobile(rsUserInfo.getMobile());
        sysUser.setHeadOrgId(headOrgId);

        //已经判断用角色不能为空
        List<DictVO> userRoles = roleService.id2ValueMap(roleIds, Role::getRoleName).entrySet()
                .parallelStream().map(entry -> new DictVO((String) entry.getValue(), entry.getKey())).collect(Collectors.toList());
        sysUser.setUserRoles(userRoles);
        String tokenId = IdUtil.simpleUUID();
        sysUser.setTokenId(tokenId);
        // 生成token
        String accessToken = JwtBaseUtil.generateUserToken(sysUser.getId(), rsUserInfo.getRealName(), headOrgId, sysUser.getTokenId(), userType);
        //String refreshToken = accessToken;
        sysUser.setAccessToken(accessToken);
        //sysUser.setRefreshToken(refreshToken);
        // 存入redis
        CompletableFuture.runAsync(() -> {
            redisUtil.setObject(Constants.Redis.USER_ACCESS_TOKEN_PREFIX.getBuildCode(tokenId), accessToken, Constants.OVERTIME, TimeUnit.MINUTES);
            //redisUtil.setObject(Constants.Redis.USER_REFRESH_TOKEN_PREFIX.getBuildCode(tokenId), refreshToken, Constants.OVERTIME_REFRESH, TimeUnit.MINUTES);
            redisUtil.setObject(Constants.Redis.USER_INFO_PREFIX.getBuildCode(tokenId), sysUser, Constants.OVERTIME, TimeUnit.MINUTES);
        });
        return sysUser;
    }

    private RsUserLoginVO setUserInfo(String userId, String username, String headOrgId) {
        RsUserInfo rsUserInfo = userInfoService.getOne(Wrappers.<RsUserInfo>lambdaQuery().eq(RsUserInfo::getUserId, userId));
        RsUserLoginVO sysUser = new RsUserLoginVO();
        sysUser.setId(userId);
        sysUser.setUsername(username);
        sysUser.setRealName(rsUserInfo.getRealName());
        sysUser.setMobile(rsUserInfo.getMobile());
        sysUser.setOrgId(rsUserInfo.getOrgId());
        sysUser.setHeadOrgId(headOrgId);
        return sysUser;
    }

    @Override
    public Boolean updateStatus(UpdateStatusReqDTO status) {
        if (this.judgeAdminUser(status.getId())) {
            throw new ResolinkRuntimeException("40022");
        }
        RsUser user = new RsUser();
        user.setId(status.getId());
        user.setStatus(status.getStatus());
        EntityUserUtil.setUpdateBy(user);
        return userService.updateById(user);
    }

    @Override
    public Boolean getResetPwdSmsCode(BusinessUserMobileDTO reqDTO) {
        String code = String.format("%06d", new Random().nextInt(999999));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        boolean flag = SendSmsUtil.sendSms(SmsConstant.SIGN_NAME, SmsConstant.PARK_TEMPLATE_CODE, reqDTO.getMobile(),
                JSONUtil.toJsonStr(jsonObject));
        if (flag) {
            String smsCodeKey = Constants.Redis.RESET_PWD_CODE_PREFIX.getBuildCode(reqDTO.getMobile());
            redisUtil.setObject(smsCodeKey, code, 15L, TimeUnit.MINUTES);
        }
        return flag;
    }

    @Override
    public String validateIdentity(RsUserValidateDTO userPwdResetDTO) {
        // 验证码校验
        String oldValidateCode = (String) redisUtil.getObject(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(userPwdResetDTO.getValidateCodeKey()));
        redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(userPwdResetDTO.getValidateCodeKey()));
        AssertUtil.requireTrue(userPwdResetDTO.getValidateCode().equalsIgnoreCase(oldValidateCode), "40004");
        // 手机验证码验证
        String smsCode = (String) redisUtil
                .getObject(Constants.Redis.RESET_PWD_CODE_PREFIX.getBuildCode(userPwdResetDTO.getMobilePhone()));
        AssertUtil.requireTrue(userPwdResetDTO.getTelValidateCode().equals(smsCode), "40012");
        RsUser rsUser = userService.getOne(Wrappers.<RsUser>lambdaQuery().eq(RsUser::getUsername, userPwdResetDTO.getUsername()), false);
        AssertUtil.requireNotNull(rsUser, "40003");
        RsUserInfo userInfo = userInfoService.getOne(Wrappers.<RsUserInfo>lambdaQuery().select(RsUserInfo::getId, RsUserInfo::getMobile).eq(RsUserInfo::getUserId, rsUser.getId()), false);
        AssertUtil.requireTrue(userPwdResetDTO.getMobilePhone().equals(userInfo.getMobile()), "40013");
        String validateData = SimpleJwtUtil.signToken(userPwdResetDTO.getUsername());
        return validateData;
    }

    @Override
    public Boolean resetPwdForForget(ResetPwdForForgetDTO resetPwdForForgetDTO) {
        AssertUtil.requireTrueOrParamExp(resetPwdForForgetDTO.getNewPassword().equals(resetPwdForForgetDTO.getNewPasswordAgain()));
        String username = SimpleJwtUtil.checkToken(resetPwdForForgetDTO.getValidateToken());
        //密码加密,密码后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(resetPwdForForgetDTO.getNewPassword() + SYSTEM_SALT);
        return userService.update(Wrappers.<RsUser>lambdaUpdate().set(RsUser::getPassword, encodePwd).eq(RsUser::getUsername, username));
    }

    @Override
    public Boolean updatePwd(UpdatePwdDTO updatePwdDTO) {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        //密码加密,密码后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(updatePwdDTO.getOldPassword() + SYSTEM_SALT);
        //校验用户原密码是否正确
        int countUser = userService.count(Wrappers.<RsUser>query().lambda().eq(RsUser::getId, userId).eq(RsUser::getPassword, encodePwd));
        if (countUser <= 0) {
            throw new ResolinkRuntimeException("40014");
        }
        if (!updatePwdDTO.getNewPassword().equals(updatePwdDTO.getNewPasswordAgain())) {
            throw new ResolinkRuntimeException("40015");
        }
        String encodeNewPwd = StringEncodeUtil.Str2SHA256(updatePwdDTO.getNewPassword() + SYSTEM_SALT);
        return passwordCommit(userId, encodeNewPwd);
    }

    @Override
    public Boolean resetPwd(RsUserPwdResetDTO rsUser) {
        //校验用户是否存在
        int count = userService.count(Wrappers.<RsUser>query().lambda().eq(RsUser::getId, rsUser.getUserId()));
        if (count <= 0) {
            throw new ResolinkRuntimeException("40003");
        }
        //密码加密,后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(rsUser.getNewPassword() + SYSTEM_SALT);
        return passwordCommit(rsUser.getUserId(), encodePwd);

    }

    private Boolean passwordCommit(String userId, String encodePwd) {
        RsUser user = new RsUser();
        user.setId(userId);
        user.setPassword(encodePwd);
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        user.setUpdateBy(currentUserId);
        return userService.updateById(user);
    }


    @Override
    public Boolean logout() {
        return redisUtil.delete(USER_ACCESS_TOKEN_PREFIX.getBuildCode(SystemUtil.getInstance().getCurrentTokenId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeUserById(String userId) {
        try {
            if (this.judgeAdminUser(userId)) {
                throw new ResolinkRuntimeException("40021");
            }
            userRoleRelationService.remove(Wrappers.<RsUserRoleRelation>lambdaQuery().eq(RsUserRoleRelation::getUserId, userId));
            userInfoService.remove(Wrappers.<RsUserInfo>lambdaQuery().eq(RsUserInfo::getUserId, userId));
            userService.removeById(userId);
        } catch (Exception e) {
            log.error("【删除用户失败,userid:{}】", userId, e);
            throw new ResolinkRuntimeException("40016");
        }
        return true;
    }

    @Override
    public Boolean judgeAdminUser(String userId) {
        RsUserInfo userInfo = userInfoService.getOne(Wrappers.<RsUserInfo>lambdaQuery().select(RsUserInfo::getUserId, RsUserInfo::getAdminFlag).eq(RsUserInfo::getUserId, userId), false);
        if (userInfo != null && userInfo.getAdminFlag()) {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<DictVO> listDictUsers(int userType, BaseQuery<String> userQuery, Set<String> orgIds) {
        if (CollectionUtil.isEmpty(orgIds)) {
            return new PageInfo<>();
        }
        String userName = userQuery.getParam();
        userQuery.setPageSize(DICT_PAGE_SIZE);
        userQuery.startPage();
        LambdaQueryWrapper<RsUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(RsUser::getId, RsUser::getUsername);
        queryWrapper.eq(RsUser::getUserType, userType);
        queryWrapper.eq(RsUser::getStatus, Constants.Status.NORMAL.getCode());
        queryWrapper.like(StringUtils.isNotBlank(userName), RsUser::getUsername, userName);
//        Set<String> userIds = userInfoService.lambdaQuery().select(RsUserInfo::getUserId).in(RsUserInfo::getOrgId, orgIds).list().parallelStream().map(RsUserInfo::getUserId).collect(Collectors.toSet());
//        queryWrapper.in(RsUser::getId, userIds);
        List<RsUserInfo> list = userInfoService.lambdaQuery().select(RsUserInfo::getId, RsUserInfo::getUserId, RsUserInfo::getRealName)
                .in(RsUserInfo::getOrgId, orgIds).list();
        Map<String, String> userInfoMap = list.parallelStream().collect(Collectors.toMap(RsUserInfo::getUserId, RsUserInfo::getRealName));
        queryWrapper.in(RsUser::getId, userInfoMap.keySet());
        List<RsUser> users = userService.list(queryWrapper);
        List<DictVO> dictVOList = users.parallelStream().filter(t -> Objects.nonNull(userInfoMap.get(t.getId())))
                .map(u -> new DictVO(userInfoMap.get(u.getId()), u.getId())).collect(Collectors.toList());
//        List<DictVO> dictVOList = users.parallelStream().map(u -> new DictVO(u.getUsername(), u.getId())).collect(Collectors.toList());
        //PageInfo<DictVO> result = new PageInfo<>(dictVOList);
        return userQuery.createPageInfo(dictVOList);
    }

    @Override
    public Map<String, Map<String, Object>> queryUserByUserId(Set<String> userIds, Set<String> queryColumns) {
        if (CollectionUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        String userSqlSelect = TableInfoHelper.getTableInfo(RsUser.class).getAllSqlSelect();
        List<String> userAllColumns = Arrays.stream(StringUtils.split(userSqlSelect, COMMA_MARK)).collect(Collectors.toList());

        String userInfoSqlSelect = TableInfoHelper.getTableInfo(RsUserInfo.class).getAllSqlSelect();
        List<String> userInfoAllColumns = Arrays.stream(StringUtils.split(userInfoSqlSelect, COMMA_MARK)).collect(Collectors.toList());

        Map<String, Map<String, Object>> result = Maps.newHashMap();
        Set<String> userQueryColumns = Sets.newHashSet();
        Set<String> userInfoQueryColumns = Sets.newHashSet();
        queryColumns.forEach(c -> {
            if (userAllColumns.contains(c)) {
                userQueryColumns.add(c);
            } else if (userInfoAllColumns.contains(c)) {
                userInfoQueryColumns.add(c);
            }
        });
        if (CollectionUtil.isEmpty(userQueryColumns) && CollectionUtil.isEmpty(userInfoQueryColumns)) {
            return Collections.emptyMap();
        }
        if (CollectionUtil.isNotEmpty(userQueryColumns)) {
            userQueryColumns.add("id");
            QueryWrapper<RsUser> userQuery = new QueryWrapper<>();
            userQuery.select(userQueryColumns.toArray(new String[userQueryColumns.size()])).in("id", userIds);
            List<Map<String, Object>> users = userService.listMaps(userQuery);
            users.parallelStream().forEach(t -> {
                Map<String, Map<String, Object>> re = Maps.newHashMap();
                result.put((String) t.get("id"), t);

            });
        }
        if (CollectionUtil.isNotEmpty(userInfoQueryColumns)) {
            QueryWrapper<RsUserInfo> userInfoQuery = new QueryWrapper<>();
            userInfoQueryColumns.add("user_id");
            userInfoQuery.select(userInfoQueryColumns.toArray(new String[userInfoQueryColumns.size()])).in("user_id", userIds);
            List<Map<String, Object>> userInfos = userInfoService.listMaps(userInfoQuery);
            userInfos.parallelStream().forEach(t -> {
                String userId = (String) t.get("user_id");
                Map<String, Object> userMap = result.get(userId);
                if (userMap != null) {
                    t.remove("user_id");
                    userMap.putAll(t);
                } else {
                    result.put(userId, t);
                }
            });
        }
        return result;
    }
}
