package com.crlink.smartpark.attendance.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.attendance.controller.request.query.FieldworkApplyRecordQuery;
import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordDTO;
import com.crlink.smartpark.attendance.service.FieldworkApplyRecordService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * park-attendance
 * 外勤申请记录
 * */
@Slf4j
@RestController
@RequestMapping("/fieldwork-apply-record")
@Validated
@Api(tags = "考勤管理")
public class FieldworkApplyRecordController {

    @Autowired
    private FieldworkApplyRecordService fieldworkApplyRecordService;


    @ApiOperation(value = "外勤申请记录-分页查询-PC", tags = "外勤申请记录")
    @PostMapping("/listFieldworkApplyRecordPage")
    public ResponseResult<PageInfo<FieldworkApplyRecordDTO>> listFieldworkApplyRecordPage(@RequestBody @Valid FieldworkApplyRecordQuery query){
        return ResponseResult.success(fieldworkApplyRecordService.listFieldworkApplyRecordPage(query));
    }

    @ApiOperation(value = "外勤申请记录-分页查询-导出", tags = "外勤申请记录")
    @PostMapping("/export")
    public void export(@RequestBody FieldworkApplyRecordQuery query){
        fieldworkApplyRecordService.export(query);
    }

}

