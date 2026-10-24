package com.crlink.smartpark.system.service.platform.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.system.dto.user.*;
import com.crlink.smartpark.system.entity.RsUser;
import com.crlink.smartpark.system.service.common.RsUserService;
import com.crlink.smartpark.system.service.common.UserInfoService;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.service.platform.PlatformUserService;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.Constants.PLATFORM_ORG_ID;
import static com.crlink.smartpark.common.constants.Constants.PLATFORM_USER;

@Service
public class PlatformUserServiceImpl implements PlatformUserService {

    @Autowired
    private RsUserService userService;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private UserInfoService rsUserInfoService;


    @Override
    @Transactional
    public Boolean savePlatformSysRsUser(RsUserCreateDTO userReq) {
        //校验用户名不重复
        checkName(null, userReq.getUsername());
        Boolean result = userManagementService.saveUserInfo(userReq);
        return result;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_GROUP_ORGANIZATION, CACHE_GROUP_USER, CACHE_GROUP_ROLE, CACHE_GROUP_MENU}, allEntries = true)
    public Boolean updatePlatformSysUser(SysUserReqDTO userUpdateDTO) {
        String userId = userUpdateDTO.getUserId();
        Boolean isAdmin = userManagementService.judgeAdminUser(userId);
        if (isAdmin) {
            userUpdateDTO.setUsername(null);
            userUpdateDTO.setRoleIds(null);
        }
        checkName(userId, userUpdateDTO.getUsername());
        UserReqDTO userReqDTO = new UserReqDTO();
        BeanUtils.copyProperties(userUpdateDTO, userReqDTO);
        userReqDTO.setOrgId(PLATFORM_ORG_ID);
        userManagementService.updateUserInfo(userReqDTO);
        return true;
    }

    @Override
    public RsUserLoginVO checkPlatformLogin(RsAuthLoginDTO rsAuthLoginDTO) {
        return userManagementService.checkUser(PLATFORM_USER, rsAuthLoginDTO);
    }

    @Override
    public PageInfo<RsUserInfoVO> listSysUserPage(RsUserQueryDTO rsUserQueryDTO) {
        return userService.listPage(Sets.newHashSet(PLATFORM_ORG_ID), rsUserQueryDTO);
    }

    @Override
    public Boolean removeUserById(String userId) {
        if (userManagementService.judgeAdminUser(userId)) {
            throw new ResolinkRuntimeException("40021");
        }
        return userManagementService.removeUserById(userId);
    }

    private void checkName(String id, String name) {
        int countByName = userService.count(Wrappers.<RsUser>query().lambda().eq(RsUser::getUserType, PLATFORM_USER).ne(StringUtils.isNotBlank(id), RsUser::getId, id).eq(RsUser::getUsername, name));
        if (countByName > 0) {
            throw new ResolinkRuntimeException("40001");
        }
    }
}
