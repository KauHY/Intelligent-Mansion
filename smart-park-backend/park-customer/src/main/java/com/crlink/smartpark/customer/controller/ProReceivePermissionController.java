package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.customer.controller.request.ProReceivePermissionAddDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.query.ProReceivePermissionQuery;
import com.crlink.smartpark.customer.controller.request.ProReceivePermissionReqDTO;
import com.crlink.smartpark.customer.dto.ProReceivePermissionDTO;
import com.crlink.smartpark.customer.service.ProReceivePermissionService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 领用权限设置
 * */
@Slf4j
@RestController
@Api(tags = "资产管理-领用权限设置")
@RequestMapping("/pro-receive-permission")
@Validated
public class ProReceivePermissionController {

    @Autowired
    private ProReceivePermissionService proReceivePermissionService;


    @ApiOperation(value = "领用权限设置-设置权限", tags = "领用权限设置")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ProReceivePermissionAddDTO reqDTO) {
        return ResponseResult.success(proReceivePermissionService.saveOrUpdateProReceivePermission(reqDTO));
    }

    @ApiOperation(value = "领用权限设置-分页查询", tags = "领用权限设置")
    @PostMapping("/listProReceivePermissionPage")
    public ResponseResult<PageInfo<ProReceivePermissionDTO>> listProReceivePermissionPage(@RequestBody @Valid ProReceivePermissionQuery query){
        return ResponseResult.success(proReceivePermissionService.listProReceivePermissionPage(query));
    }

}

