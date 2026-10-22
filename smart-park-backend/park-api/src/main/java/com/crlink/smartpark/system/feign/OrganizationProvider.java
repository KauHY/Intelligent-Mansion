package com.crlink.smartpark.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * <p>
 * 组织机构 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/parkManagement/organization", contextId = "OrganizationProvider")
public interface OrganizationProvider {

    /**
     * 获取用户权限的园区id集合
     *
     * @return
     */
    @GetMapping("/getUserParkOrgIds")
    Set<String> getUserParkOrgIds();

    /**
     * 获取租户的园区ID集合
     *
     * @param headOrgId
     * @return
     */
    @GetMapping("/getTenantParkOrgIds")
    Set<String> getTenantParkOrgIds(@RequestParam("headOrgId") String headOrgId);


}
