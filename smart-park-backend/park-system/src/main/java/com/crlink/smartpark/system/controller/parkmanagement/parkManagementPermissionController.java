package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.PermissionReqDTO;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.RoleManageService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementMenuService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 园区运营管理-权限分配
 * */
@Slf4j
@RestController
@Api(tags = "园区运营管理-权限分配")
@RequestMapping("/parkManagement/permission")
public class parkManagementPermissionController {


    @Autowired
    private ParkManagementPermissionService parkManagementPermissionService;

    @Autowired
    private ParkManagementMenuService parkManagementMenuService;

    @Autowired
    private OrganizationManageService organizationManageService;


    @Autowired
    private RoleManageService roleManageService;

    /**
     * 数据权限分配
     */
    @ApiOperation(value = " 数据权限分配", tags = "角色管理-权限分配")
    @PostMapping("/enable/data")
    public ResponseResult<Boolean> dataPermission(@RequestBody PermissionReqDTO permissionDTO) {
        Boolean result = parkManagementPermissionService.dataPermission(permissionDTO);

        return ResponseResult.success(result);
    }

    /**
     * 角色菜单权限分配
     */
    @ApiOperation(value = "角色菜单权限分配", tags = "角色管理")
    @PostMapping("/enable/menu")
    public ResponseResult<Boolean> enableMenuPermission(@RequestBody PermissionReqDTO permissionDTO) {
        roleManageService.cannotUpdateOrDeleteAdminRole(permissionDTO.getRoleId());
        Boolean result = parkManagementMenuService.enableParkMenus(permissionDTO.getRoleId(), permissionDTO.getDestIds());
        return ResponseResult.success(result);
    }

    /**
     * 回显数据权限
     */
    @ApiOperation(value = "回显数据权限", tags = "角色管理-权限分配")
    @GetMapping("/echoData")
    public ResponseResult<Set<String>> echoData(String roleId) {
        Set<String> result = parkManagementPermissionService.echoData(roleId);
        return ResponseResult.success(result);
    }

    /**
     * 回显菜单权限
     */
    @ApiOperation(value = "回显菜单权限", tags = "角色管理-权限分配")
    @GetMapping("/echoMenu")
    public ResponseResult<Set<String>> echoMenu(String roleId) {
        //Set<String> result = parkManagementPermissionService.echoParkMenu(roleId);
        Set<String> result = parkManagementMenuService.echoParkMenu(roleId);
        return ResponseResult.success(result);
    }

}

