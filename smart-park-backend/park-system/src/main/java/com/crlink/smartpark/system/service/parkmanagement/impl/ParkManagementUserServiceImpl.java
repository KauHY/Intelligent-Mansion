package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.SmsConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.SendSmsUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.user.RsAuthLoginDTO;
import com.crlink.smartpark.system.dto.user.RsUserCreateDTO;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.dto.user.UserReqDTO;
import com.crlink.smartpark.system.entity.RsUser;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.RsUserService;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementUserService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.Constants.TENANT_USER;

@Service
public class ParkManagementUserServiceImpl implements ParkManagementUserService {
    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private RsUserService userService;

    @Autowired
    private OrganizationManageService organizationManageService;


    @Override
    public RsUserLoginVO checkTenantLogin(RsAuthLoginDTO reqDto) {
        return userManagementService.checkUser(TENANT_USER, reqDto);
    }

    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ORGANIZATION, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean saveTenantUser(RsUserCreateDTO userReq) {
        //校验用户名不重复
        checkName(null, userReq.getUsername());
        userReq.setUserType(TENANT_USER);
        Boolean result = userManagementService.saveUserInfo(userReq);
        return result;
    }

    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ORGANIZATION, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean updateRsUser(UserReqDTO rsUserReq) {
        String userId = rsUserReq.getUserId();
        if (StringUtils.isBlank(userId)) {
            throw new ResolinkRuntimeException("10007");
        }
        if (userManagementService.judgeAdminUser(userId)) {
            throw new ResolinkRuntimeException("40022");
        }
        checkName(userId, rsUserReq.getUsername());
        Boolean result = userManagementService.updateUserInfo(rsUserReq);
        return result;
    }


    @Override
    public PageInfo<RsUserInfoVO> listUserPage(RsUserQueryDTO rsUserQueryDTO) {
        Set<String> orgIds = organizationManageService.listOrgIdsByUserId(SystemUtil.getInstance().getCurrentUserId());
        if (CollectionUtil.isEmpty(orgIds)) {
            return new PageInfo<>();
        }
        return userService.listPage(orgIds, rsUserQueryDTO);
    }

    @Override
    public PageInfo<DictVO> listDictUsers(BaseQuery<String> roleQuery) {
        Set<String> orgIds = organizationManageService.listOrgIdsByUserId(SystemUtil.getInstance().getCurrentUserId());
        PageInfo<DictVO> pageInfo = userManagementService.listDictUsers(TENANT_USER, roleQuery, orgIds);
        return pageInfo;
    }

    public void checkName(String id, String name) {
        int countByName = userService.count(Wrappers.<RsUser>query().lambda().eq(RsUser::getUserType, TENANT_USER).ne(StringUtils.isNotBlank(id), RsUser::getId, id).eq(RsUser::getUsername, name));
        if (countByName > 0) {
            throw new ResolinkRuntimeException("40001");
        }
    }
}
