package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.user.RsUserCreateDTO;
import com.crlink.smartpark.system.dto.user.RsUserPwdResetDTO;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.dto.user.UserReqDTO;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementUserService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 园区运营平台-用户管理
 * 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-用户管理")
@RequestMapping("/parkManagement/user")
public class ParkManagementUserController {

    @Autowired
    private ParkManagementUserService parkManagementUserService;

    @Autowired
    private UserManagementService userManagementService;

    /**
     * 创建角色，户下拉列表
     *
     * @param roleQuery
     * @return
     */
    @ApiOperation(value = "创建角色，用户下拉列表", tags = "用户管理")
    @PostMapping("/listUsers")
    public ResponseResult<PageInfo<DictVO>> listDictUsers(@RequestBody BaseQuery<String> roleQuery) {
        PageInfo<DictVO> page = parkManagementUserService.listDictUsers(roleQuery);
        return ResponseResult.success(page);
    }

    /**
     * 新增用户
     *
     * @param rsUserReq 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "新增用户", tags = "用户管理")
    @PostMapping("/save")
    public ResponseResult create(@RequestBody @Valid RsUserCreateDTO rsUserReq) {
        Boolean result = parkManagementUserService.saveTenantUser(rsUserReq);
        return ResponseResult.success(result);
    }

    /**
     * 编辑用户
     *
     * @param rsUserReq 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "编辑用户", tags = "用户管理")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody @Valid UserReqDTO rsUserReq) {
        parkManagementUserService.updateRsUser(rsUserReq);
        return ResponseResult.success();
    }

    /**
     * 重置密码
     *
     * @param userPwdResetDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "重置密码", tags = "用户管理")
    @PostMapping("/resetPwd")
    public ResponseResult create(@RequestBody @Valid RsUserPwdResetDTO userPwdResetDTO) {
        log.info("-新增，请求入参：RsUser={}", userPwdResetDTO);
        Boolean result = userManagementService.resetPwd(userPwdResetDTO);
        return ResponseResult.success(result);
    }

    /**
     * 删除用户
     *
     * @param userId 主键
     * @return 是否成功
     */
    @ApiOperation(value = "删除用户", tags = "用户管理")
    @DeleteMapping("/remove/{userId}")
    public ResponseResult<Boolean> remove(@PathVariable("userId") @NotNull(message = "userId不能为空") String userId) {
        Boolean result = userManagementService.removeUserById(userId);
        return ResponseResult.success(result);
    }


    /**
     * 用户分页
     *
     * @param rsUserQueryDTO 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "用户分页", tags = "用户管理")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<RsUserInfoVO>> pageUsers(@RequestBody RsUserQueryDTO rsUserQueryDTO) {
        PageInfo<RsUserInfoVO> result = parkManagementUserService.listUserPage(rsUserQueryDTO);
        return ResponseResult.success(result);
    }

    /**
     * 用户启用/禁用
     *
     * @return 实体
     */
    @ApiOperation(value = "用户启用/禁用", tags = "用户管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@RequestBody UpdateStatusReqDTO status) {
        Boolean result = userManagementService.updateStatus(status);
        return ResponseResult.success(result);
    }

}

