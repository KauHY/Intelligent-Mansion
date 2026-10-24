package com.crlink.smartpark.system.controller.platform;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.user.RsUserCreateDTO;
import com.crlink.smartpark.system.dto.user.RsUserPwdResetDTO;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.dto.user.SysUserReqDTO;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.service.platform.PlatformUserService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.crlink.smartpark.common.constants.Constants.PLATFORM_ORG_ID;
import static com.crlink.smartpark.common.constants.Constants.PLATFORM_USER;

/**
 * 平台-系统用户管理
 * */
@Slf4j
@RestController
@Api(tags = "平台-系统用户管理")
@RequestMapping("/platform/user")
public class PlatformSysUserController {

    @Autowired
    private PlatformUserService platformUserService;

    @Autowired
    private UserManagementService userManagementService;


    /**
     * 创建角色，系统用户下拉列表
     *
     * @param roleQuery
     * @return
     */

    @ApiOperation(value = "创建角色，系统用户下拉列表", tags = "用户管理")
    @PostMapping("/listUsers")
    public ResponseResult<PageInfo<DictVO>> listDictUsers(@RequestBody BaseQuery<String> roleQuery) {
        PageInfo<DictVO> page = userManagementService.listDictUsers(PLATFORM_USER, roleQuery, Sets.newHashSet(PLATFORM_ORG_ID));
        return ResponseResult.success(page);
    }

    /**
     * 新增系统用户
     *
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "新增系统用户", tags = "用户管理")
    @PostMapping("/save")
    public ResponseResult<Boolean> create(@RequestBody @Valid RsUserCreateDTO userReq) {
        userReq.setOrgId(PLATFORM_ORG_ID);
        userReq.setUserType(PLATFORM_USER);
        Boolean result = platformUserService.savePlatformSysRsUser(userReq);
        return ResponseResult.success(result);
    }

    /**
     * 编辑系统用户
     *
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "编辑系统用户", tags = "用户管理")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody @Valid SysUserReqDTO userUpdateDTO) {
        AssertUtil.requireNotNull(userUpdateDTO.getUserId(), "userId 用户Id不能为空");
        Boolean result = platformUserService.updatePlatformSysUser(userUpdateDTO);
        return ResponseResult.success(result);
    }

    /**
     * 系统用户重置密码
     *
     * @param userPwdResetDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "系统用户重置密码", tags = "用户管理")
    @PostMapping("/resetPwd")
    public ResponseResult create(@RequestBody @Valid RsUserPwdResetDTO userPwdResetDTO) {
        Boolean result = userManagementService.resetPwd(userPwdResetDTO);
        return ResponseResult.success(result);
    }
    //

    /**
     * 删除系统用户
     *
     * @param userId 主键
     * @return 是否成功
     */
    @ApiOperation(value = "删除系统用户", tags = "用户管理")
    @DeleteMapping("/remove/{userId}")
    public ResponseResult<Boolean> remove(@PathVariable("userId") @NotNull(message = "userId不能为空") String userId) {
        Boolean result = platformUserService.removeUserById(userId);
        return ResponseResult.success(result);
    }
    //

    /**
     * 平台系统用户分页查询
     *
     * @param rsUserQueryDTO
     * @return
     */
    @ApiOperation(value = "平台系统用户分页查询", tags = "用户管理")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<RsUserInfoVO>> sysUserPage(@RequestBody RsUserQueryDTO rsUserQueryDTO) {
        PageInfo<RsUserInfoVO> result = platformUserService.listSysUserPage(rsUserQueryDTO);
        return ResponseResult.success(result);
    }

    /**
     * 系统用户启用/禁用
     *
     * @return 实体
     */
    @ApiOperation(value = "系统用户启用/禁用", tags = "用户管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@RequestBody UpdateStatusReqDTO status) {
        AssertUtil.requireNotNull(status.getId(), "10007");
        Boolean result = userManagementService.updateStatus(status);
        return ResponseResult.success(result);
    }


}

