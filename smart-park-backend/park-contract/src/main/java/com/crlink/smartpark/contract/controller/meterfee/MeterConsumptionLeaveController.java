package com.crlink.smartpark.contract.controller.meterfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.ChargeDetail;
import com.crlink.smartpark.contract.dto.ComputeAmountDTO;
import com.crlink.smartpark.contract.dto.MeterConsumptionLeaveDTO;
import com.crlink.smartpark.contract.model.query.RentContractLeaveQuery;
import com.crlink.smartpark.contract.model.request.MeterConsumptionLeaveReqDTO;
import com.crlink.smartpark.contract.model.request.SettlementPayReqDTO;
import com.crlink.smartpark.contract.model.vo.RentContractLeaveListVO;
import com.crlink.smartpark.contract.service.MeterConsumptionLeaveService;
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
 * park-device
 * 租赁合同交易明细
 * */
@Slf4j
@RestController
@RequestMapping("/meterConsumptionLeave")
@Validated
@Api(tags = "水电费离场结算记录")
public class MeterConsumptionLeaveController {

    @Autowired
    private MeterConsumptionLeaveService meterConsumptionLeaveService;


    @ApiOperation(value = "水电费离场结算记录-离场结算-计算费用", tags = "水电费离场结算记录")
    @PostMapping("/leaveSettlement")
    public ResponseResult<MeterConsumptionLeaveDTO> leaveSettlement(@RequestBody @Valid MeterConsumptionLeaveReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionLeaveService.leaveSettlement(reqDTO));
    }

    @ApiOperation(value = "水电费离场结算记录-离场结算-计算金额", tags = "水电费离场结算记录")
    @PostMapping("/computeAmount")
    public ResponseResult<ChargeDetail> computeAmount(@RequestBody @Valid ComputeAmountDTO reqDTO) {
        return ResponseResult.success(meterConsumptionLeaveService.computeAmount(reqDTO));
    }

    @ApiOperation(value = "水电费离场结算记录-结算缴费", tags = "水电费离场结算记录")
    @PostMapping("/settlementPay")
    public ResponseResult<Boolean> settlementPay(@RequestBody @Valid SettlementPayReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionLeaveService.settlementPay(reqDTO));
    }


    @ApiOperation(value = "水电费离场结算记录-根据ID查询", tags = "水电费离场结算记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterConsumptionLeaveDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterConsumptionLeaveService.getRentContractLeaveById(id));
    }


    @ApiOperation(value = "水电费离场结算记录-分页查询", tags = "水电费离场结算记录")
    @PostMapping("/listRentContractLeavePage")
    public ResponseResult<PageInfo<RentContractLeaveListVO>> listRentContractLeavePage(@RequestBody @Valid RentContractLeaveQuery query) {
        return ResponseResult.success(meterConsumptionLeaveService.listRentContractLeavePage(query));
    }

    @ApiOperation(value = "水电费离场结算记录-导出", tags = "水电费离场结算记录")
    @PostMapping("/export")
    public void export(@RequestBody @Valid RentContractLeaveQuery query) {
        meterConsumptionLeaveService.export(query);
    }
}

