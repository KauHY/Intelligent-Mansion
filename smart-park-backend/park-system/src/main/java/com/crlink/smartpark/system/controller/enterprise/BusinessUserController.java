package com.crlink.smartpark.system.controller.enterprise;

import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.user.ResetPwdForForgetDTO;
import com.crlink.smartpark.system.dto.user.RsAuthLoginDTO;
import com.crlink.smartpark.system.dto.user.RsUserValidateDTO;
import com.crlink.smartpark.system.dto.user.UpdatePwdDTO;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.service.common.UserManagementService;
import com.crlink.smartpark.system.vo.BusinessUserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.model.query.BusinessUserQuery;
import com.crlink.smartpark.system.model.BusinessUserReqDTO;
import com.crlink.smartpark.system.dto.BusinessUserDTO;
import com.crlink.smartpark.system.service.BusinessUserService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * park-customer 企业端用户模块
 * */
@Api(tags = "企业端-基础模块-白名单")
@Slf4j
@RestController
@RequestMapping("/business-user")
@Validated
public class BusinessUserController {

    @Autowired
    private BusinessUserService businessUserService;

    @ApiOperation(value = "企业端用户-注册", tags = "企业端用户")
    @PostMapping("/register")
    public ResponseResult<Boolean> create(@RequestBody @Valid BusinessUserReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(businessUserService.saveOrUpdateBusinessUser(reqDTO));
    }

    @ApiOperation(value = "企业端用户-登陆", tags = "企业端用户")
    @PostMapping("/login")
    public ResponseResult<BusinessUserLoginVO> login(@RequestBody @Valid RsAuthLoginDTO rsAuthLoginDTO) {
        return ResponseResult.success(businessUserService.login(rsAuthLoginDTO));
    }

    @ApiOperation(value = "企业端用户-注册获取短信验证码", tags = "企业端用户")
    @PostMapping("/getSmsCode")
    public ResponseResult<Boolean> getSmsCode(@RequestBody @Valid BusinessUserMobileDTO reqDTO) {
        return ResponseResult.success(businessUserService.getSmsCode(reqDTO));
    }

    @ApiOperation(value = "企业端用户-忘记密码-0获取短信验证码", tags = "企业端用户")
    @PostMapping("/getResetPwdSmsCode")
    public ResponseResult<Boolean> getResetPwdSmsCode(@RequestBody @Valid BusinessUserMobileDTO reqDTO) {
        return ResponseResult.success(businessUserService.getResetPwdSmsCode(reqDTO));
    }
    /**
     * 企业端用户 重置密码 第一步 校验身份
     * @param userPwdResetDTO:
     * @return  com.crlink.smartpark.common.web.ResponseResult<java.lang.String>
     * @MethodName validateIdentity
     */
    @ApiOperation(value = "企业端用户-忘记密码-1校验身份", tags = "企业端用户")
    @PostMapping("/validateIdentity")
    public ResponseResult<String> validateIdentity(@RequestBody @Valid RsUserValidateDTO userPwdResetDTO) {
        String result = businessUserService.validateIdentity(userPwdResetDTO);
        return ResponseResult.success(result);
    }

    /**
     * 企业端用户 重置密码 第二步 重置密码
     * @param resetPwdForForgetDTO:
     * @return  com.crlink.smartpark.common.web.ResponseResult
     * @MethodName resetPwdForForget
     */
    @ApiOperation(value = "企业端用户-忘记密码-2重置密码", tags = "企业端用户")
    @PostMapping("/resetPwdForForget")
    public ResponseResult resetPwdForForget(@RequestBody @Valid ResetPwdForForgetDTO resetPwdForForgetDTO) {
        Boolean result = businessUserService.resetPwdForForget(resetPwdForForgetDTO);
        return ResponseResult.success(result);
    }



}
