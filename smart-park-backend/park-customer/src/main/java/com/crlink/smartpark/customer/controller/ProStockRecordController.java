package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.customer.controller.request.query.ProGoodsManagerQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.query.ProStockRecordQuery;
import com.crlink.smartpark.customer.model.dto.ProStockRecordReqDTO;
import com.crlink.smartpark.customer.dto.ProStockRecordDTO;
import com.crlink.smartpark.customer.service.ProStockRecordService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 库存出入库记录
 * */
@Slf4j
@RestController
@Api(tags = "资产管理-库存出入库记录")
@RequestMapping("/pro-stock-record")
@Validated
public class ProStockRecordController {

    @Autowired
    private ProStockRecordService proStockRecordService;


//    @ApiOperation(value = "库存出入库记录-新增", tags = "库存出入库记录")
//    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ProStockRecordReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(proStockRecordService.saveOrUpdateProStockRecord(reqDTO));
    }


//    @ApiOperation(value = "库存出入库记录-删除", tags = "库存出入库记录")
//    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proStockRecordService.removeById(id));
    }

//
//    @ApiOperation(value = "库存出入库记录-根据ID查询", tags = "库存出入库记录")
//    @GetMapping("/findById/{id}")
    public ResponseResult<ProStockRecordDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proStockRecordService.getProStockRecordById(id));
    }

//
//    @ApiOperation(value = "库存出入库记录-修改", tags = "库存出入库记录")
//    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ProStockRecordReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(proStockRecordService.saveOrUpdateProStockRecord(reqDTO));
    }


    @ApiOperation(value = "库存出入库记录-分页查询", tags = "库存出入库记录")
    @PostMapping("/listProStockRecordPage")
    public ResponseResult<PageInfo<ProStockRecordDTO>> listProStockRecordPage(@RequestBody @Valid ProStockRecordQuery query){
        return ResponseResult.success(proStockRecordService.listProStockRecordPage(query));
    }

    @ApiOperation(value = "库存出入库记录-导出", tags = "库存出入库记录")
    @PostMapping("/export")
    public void export(@RequestBody ProStockRecordQuery query){
        proStockRecordService.export(query);
    }

}

