package com.crlink.smartpark.system.controller;

import com.crlink.smartpark.common.annotations.ShortMessageCheck;
import com.crlink.smartpark.common.constants.ShortMessageConstant;
import com.crlink.smartpark.core.utils.SignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.query.WxOaMobileRelationQuery;
import com.crlink.smartpark.system.model.WxOaMobileRelationReqDTO;
import com.crlink.smartpark.system.service.WxOaMobileRelationService;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * park-system
 * 公众号用户openId与手机号关联表
 * */
@Slf4j
@RestController
@RequestMapping("/wx-oa-mobile-relation")
@Validated
@Api(tags = "公众号")
public class WxOaMobileRelationController {

    @Autowired
    private WxOaMobileRelationService wxOaMobileRelationService;


    @ApiOperation(value = "公众号用户openId与手机号关联表-绑定", tags = "公众号用户openId与手机号关联表")
    @PostMapping("/bind")
    public ResponseResult<Boolean> bind(@RequestBody @Valid WxOaMobileRelationReqDTO reqDTO) {
        return ResponseResult.success(wxOaMobileRelationService.bind(reqDTO));
    }

    @ApiOperation(value = "公众号用户openId与手机号关联表-解绑", tags = "公众号用户openId与手机号关联表")
    @PostMapping("/unbind")
    public ResponseResult<Boolean> unbind(@RequestBody @Valid WxOaMobileRelationReqDTO reqDTO) {
        return ResponseResult.success(wxOaMobileRelationService.unbind(reqDTO));
    }

    @ApiOperation(value = "公众号用户openId与手机号关联表-换绑", tags = "公众号用户openId与手机号关联表")
    @PostMapping("/changeBind")
    public ResponseResult<Boolean> changeBind(@RequestBody @Valid WxOaMobileRelationReqDTO reqDTO) {
        return ResponseResult.success(wxOaMobileRelationService.changeBind(reqDTO));
    }

    @ApiOperation(value = "内部接口-通过手机号和公众号APPID获取用户openId", tags = "公众号用户openId与手机号关联表")
    @PostMapping("/getOpenIdsByMobilesAndAppId")
    public Set<String> getOpenIdsByMobilesAndAppId(@RequestBody @Valid WxOaMobileRelationQuery query) {
        return wxOaMobileRelationService.getOpenIdsByMobilesAndAppId(query);
    }

    @GetMapping("/wxSign")
    public String checkSign(HttpServletRequest request, HttpServletResponse response) {
        try {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            log.info("本身" + signature);
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                return echostr;
            }
        } catch (Exception e) {
            log.error("验证公众号token失败", e);
        }
        return null;
    }



}

