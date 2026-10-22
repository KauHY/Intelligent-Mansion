package com.crlink.smartpark.system.aop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.RsUserInfo;
import com.crlink.smartpark.system.entity.RsUserRoleRelation;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.RsUserRoleRelationService;
import com.crlink.smartpark.system.service.common.UserInfoService;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.PLATFORM_ORG_ID;

@Component
@Aspect
@Slf4j
public class ResetUserInfoCacheAspect {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserInfoService rsUserInfoService;

    @Autowired
    private RsUserRoleRelationService userRoleRelationService;

    @Autowired
    private OrganizationService organizationService;

    @Pointcut("@annotation(com.crlink.smartpark.system.aop.ResetUserInfoCache)")
    private void pointcut() {
    }

    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        String tokenId = SystemUtil.getInstance().getCurrentTokenId();
        String userId = SystemUtil.getInstance().getCurrentUserId();

        String key = Constants.Redis.USER_INFO_PREFIX.getBuildCode(tokenId);
        RsUserInfo rsUserInfo = rsUserInfoService.getOne(Wrappers.<RsUserInfo>lambdaQuery().eq(RsUserInfo::getUserId, userId));
        String roleIds = userRoleRelationService.lambdaQuery().select(RsUserRoleRelation::getRoleId).eq(RsUserRoleRelation::getUserId, userId).list()
                .parallelStream().map(RsUserRoleRelation::getRoleId).collect(Collectors.joining(","));
        RsUserLoginVO sysUser = new RsUserLoginVO();
        sysUser.setOrgId(rsUserInfo.getOrgId());
        String orgId = rsUserInfo.getOrgId();
        String headOrgId = orgId;
        if (!Objects.equals(orgId, PLATFORM_ORG_ID)) {
            headOrgId = organizationService.lambdaQuery().select(Organization::getHeadOrgId).eq(Organization::getId, orgId).one().getHeadOrgId();
        }
        sysUser.setHeadOrgId(headOrgId);
        //sysUser.setRoleIds(roleIds);
        sysUser.setRealName(rsUserInfo.getRealName());
        sysUser.setMobile(rsUserInfo.getMobile());
        redisUtil.setObject(key, sysUser, Constants.OVERTIME, TimeUnit.MINUTES);
        log.info("【重置用户缓存】:{}", sysUser);
    }
}
