package com.crlink.smartpark.system.controller.platform;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.role.CreateRoleReqDTO;
import com.crlink.smartpark.system.dto.role.RoleVO;
import com.crlink.smartpark.system.query.RoleQuery;
import com.crlink.smartpark.system.service.common.RoleManageService;
import com.crlink.smartpark.system.service.platform.PlatformRoleService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 平台-系统角色管理
 *
 * @Description
 * @Created by ZHOUXINJIAN3
 */
@Slf4j
@RestController
@Api(tags = "平台-系统角色管理")
@RequestMapping("/platform/role")
public class PlatformRoleController {
    @Autowired
    private PlatformRoleService platformRoleService;

    @Autowired
    private RoleManageService roleManageService;


    /**
     * 创建用户，角色下拉列表
     */
    @ApiOperation(value = "创建用户，角色下拉列表", tags = "用户管理")
    @PostMapping("/listRoles")
    public ResponseResult<PageInfo<DictVO>> listRoleDicts(@RequestBody RoleQuery roleQuery) {
        PageInfo<DictVO> page = platformRoleService.listPlatformRoles(roleQuery);
        return ResponseResult.success(page);
    }


    /**
     * 创建系统角色
     */
    @ApiOperation(value = "创建系统角色", tags = "角色管理")
    @PostMapping("/save")
    public ResponseResult<Boolean> createRole(@RequestBody @Valid CreateRoleReqDTO createRoleReqDTO) {
        createRoleReqDTO.setId(null);
        Boolean result = platformRoleService.createOrUpdateSysRole(createRoleReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 编辑系统角色
     */
    @ApiOperation(value = "编辑系统角色", tags = "角色管理")
    @PostMapping("/update")
    public ResponseResult<Boolean> updateRole(@RequestBody @Valid CreateRoleReqDTO createRoleReqDTO) {
        Boolean result = platformRoleService.createOrUpdateSysRole(createRoleReqDTO);
        return ResponseResult.success(result);
    }


    /**
     * 删除系统角色
     */
    @ApiOperation(value = "删除系统角色", tags = "角色管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = roleManageService.deleteRole(id);
        return ResponseResult.success(result);
    }

    /**
     * 编辑角色状态
     */
    @ApiOperation(value = "编辑角色状态", tags = "角色管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@Valid @RequestBody UpdateStatusReqDTO reqDTO) {
        AssertUtil.requireNotNull(reqDTO.getId(), "10007");
        boolean result = roleManageService.updateStatus(reqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 角色分页查询
     */
    @ApiOperation(value = "角色分页查询", tags = "角色管理")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<RoleVO>> pageRoles(@RequestBody RoleQuery roleQuery) {
        PageInfo<RoleVO> page = platformRoleService.pageRoles(roleQuery);
        return ResponseResult.success(page);
    }

}
