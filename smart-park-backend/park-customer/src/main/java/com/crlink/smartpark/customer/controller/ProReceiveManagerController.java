package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.customer.controller.request.ProReceiveInAuditDTO;
import com.crlink.smartpark.customer.controller.request.ProReceiveOutAuditDTO;
import com.crlink.smartpark.customer.model.vo.ReceiveDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.query.ProReceiveManagerQuery;
import com.crlink.smartpark.customer.dto.ProReceiveManagerDTO;
import com.crlink.smartpark.customer.service.ProReceiveManagerService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 物品领用管理
 * */
@Slf4j
@RestController
@Api(tags = "资产管理-物品领用管理")
@RequestMapping("/pro-receive-manager")
@Validated
public class ProReceiveManagerController {

    @Autowired
    private ProReceiveManagerService proReceiveManagerService;

    @ApiOperation(value = "物品领用管理-审核出库", tags = "物品领用管理")
    @PostMapping("/auditReceive")
    public ResponseResult auditReceive(@RequestBody @Valid ProReceiveInAuditDTO reqDTO) {
        proReceiveManagerService.auditReceive(reqDTO);
        return ResponseResult.success();
    }

    @ApiOperation(value = "物品领用管理-获取领用详情", tags = "物品领用管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<ReceiveDetailVo> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proReceiveManagerService.getProReceiveManagerById(id));
    }

    @ApiOperation(value = "物品领用管理-审核入库", tags = "物品领用管理")
    @PostMapping("/auditReturn")
    public ResponseResult auditReturn(@RequestBody @Valid ProReceiveOutAuditDTO reqDTO) {
        proReceiveManagerService.auditReturn(reqDTO);
        return ResponseResult.success();
    }

    @ApiOperation(value = "物品领用管理-分页查询", tags = "物品领用管理")
    @PostMapping("/listProReceiveManagerPage")
    public ResponseResult<PageInfo<ProReceiveManagerDTO>> listProReceiveManagerPage(@RequestBody @Valid ProReceiveManagerQuery query) {
        return ResponseResult.success(proReceiveManagerService.listProReceiveManagerPage(query));
    }


}

