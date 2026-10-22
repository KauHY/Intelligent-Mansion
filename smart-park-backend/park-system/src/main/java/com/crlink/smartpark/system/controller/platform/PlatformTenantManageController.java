package com.crlink.smartpark.system.controller.platform;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.aop.ResetUserCache;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.platform.TenantOrganizationVO;
import com.crlink.smartpark.system.dto.platform.TenantReqDTO;
import com.crlink.smartpark.system.dto.user.RsUserPwdResetDTO;
import com.crlink.smartpark.system.query.TenantQuery;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.service.platform.PlatformTenantOrganizationService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 平台-租户管理
 *
 * @Description
 * @Date 2021/6/16
 */
@Slf4j
@RestController
@Api(tags = "平台-租户管理")
@RequestMapping("/platform/tenant")
public class PlatformTenantManageController {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private PlatformTenantOrganizationService platformTenantOrganizationService;


    /**
     * 新增租户
     */
    @ApiOperation(value = "新增租户", tags = "租户管理")
    @PostMapping("/createTenant")
    public ResponseResult<Object> create(@RequestBody @Valid TenantReqDTO tenantCreateReqDTO) {
        tenantCreateReqDTO.setId(null);
        platformTenantOrganizationService.create(tenantCreateReqDTO);
        return ResponseResult.success();
    }

    /**
     * 租户删除
     */
    @ApiOperation(value = "租户删除", tags = "租户管理")
    @DeleteMapping("/remove/{id}")
    @ResetUserCache
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = platformTenantOrganizationService.removeTenantById(id);
        return ResponseResult.success(result);
    }

    /**
     * 租户禁启用
     *
     * @param statusDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "租户禁启用", tags = "租户管理")
    @PostMapping("/updateStatus")
    @ResetUserCache
    public ResponseResult<Boolean> updateStatus(@Valid @RequestBody UpdateStatusReqDTO statusDTO) {
        AssertUtil.requireNotNull(statusDTO.getId(), "10007");
        Boolean result = organizationManageService.updateStatus(statusDTO);
        return ResponseResult.success(result);
    }

    /**
     * 系统用户重置密码
     *
     * @param userPwdResetDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "重置租户密码", tags = "租户管理")
    @PostMapping("/resetPwd")
    public ResponseResult create(@RequestBody @Valid RsUserPwdResetDTO userPwdResetDTO) {
        Boolean result = userManagementService.resetPwd(userPwdResetDTO);
        return ResponseResult.success(result);
    }

    /**
     * 租户编辑
     *
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "租户编辑", tags = "租户管理")
    @PostMapping("/modify")
    @ResetUserCache
    public ResponseResult<Boolean> modify(@RequestBody @Valid TenantReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        platformTenantOrganizationService.update(reqDTO);
        return ResponseResult.success();
    }

    /**
     * 租户分页查询
     *
     * @return 分页数据
     */
    @ApiOperation(value = "租户分页查询", tags = "租户管理")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<TenantOrganizationVO>> listTenantPage(@RequestBody TenantQuery tenantQuery) {
        PageInfo<TenantOrganizationVO> result = platformTenantOrganizationService.listTenantPage(tenantQuery);
        return ResponseResult.success(result);
    }

    /**
     * 租户分页查询
     *
     * @return 分页数据
     */
    @ApiOperation(value = "租户分页查询", tags = "租户管理")
    @PostMapping("/getTenantInfoById/{id}")
    public ResponseResult<TenantOrganizationVO> getTenantInfoById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        TenantOrganizationVO result = platformTenantOrganizationService.getTenantInfoById(id);
        return ResponseResult.success(result);
    }


}
