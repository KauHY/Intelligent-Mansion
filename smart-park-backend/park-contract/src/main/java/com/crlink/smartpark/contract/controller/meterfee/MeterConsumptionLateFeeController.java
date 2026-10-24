package com.crlink.smartpark.contract.controller.meterfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.MeterConsumptionLateFeeDTO;
import com.crlink.smartpark.contract.model.MeterConsumptionLateFeeReqDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionLateFeeQuery;
import com.crlink.smartpark.contract.service.MeterConsumptionLateFeeService;
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

/**
 * 水电滞纳金费用
 * */
@Slf4j
@RestController
@RequestMapping("/meterConsumptionLateFee")
@Api(tags = "水电滞纳金费用")
@Validated
public class MeterConsumptionLateFeeController {

    @Autowired
    private MeterConsumptionLateFeeService meterConsumptionLateFeeService;


    @ApiOperation(value = "水电滞纳金费用-导出", tags = "水电滞纳金费用")
    @PostMapping("/export")
    public void export(@RequestBody @Valid MeterConsumptionLateFeeQuery reqDTO) {
        meterConsumptionLateFeeService.export(reqDTO);
    }


    @ApiOperation(value = "水电滞纳金费用-免除", tags = "水电滞纳金费用")
    @PostMapping("/prevent")
    public ResponseResult<Boolean> prevent(@RequestBody @Valid MeterConsumptionLateFeeReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionLateFeeService.saveOrUpdateRentLateFee(reqDTO));
    }


    @ApiOperation(value = "水电滞纳金费用-分页查询", tags = "水电滞纳金费用")
    @PostMapping("/listMeterLateFeePage")
    public ResponseResult<PageInfo<MeterConsumptionLateFeeDTO>> listMeterConsumptionLateFeePage(@RequestBody @Valid MeterConsumptionLateFeeQuery query) {
        return ResponseResult.success(meterConsumptionLateFeeService.listMeterConsumptionLateFeePage(query));
    }

}

