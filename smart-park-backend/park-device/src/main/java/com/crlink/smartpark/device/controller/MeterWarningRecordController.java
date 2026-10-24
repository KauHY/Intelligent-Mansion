package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.MeterWarningRecordDTO;
import com.crlink.smartpark.device.model.MeterWarningRecordReqDTO;
import com.crlink.smartpark.device.model.query.MeterWarningRecordQuery;
import com.crlink.smartpark.device.service.MeterWarningRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * park-customer
 * 仪表异常告警记录
 * */
@Slf4j
@RestController
@RequestMapping("/meter-warning-record")
@Validated
@Api(tags = "智慧能源-异常告警")
public class MeterWarningRecordController {

    @Autowired
    private MeterWarningRecordService meterWarningRecordService;


    @ApiOperation(value = "仪表异常告警记录-新增", tags = "仪表异常告警记录")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MeterWarningRecordReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(meterWarningRecordService.saveOrUpdateMeterWarningRecord(reqDTO));
    }


    @ApiOperation(value = "仪表异常告警记录-删除", tags = "仪表异常告警记录")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterWarningRecordService.removeById(id));
    }


    @ApiOperation(value = "仪表异常告警记录-根据ID查询", tags = "仪表异常告警记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterWarningRecordDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterWarningRecordService.getMeterWarningRecordById(id));
    }


    @ApiOperation(value = "仪表异常告警记录-修改", tags = "仪表异常告警记录")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MeterWarningRecordReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meterWarningRecordService.saveOrUpdateMeterWarningRecord(reqDTO));
    }


    @ApiOperation(value = "仪表异常告警记录-分页查询", tags = "仪表异常告警记录")
    @PostMapping("/listMeterWarningRecordPage")
    public ResponseResult<PageInfo<MeterWarningRecordDTO>> listMeterWarningRecordPage(@RequestBody @Valid MeterWarningRecordQuery query) {
        return ResponseResult.success(meterWarningRecordService.listMeterWarningRecordPage(query));
    }

}

