package com.crlink.smartpark.system.controller.enterprise;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.user.UpdatePwdDTO;
import com.crlink.smartpark.system.service.BusinessUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @ClassName: BusinessUserAuditController
 * @Package com.crlink.smartpark.system.controller.enterprise
 * @Description: 企业端用户-鉴权模块
 * @Version 1.0
 */
@Api(tags = "企业端-基础模块-鉴权")
@Slf4j
@RestController
@RequestMapping("/businessUserAudit")
@Validated
public class BusinessUserAuditController {

    @Autowired
    private BusinessUserService businessUserService;

    /**
     * 企业端用户 修改密码
     * @param updatePwdDTO:
     * @return  com.crlink.smartpark.common.web.ResponseResult
     * @MethodName updatePwd
     */
    @ApiOperation(value = "企业端用户-修改密码", tags = "个人信息")
    @PostMapping("/updatePwd")
    public ResponseResult updatePwd(@RequestBody @Valid UpdatePwdDTO updatePwdDTO) {
        Boolean result = businessUserService.updatePwd(updatePwdDTO);
        return ResponseResult.success(result);
    }
}
