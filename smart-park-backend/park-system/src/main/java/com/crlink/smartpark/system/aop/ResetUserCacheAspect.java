package com.crlink.smartpark.system.aop;

import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@Aspect
@Slf4j
public class ResetUserCacheAspect {


    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserInfoService rsUserInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.crlink.smartpark.system.aop.ResetUserCache)")
    private void pointcut() {
    }


    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        CompletableFuture.runAsync(() -> {
            Set<String> orgIds = organizationService.getAllOrgIdByHeadOrgId(headOrgId);
            Collection<String> userIds = rsUserInfoService.queryUserIdByOrgIds(orgIds);
            userIds.forEach(userId -> {
                redisTemplate.delete(Constants.Redis.USER_PARK_ORG_IDS.getBuildCode(userId));
            });
            log.info("【重置角色组织数据缓存】:{}", userIds);
        });

    }

}
