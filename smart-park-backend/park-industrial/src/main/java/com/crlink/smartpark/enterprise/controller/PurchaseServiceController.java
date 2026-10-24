package com.crlink.smartpark.enterprise.controller;

import com.crlink.smartpark.enterprise.model.StateReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.enterprise.model.query.PurchaseServiceQuery;
import com.crlink.smartpark.enterprise.model.PurchaseServiceReqDTO;
import com.crlink.smartpark.industrial.dto.PurchaseServiceDTO;
import com.crlink.smartpark.enterprise.service.PurchaseServiceService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-enterprise
 * 采购服务
 * */
@Slf4j
@RestController
@RequestMapping("/purchase-service")
@Validated
@Api(tags = "产业服务")
public class PurchaseServiceController {

    @Autowired
    private PurchaseServiceService purchaseServiceService;


    @ApiOperation(value = "采购服务-新增", tags = "采购服务")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid PurchaseServiceReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(purchaseServiceService.saveOrUpdatePurchaseService(reqDTO));
    }


    @ApiOperation(value = "采购服务-删除", tags = "采购服务")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(purchaseServiceService.removeById(id));
    }


    @ApiOperation(value = "采购服务-根据ID查询", tags = "采购服务")
    @GetMapping("/findById/{id}")
    public ResponseResult<PurchaseServiceDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(purchaseServiceService.getPurchaseServiceById(id));
    }


    @ApiOperation(value = "采购服务-修改", tags = "采购服务")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid PurchaseServiceReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(purchaseServiceService.saveOrUpdatePurchaseService(reqDTO));
    }


    @ApiOperation(value = "采购服务-分页查询", tags = "采购服务")
    @PostMapping("/listPurchaseServicePage")
    public ResponseResult<PageInfo<PurchaseServiceDTO>> listPurchaseServicePage(@RequestBody @Valid PurchaseServiceQuery query){
        return ResponseResult.success(purchaseServiceService.listPurchaseServicePage(query));
    }

    @ApiOperation(value = "企业服务-状态变更", tags = "企业服务")
    @PostMapping("/changeState")
    public ResponseResult<Boolean> changeState(@RequestBody @Valid StateReqDTO reqDTO) {
        return ResponseResult.success(purchaseServiceService.changeState(reqDTO));
    }

}

