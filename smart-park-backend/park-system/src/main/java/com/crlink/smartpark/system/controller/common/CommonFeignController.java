package com.crlink.smartpark.system.controller.common;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.common.CacheUtil;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.service.common.CommonService;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeService;
import com.crlink.smartpark.system.service.parkmanagement.HouseService;
import com.crlink.smartpark.system.vo.SimpleNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统管理
 *
 * @Description
 * @Created by ZHOUXINJIAN3
 */
@Slf4j
@Api(tags = "内部接口")
@RestController
@RequestMapping("/common_feign")
public class CommonFeignController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * @title 通过ids获取名字
     */
    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.POST)
    public Map<Integer, Map<String, String>> getNamesByIds(@RequestBody List<SimpleNode> list) {
        return commonService.getNamesByIds(list);
    }

    /**
     * 通过类型和ids获取名字
     *
     * @param ids
     * @param typeCode
     */
    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.GET)
    Map<String, String> getNamesByIdsAndType(@RequestParam("ids") Set<String> ids, @RequestParam("typeCode") Integer typeCode) {
        return commonService.getNamesByIdsAndType(ids, typeCode);
    }

    @RequestMapping(value = "/getIdsByNames", method = RequestMethod.GET)
    Map<String, String> getIdsByNamesAndType(@RequestParam(value = "names", required = false) Set<String> names,
                                             @RequestParam("typeCode") Integer typeCode,
                                             @RequestParam(required = false) String parkOrgId) {
        return commonService.getIdsByNamesAndType(names, typeCode, parkOrgId);
    }

    @RequestMapping(value = "/getBuildingHouseNamesByIds", method = RequestMethod.GET)
    Map<String, String> getBuildingHouseNamesByIds(@RequestParam("ids") Set<String> houseIds) {
        Map<String, String> result = houseService.getBuildingHouseNamesByIds(houseIds);
        return result;
    }

    @ApiOperation(value = "根据organizationId 获取名字", tags = "内部接口")
    @PostMapping("/getOrgNamesByOrgIds")
    public Map<String, String> getOrgNamesByOrgIds(@RequestBody Set<String> orgIds) {
        Map<String, String> orgId2NameMap = Collections.emptyMap();
        if (CollectionUtil.isNotEmpty(orgIds)) {
            orgId2NameMap = organizationService.lambdaQuery()
                    .select(Organization::getId, Organization::getOrgName)
                    .in(Organization::getId, orgIds)
                    .list().stream().collect(Collectors.toMap(Organization::getId, Organization::getOrgName));
        }
        return orgId2NameMap;
    }

    @ApiOperation(value = "获取当前用户的园区类型的组织ID", tags = "内部接口")
    @PostMapping("/getCurrentUseParkOrgIds")
    public Set<String> getCurrentUseParkOrgIds() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Set<String> parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
        return parkOrgIds;
    }

    @ApiOperation(value = "获取用户信息", tags = "内部接口")
    @GetMapping("/getUserInfo")
    public Map<String, Map<String, Object>> getUsernameByUserId(@RequestParam Set<String> userIds, @RequestParam Set<String> columns) {
        Map<String, Map<String, Object>> users = userManagementService.queryUserByUserId(userIds, columns);
        return users;
    }

    @ApiOperation(value = "获取用户信息", tags = "内部接口")
    @GetMapping("/getAllParkOrgIdsByOrgId")
    public Set<String> getAllParkOrgIdsByOrgId(@RequestParam("orgId") String orgId) {
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        Set<String> parkOrgIds = organizationManageService.getAllParkOrgIdsByOrgId(orgId, currentUserId);
        return parkOrgIds;
    }


}
