package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.customer.controller.request.query.ProStockRecordQuery;
import com.crlink.smartpark.customer.model.query.ProInventoryManagerQuery;
import com.crlink.smartpark.customer.task.InventoryStockAutoTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.model.dto.ProInventoryManagerReqDTO;
import com.crlink.smartpark.customer.dto.ProInventoryManagerDTO;
import com.crlink.smartpark.customer.service.ProInventoryManagerService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 盘点管理
 * */
@Slf4j
@RestController
@Api(tags = "资产管理-盘点管理")
@RequestMapping("/pro-inventory-manager")
@Validated
public class ProInventoryManagerController {

    @Autowired
    private ProInventoryManagerService proInventoryManagerService;

    @Autowired
    private InventoryStockAutoTask taskStockAutoTask ;

//    @ApiOperation(value = "盘点管理-新增", tags = "盘点管理")
//    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ProInventoryManagerReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(proInventoryManagerService.saveOrUpdateProInventoryManager(reqDTO));
    }


//    @ApiOperation(value = "盘点管理-删除", tags = "盘点管理")
//    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proInventoryManagerService.removeById(id));
    }

//
//    @ApiOperation(value = "盘点管理-根据ID查询", tags = "盘点管理")
//    @GetMapping("/findById/{id}")
    public ResponseResult<ProInventoryManagerDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proInventoryManagerService.getProInventoryManagerById(id));
    }


//    @ApiOperation(value = "盘点管理-修改", tags = "盘点管理")
//    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ProInventoryManagerReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(proInventoryManagerService.saveOrUpdateProInventoryManager(reqDTO));
    }


    @ApiOperation(value = "盘点管理-分页查询", tags = "盘点管理")
    @PostMapping("/listProInventoryManagerPage")
    public ResponseResult<PageInfo<ProInventoryManagerDTO>> listProInventoryManagerPage(@RequestBody @Valid ProInventoryManagerQuery query){
        return ResponseResult.success(proInventoryManagerService.listProInventoryManagerPage(query));
    }


    @ApiOperation(value = "盘点管理-text", tags = "盘点管理")
    @PostMapping("/text")
    public ResponseResult<PageInfo<ProInventoryManagerDTO>> text(@RequestBody @Valid ProInventoryManagerQuery query){

        taskStockAutoTask.autoInventoryStock();
        return ResponseResult.success(null);
    }

    @ApiOperation(value = "盘点管理-导出", tags = "盘点管理")
    @PostMapping("/export")
    public void export(@RequestBody ProInventoryManagerQuery query){
        proInventoryManagerService.export(query);
    }

}

