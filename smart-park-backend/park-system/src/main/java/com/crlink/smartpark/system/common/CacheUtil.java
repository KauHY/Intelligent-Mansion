package com.crlink.smartpark.system.common;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.entity.RsUser;
import com.crlink.smartpark.system.entity.RsUserInfo;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.RsUserService;
import com.crlink.smartpark.system.service.common.UserInfoService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.crlink.smartpark.common.constants.Constants.*;

@Slf4j
@Service
public class CacheUtil {
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RsUserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private EmployeeManageService employeeManageService;

    private static CacheUtil instance;


    public CacheUtil() {
        instance = this;
    }

    /**
     * 获取当前对象
     */
    public static CacheUtil getInstance() {
        return instance;
    }

    @Autowired
    private OrganizationManageService organizationManageService;


    /**
     * 获取当前用户拥有权限的园区ID集合
     * @return Set<String>
     */
    public Set<String> getParkOrgIds() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Object result = null;
        try {
            result = redisUtil.getObject(Constants.Redis.USER_PARK_ORG_IDS.getBuildCode(userId));
        } catch (Exception e) {
            log.info("【查询用户所属园区parkOrgId 出错】:{}", userId, e);
        }
        Set<String> parkOrgIds = ((Set<String>) result);
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            Integer userType = userService.lambdaQuery().select(RsUser::getUserType).eq(RsUser::getId, userId).one().getUserType();
            if (userType == PLATFORM_USER || userType == TENANT_USER) {
                parkOrgIds = organizationManageService.listParkOrgIdsByUserId(userId);
            } else if (userType == WECHAT_TEMP_USER) {
                String mobile = this.queryMobileByUserId(userId);
                parkOrgIds = employeeManageService.queryEmployeeParkOrgId2NameByMobile(mobile).keySet();
            }
            Set<String> finalParkOrgIds = parkOrgIds;
            CompletableFuture.runAsync(() -> {
                try {
                    redisUtil.setObject(Constants.Redis.USER_PARK_ORG_IDS.getBuildCode(userId), finalParkOrgIds);
                } catch (Exception e) {
                    log.info("【缓存用户所属园区parkOrgId 出错】:{}", userId, e);
                }
            });
        }

        return parkOrgIds;
    }

    public Boolean invalidate(String key) {
        return redisUtil.delete(key);
    }

    private String queryMobileByUserId(String userId) {
        String mobile = userInfoService.getOne(Wrappers.<RsUserInfo>lambdaQuery()
                .select(RsUserInfo::getMobile)
                .eq(RsUserInfo::getUserId, userId))
                .getMobile();
        return mobile;
    }

}
