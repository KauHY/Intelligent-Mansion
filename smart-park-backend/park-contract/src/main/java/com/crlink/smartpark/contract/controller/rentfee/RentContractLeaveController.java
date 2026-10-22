package com.crlink.smartpark.contract.controller.rentfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.RentContractLeaveDTO;
import com.crlink.smartpark.contract.model.query.RentContractLeaveQuery;
import com.crlink.smartpark.contract.model.request.RentContractLeaveReqDTO;
import com.crlink.smartpark.contract.model.request.SettlementPayReqDTO;
import com.crlink.smartpark.contract.model.vo.RentContractLeaveListVO;
import com.crlink.smartpark.contract.service.RentContractLeaveService;
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
 * park-contract
 * 离场结算记录
 * */
@Slf4j
@RestController
@RequestMapping("/rentContractLeave")
@Validated
@Api(tags = "租赁合同收费模块-离场结算记录")
public class RentContractLeaveController {

    @Autowired
    private RentContractLeaveService rentContractLeaveService;


    @ApiOperation(value = "离场结算记录-离场结算-计算费用", tags = "离场结算记录")
    @PostMapping("/leaveSettlement")
    public ResponseResult<RentContractLeaveDTO> leaveSettlement(@RequestBody @Valid RentContractLeaveReqDTO reqDTO) {
        return ResponseResult.success(rentContractLeaveService.leaveSettlement(reqDTO));
    }


    @ApiOperation(value = "离场结算记录-结算缴费", tags = "离场结算记录")
    @PostMapping("/settlementPay")
    public ResponseResult<Boolean> settlementPay(@RequestBody @Valid SettlementPayReqDTO reqDTO) {
        return ResponseResult.success(rentContractLeaveService.settlementPay(reqDTO));
    }


    @ApiOperation(value = "离场结算记录-根据ID查询", tags = "离场结算记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<RentContractLeaveDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(rentContractLeaveService.getRentContractLeaveById(id));
    }


    @ApiOperation(value = "离场结算记录-分页查询", tags = "离场结算记录")
    @PostMapping("/listRentContractLeavePage")
    public ResponseResult<PageInfo<RentContractLeaveListVO>> listRentContractLeavePage(@RequestBody @Valid RentContractLeaveQuery query) {
        return ResponseResult.success(rentContractLeaveService.listRentContractLeavePage(query));
    }

    @ApiOperation(value = "离场结算记录-导出", tags = "离场结算记录")
    @PostMapping("/export")
    public void export(@RequestBody @Valid RentContractLeaveQuery query) {
        rentContractLeaveService.export(query);
    }
}

