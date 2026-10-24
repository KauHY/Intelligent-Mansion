package com.crlink.smartpark.contract.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.model.ChargeStaReqDTO;
import com.crlink.smartpark.contract.model.ChargeStaTrendDTO;
import com.crlink.smartpark.contract.model.ChargeTopStatisticDTO;
import com.crlink.smartpark.contract.model.vo.*;
import com.crlink.smartpark.contract.service.RentContractBillService;
import com.crlink.smartpark.contract.service.RentLateFeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Slf4j
@Api(tags = "报表分析-收费统计")
@RestController
@RequestMapping("/statement/feeStatistic")
public class FeeStatisticController {

    @Autowired
    private RentLateFeeService rentLateFeeService;

    @Autowired
    private RentContractBillService rentContractBillService;

    @ApiOperation(value = "收费统计-逾期客户TOP5", tags = "收费统计")
    @PostMapping("/lateCustomers")
    public ResponseResult<List<LateCustomerVO>> lateCustomers(@RequestBody @Valid ChargeStaReqDTO reqDTO) {
        return ResponseResult.success(rentLateFeeService.lateCustomers(reqDTO));
    }

    @ApiOperation(value = "收费统计-逾期趋势", tags = "收费统计")
    @PostMapping("/overdueTrend")
    public ResponseResult<List<OverDueTrendVO>> overdueTrend(@RequestBody @Valid ChargeStaTrendDTO reqDTO) {
        return ResponseResult.success(rentLateFeeService.overdueTrend(reqDTO));
    }


    @ApiOperation(value = "收费统计-缴费趋势", tags = "收费统计")
    @PostMapping("/paymentTrend")
    public ResponseResult<List<PayTrendVO>> paymentTrend(@RequestBody @Valid ChargeStaTrendDTO reqDTO) {
        return ResponseResult.success(rentContractBillService.paymentTrend(reqDTO));
    }

    @ApiOperation(value = "收费统计-收入分布", tags = "收费统计")
    @PostMapping("/incomeDistribution")
    public ResponseResult<IncomeDistributionVO> incomeDistribution(@RequestBody @Valid ChargeStaReqDTO reqDTO) {
        reqDTO.setChargeType(null);
        return ResponseResult.success(rentContractBillService.incomeDistribution(reqDTO));
    }

    @ApiOperation(value = "收费统计-顶部统计", tags = "收费统计")
    @PostMapping("/topStatistic")
    public ResponseResult<FeeTopStatisticVO> topStatistic(@RequestBody @Valid ChargeTopStatisticDTO reqDTO) {
        return ResponseResult.success(rentContractBillService.topStatistic(reqDTO));
    }
}
