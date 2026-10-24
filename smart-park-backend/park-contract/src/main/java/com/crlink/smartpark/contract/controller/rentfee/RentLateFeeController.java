package com.crlink.smartpark.contract.controller.rentfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.RentLateFeeDTO;
import com.crlink.smartpark.contract.model.ChargeStaReqDTO;
import com.crlink.smartpark.contract.model.ChargeStaTrendDTO;
import com.crlink.smartpark.contract.model.query.RentLateFeeQuery;
import com.crlink.smartpark.contract.model.request.RentLateFeeReqDTO;
import com.crlink.smartpark.contract.model.vo.LateCustomerVO;
import com.crlink.smartpark.contract.model.vo.OverDueTrendVO;
import com.crlink.smartpark.contract.service.RentLateFeeService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * park-customer
 * 租赁滞纳金费用
 * */
@Api(tags = "租赁合同收费模块-滞纳金")
@Slf4j
@RestController
@RequestMapping("/rentLateFee")
@Validated
public class RentLateFeeController {

    @Autowired
    private RentLateFeeService rentLateFeeService;

    @ApiOperation(value = "租赁滞纳金费用-导出", tags = "租赁滞纳金费用")
    @PostMapping("/export")
    public void export(@RequestBody @Valid RentLateFeeQuery reqDTO) {
        rentLateFeeService.export(reqDTO);
    }


    @ApiOperation(value = "租赁滞纳金费用-免除", tags = "租赁滞纳金费用")
    @PostMapping("/prevent")
    public ResponseResult<Boolean> prevent(@RequestBody @Valid RentLateFeeReqDTO reqDTO) {
        return ResponseResult.success(rentLateFeeService.saveOrUpdateRentLateFee(reqDTO));
    }


    @ApiOperation(value = "租赁滞纳金费用-分页查询", tags = "租赁滞纳金费用")
    @PostMapping("/listRentLateFeePage")
    public ResponseResult<PageInfo<RentLateFeeDTO>> listRentLateFeePage(@RequestBody @Valid RentLateFeeQuery query){
        return ResponseResult.success(rentLateFeeService.listRentLateFeePage(query));
    }

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


}

