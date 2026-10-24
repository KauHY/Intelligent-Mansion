package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.MeterConsumptionCountDTO;
import com.crlink.smartpark.device.dto.MeterConsumptionRecordDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionAnalysisReqDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionRecordQuery;
import com.crlink.smartpark.device.service.MeterConsumptionRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

/**
 * park-device
 * 水电表用量记录
 * */
@Slf4j
@RestController
@RequestMapping("/meterConsumptionRecord")
@Api(tags = "智慧能源-用量记录和用量分析")
@Validated
public class MeterConsumptionRecordController {

    @Autowired
    private MeterConsumptionRecordService meterConsumptionRecordService;


    @ApiOperation(value = "水电表用量记录-根据ID查询", tags = "水电表用量记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterConsumptionRecordDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterConsumptionRecordService.getMeterConsumptionRecordById(id));
    }

    @ApiOperation(value = "水电表用量记录-分页查询", tags = "水电表用量记录")
    @PostMapping("/listMeterConsumptionRecordPage")
    public ResponseResult<PageInfo<MeterConsumptionRecordDTO>> listMeterConsumptionRecordPage(@RequestBody @Valid MeterConsumptionRecordQuery query) {
        return ResponseResult.success(meterConsumptionRecordService.listMeterConsumptionRecordPage(query));
    }


    @ApiOperation(value = "用量记录-报表导出", tags = "抄表记录")
    @PostMapping("/consumptionRecord/download")
    public void consumptionRecordDownload(@RequestBody @Valid MeterConsumptionRecordQuery query) {
        meterConsumptionRecordService.consumptionRecordDownload(query);
    }

    @ApiOperation(value = "用量分析-用量分析", tags = "抄表记录")
    @PostMapping("/listMeterConsumptionAnalysis")
    public ResponseResult<Collection<MeterConsumptionCountDTO>> listMeterConsumptionAnalysis(@RequestBody @Valid MeterConsumptionAnalysisReqDTO query) {
        return ResponseResult.success(meterConsumptionRecordService.listMeterConsumptionAnalysis(query));
    }

    @ApiOperation(value = "用量分析-用量分析导出", tags = "抄表记录")
    @PostMapping("/analysis/download")
    public void download(@RequestBody @Valid MeterConsumptionAnalysisReqDTO query) {
        meterConsumptionRecordService.downloadAnalysis(query);
    }

}

