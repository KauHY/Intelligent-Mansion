package com.crlink.smartpark.enterprise.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.industrial.dto.PurchaseServiceDTO;
import com.crlink.smartpark.enterprise.model.TenderRecordReqDTO;
import com.crlink.smartpark.enterprise.model.query.PurchaseServiceQuery;
import com.crlink.smartpark.enterprise.service.PurchaseServiceService;
import com.crlink.smartpark.enterprise.service.TenderRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * park-enterprise
 * 采购服务
 * */
@Slf4j
@RestController
@RequestMapping("/wx/purchase-service")
@Validated
@Api(tags = "产业服务")
public class WxPurchaseServiceController {

    @Autowired
    private PurchaseServiceService purchaseServiceService;
    @Autowired
    private TenderRecordService tenderRecordService;

    @ApiOperation(value = "采购服务-小程序-根据ID查询", tags = "采购服务")
    @GetMapping("/findById/{id}")
    public ResponseResult<PurchaseServiceDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(purchaseServiceService.getPurchaseServiceById(id));
    }

    @ApiOperation(value = "采购服务-小程序-分页查询", tags = "采购服务")
    @PostMapping("/listPurchaseServicePage")
    public ResponseResult<PageInfo<PurchaseServiceDTO>> listPurchaseServicePage(@RequestBody @Valid PurchaseServiceQuery query){
        return ResponseResult.success(purchaseServiceService.listPurchaseServicePage(query));
    }

    @ApiOperation(value = "采购服务-小程序-报名", tags = "采购服务")
    @PostMapping("/signUp")
    public ResponseResult<Boolean> signUp(@RequestBody @Valid TenderRecordReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(tenderRecordService.saveOrUpdateTenderRecord(reqDTO));
    }

}

