package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.ProReceiveApplyDTO;
import com.crlink.smartpark.customer.controller.request.ProReceiveCancelDTO;
import com.crlink.smartpark.customer.controller.request.ProReceiveGoodsDTO;
import com.crlink.smartpark.customer.controller.request.ProReceiveReturnDTO;
import com.crlink.smartpark.customer.controller.request.query.ProReceiveAuthDTO;
import com.crlink.smartpark.customer.controller.request.query.ProReceiveManagerQuery;
import com.crlink.smartpark.customer.dto.ProReceiveManagerDTO;
import com.crlink.smartpark.customer.model.vo.ReceiveDetailVo;
import com.crlink.smartpark.customer.model.vo.StockManagerSelectVo;
import com.crlink.smartpark.customer.service.ProReceiveManagerService;
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
 * park-customer
 * 物品领用管理
 * */
@Slf4j
@RestController
@Api(tags = "资产管理-C端小程序物品领用管理")
@RequestMapping("/wx-pro-receive-manager")
@Validated
public class WxProReceiveManagerController {

    @Autowired
    private ProReceiveManagerService proReceiveManagerService;

    @ApiOperation(value = "物品领用管理-权限判断", tags = "C端小程序物品领用管理")
    @PostMapping("/auth")
    public ResponseResult<Boolean> auth(@RequestBody @Valid ProReceiveAuthDTO authDTO) {
        return ResponseResult.success(proReceiveManagerService.auth(authDTO.getParkOrgId()));
    }

    @ApiOperation(value = "物品领用管理-领用申请", tags = "C端小程序物品领用管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ProReceiveApplyDTO reqDTO) {
        return ResponseResult.success(proReceiveManagerService.saveOrUpdateProReceiveManager(reqDTO));
    }

    @ApiOperation(value = "物品领用管理-选择物品", tags = "C端小程序物品领用管理")
    @PostMapping("/selectGoods")
    public ResponseResult<PageInfo<StockManagerSelectVo>> selectGoods(@RequestBody @Valid ProReceiveGoodsDTO reqDTO) {
        return ResponseResult.success(proReceiveManagerService.selectGoods(reqDTO));
    }

    @ApiOperation(value = "物品领用管理-获取领用详情", tags = "C端小程序物品领用管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<ReceiveDetailVo> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proReceiveManagerService.getProReceiveManagerById(id));
    }

    @ApiOperation(value = "物品领用管理-物品归还", tags = "C端小程序物品领用管理")
    @PostMapping("/returnGoods")
    public ResponseResult<Boolean> returnGoods(@RequestBody @Valid ProReceiveReturnDTO reqDTO) {
        return ResponseResult.success(proReceiveManagerService.returnGoods(reqDTO));
    }

    @ApiOperation(value = "物品领用管理-取消", tags = "C端小程序物品领用管理")
    @PostMapping("/cancel")
    public ResponseResult<Boolean> cancel(@RequestBody @Valid ProReceiveCancelDTO reqDTO) {
        return ResponseResult.success(proReceiveManagerService.cancel(reqDTO));
    }

    @ApiOperation(value = "物品领用管理-分页查询", tags = "C端小程序物品领用管理")
    @PostMapping("/listProReceivePage")
    public ResponseResult<PageInfo<ProReceiveManagerDTO>> listProReceivePage(@RequestBody @Valid ProReceiveManagerQuery query) {
        return ResponseResult.success(proReceiveManagerService.listProReceivePage(query));
    }


}

