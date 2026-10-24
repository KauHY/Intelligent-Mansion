package com.crlink.smartpark.contract.controller.meterfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.MeterConsumptionFeeDTO;
import com.crlink.smartpark.contract.model.MeterConsumptionFeeReqDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionFeeQuery;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionFeeVO;
import com.crlink.smartpark.contract.service.MeterConsumptionFeeService;
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
 * 水电费用
 * */
@Slf4j
@RestController
@RequestMapping("/meterConsumptionFee")
@Api(tags = "水电费用")
@Validated
public class MeterConsumptionFeeController {

    @Autowired
    private MeterConsumptionFeeService meterConsumptionFeeService;

    @ApiOperation(value = "水电费用-批量修改收款日期", tags = "水电费用")
    @PostMapping("/modifyDate")
    public ResponseResult<Boolean> modifyDate(@RequestBody @Valid MeterConsumptionFeeReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionFeeService.modifyDate(reqDTO));
    }

    @ApiOperation(value = "水电费用-根据ID查询详情", tags = "水电费用")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterConsumptionFeeVO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterConsumptionFeeService.getMeterConsumptionFee(id));
    }

    @ApiOperation(value = "水电费用-批量转为已出", tags = "水电费用")
    @PostMapping("/transform")
    public ResponseResult<Boolean> transform(@RequestBody @Valid MeterConsumptionFeeReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionFeeService.transform(reqDTO));
    }


    @ApiOperation(value = "水电费用-分页查询", tags = "水电费用")
    @PostMapping("/listMeterFeePage")
    public ResponseResult<PageInfo<MeterConsumptionFeeDTO>> listMeterConsumptionFeePage(@RequestBody @Valid MeterConsumptionFeeQuery query) {
        return ResponseResult.success(meterConsumptionFeeService.listMeterConsumptionFeePage(query));
    }


    @ApiOperation(value = "水电费用-导出", tags = "水电费用")
    @PostMapping("/export")
    public void exportCashFee(@RequestBody @Valid MeterConsumptionFeeQuery reqDTO) {
        meterConsumptionFeeService.exportFee(reqDTO);
    }

    @ApiOperation(value = "水电费用-生成", tags = "水电费用")
    @PostMapping("/create")
    public void create(@RequestParam(value = "month") String month) {
        meterConsumptionFeeService.create(month);
    }

    @ApiOperation(value = "水电费用-生成滞纳金", tags = "水电费用")
    @PostMapping("/createLate")
    public void create() {
        meterConsumptionFeeService.createLate();
    }

}

