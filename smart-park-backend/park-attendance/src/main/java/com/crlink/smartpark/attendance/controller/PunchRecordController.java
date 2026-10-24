package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.model.query.PunchRecordQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.attendance.dto.PunchRecordDTO;
import com.crlink.smartpark.attendance.service.PunchRecordService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-attendance
 * 打卡记录
 * */
@Slf4j
@RestController
@RequestMapping("/punch-record")
@Validated
@Api(tags = "考勤管理")
public class PunchRecordController {

    @Autowired
    private PunchRecordService punchRecordService;

    @ApiOperation(value = "打卡记录-分页查询", tags = "打卡记录")
    @PostMapping("/listPunchRecordPage")
    public ResponseResult<PageInfo<PunchRecordDTO>> listPunchRecordPage(@RequestBody PunchRecordQuery query){
        return ResponseResult.success(punchRecordService.listPunchRecordPage(query));
    }

    @ApiOperation(value = "打卡记录-导出", tags = "打卡记录")
    @PostMapping("/export")
    public void export(@RequestBody PunchRecordQuery query){
        punchRecordService.export(query);
    }

}

