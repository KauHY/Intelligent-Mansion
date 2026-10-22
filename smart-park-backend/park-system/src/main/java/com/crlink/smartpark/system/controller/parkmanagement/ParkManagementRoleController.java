package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.role.CreateRoleReqDTO;
import com.crlink.smartpark.system.dto.role.RoleVO;
import com.crlink.smartpark.system.query.RoleQuery;
import com.crlink.smartpark.system.service.common.RoleManageService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementRoleService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 园区运营管理-角色管理
 * */
@Slf4j
@RestController
@Api(tags = "园区运营管理-角色管理")
@RequestMapping("/parkManagement/role")
public class ParkManagementRoleController {


    @Autowired
    private RoleManageService roleManageService;

    @Autowired
    private ParkManagementRoleService parkManagementRoleService;

    /**
     * 创建用户，角色下拉列表
     *
     * @param roleQuery
     * @return
     */
    @ApiOperation(value = "创建用户，角色下拉列表", tags = "用户管理")
    @PostMapping("/listRoles")
    public ResponseResult<PageInfo<DictVO>> roleListByPage(@RequestBody RoleQuery roleQuery) {
        PageInfo<DictVO> page = parkManagementRoleService.listPlatformRoles(roleQuery);
        return ResponseResult.success(page);
    }


    /**
     * 创建角色
     *
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "创建角色", tags = "角色管理")
    @PostMapping("/save")
    public ResponseResult<Boolean> createRole(@RequestBody CreateRoleReqDTO createRoleReqDTO) {
        createRoleReqDTO.setId(null);
        Boolean result = parkManagementRoleService.createOrUpdateSysRole(createRoleReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 编辑角色
     *
     * @param createRoleReqDTO
     * @return
     */
    @ApiOperation(value = "编辑角色", tags = "角色管理")
    @PostMapping("/update")
    public ResponseResult<Boolean> updateRole(@RequestBody CreateRoleReqDTO createRoleReqDTO) {
        Boolean result = parkManagementRoleService.createOrUpdateSysRole(createRoleReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 删除角色
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "删除角色", tags = "角色管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = roleManageService.deleteRole(id);
        return ResponseResult.success(result);
    }

    /**
     * 编辑状态
     *
     * @return 系统角色实体
     */
    @ApiOperation(value = "编辑状态", tags = "角色管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> findById(@RequestBody UpdateStatusReqDTO reqDTO) {
        AssertUtil.requireNotNull(reqDTO.getId(), "10007");
        boolean result = roleManageService.updateStatus(reqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 分页查询
     *
     * @param roleQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "分页查询", tags = "角色管理")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<RoleVO>> pageRoles(@RequestBody RoleQuery roleQuery) {
        PageInfo<RoleVO> page = parkManagementRoleService.pageRoles(roleQuery);
        return ResponseResult.success(page);
    }

}

