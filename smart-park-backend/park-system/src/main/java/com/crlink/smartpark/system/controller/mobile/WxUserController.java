package com.crlink.smartpark.system.controller.mobile;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.wxuser.WxUserDTO;
import com.crlink.smartpark.system.dto.wxuser.WxUserVO;
import com.crlink.smartpark.system.service.WxUserManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 小程序-用户信息
 * */
@Slf4j
@Api(tags = "小程序-用户信息")
@RestController
@RequestMapping("wx/user")
public class WxUserController {

    @Autowired
    private WxUserManageService wxUserManageService;

    /**
     * 授权登录
     *
     * @return
     */
    @ApiOperation(value = "运营小程序授权登录", tags = "")
    @PostMapping("/authParkManagement/login")
    public ResponseResult<WxUserVO> authParkManagementLogin(@RequestBody @Valid WxUserDTO wxUserDTO) {
        WxUserVO result = wxUserManageService.authParkManagementLogin(wxUserDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "C端小程序", tags = "")
    @PostMapping("/authParkVisitor/login")
    public ResponseResult<WxUserVO> authParkVisitorLogin(@RequestBody @Valid WxUserDTO wxUserDTO) {
        WxUserVO wxUserVO = wxUserManageService.authParkVisitorLogin(wxUserDTO);
        return ResponseResult.success(wxUserVO);
    }

    @ApiOperation(value = "获取用户信息", tags = "")
    @GetMapping("/wxUserInfo")
    public ResponseResult<WxUserVO> getWxUserInfo() {
        //SystemUtil.getInstance()
        WxUserVO wxUserVO = wxUserManageService.queryWxUserInfo();
        return ResponseResult.success(wxUserVO);
    }


    /**
     * c端小程序个人中心信息
     */
    //@ApiOperation(value = "c端小程序个人中心信息", tags = "")
    //@GetMapping("/info")
    //public ResponseResult<WxUserVO> info() {
    //    return ResponseResult.success(wxUserManageService.info());
    //}

}

