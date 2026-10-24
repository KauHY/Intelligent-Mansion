package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.system.dto.user.ResetPwdForForgetDTO;
import com.crlink.smartpark.system.dto.user.RsAuthLoginDTO;
import com.crlink.smartpark.system.dto.user.RsUserValidateDTO;
import com.crlink.smartpark.system.dto.user.UpdatePwdDTO;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementUserService;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 园区运营平台-登录相关
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-登录相关")
@RequestMapping("/parkManagement/auth")
public class ParkManagementAuthController {

    @Autowired
    private ParkManagementUserService parkManagementUserService;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 租户用户-登陆
     */
    @ApiOperation(value = "租户用户-登陆", tags = "用户权限")
    @PostMapping("/login")
    public ResponseResult<RsUserLoginVO> create(@RequestBody @Valid RsAuthLoginDTO rsAuthLoginDTO) {
        RsUserLoginVO result = parkManagementUserService.checkTenantLogin(rsAuthLoginDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "忘记密码-获取短信验证码", tags = "忘记密码")
    @PostMapping("/getResetPwdSmsCode")
    public ResponseResult<Boolean> getResetPwdSmsCode(@RequestBody @Valid BusinessUserMobileDTO reqDTO) {
        return ResponseResult.success(userManagementService.getResetPwdSmsCode(reqDTO));
    }

    /**
     * 验证身份
     * n
     */
    @ApiOperation(value = "验证身份", tags = "忘记密码")
    @PostMapping("/validateIdentity")
    public ResponseResult<String> validateIdentity(@RequestBody @Valid RsUserValidateDTO userPwdResetDTO) {
        String result = userManagementService.validateIdentity(userPwdResetDTO);
        return ResponseResult.success(result);
    }

    /**
     * 重置密码
     */
    @ApiOperation(value = "重置密码", tags = "忘记密码")
    @PostMapping("/resetPwdForForget")
    public ResponseResult resetPwdForForget(@RequestBody @Valid ResetPwdForForgetDTO resetPwdForForgetDTO) {
        Boolean result = userManagementService.resetPwdForForget(resetPwdForForgetDTO);
        return ResponseResult.success(result);
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", tags = "个人信息")
    @PostMapping("/updatePwd")
    public ResponseResult updatePwd(@RequestBody @Valid UpdatePwdDTO updatePwdDTO) {
        Boolean result = userManagementService.updatePwd(updatePwdDTO);
        return ResponseResult.success(result);
    }


    /**
     * 退出系统
     */
    @ApiOperation(value = "退出系统", tags = "个人信息")
    @PostMapping(value = "/logout")
    public ResponseResult logout() {
        Boolean result = userManagementService.logout();
        return ResponseResult.success(result);
    }


}

