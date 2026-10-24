package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.dto.AttendanceRecordDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceRecordQuery;
import com.crlink.smartpark.attendance.service.AttendanceRecordService;
import com.crlink.smartpark.common.web.ResponseResult;
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
 * park-attendance
 * 员工考勤记录
 * */
@Slf4j
@RestController
@RequestMapping("/attendance-record")
@Validated
@Api(tags = "考勤管理")
public class AttendanceRecordController {

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @ApiOperation(value = "每日考勤-分页查询", tags = "每日考勤")
    @PostMapping("/listAttendanceRecordPage")
    public ResponseResult<PageInfo<AttendanceRecordDTO>> listAttendanceRecordPage(@RequestBody @Valid AttendanceRecordQuery query) {
        return ResponseResult.success(attendanceRecordService.listAttendanceRecordPage(query));
    }

    @ApiOperation(value = "每日考勤-导出", tags = "每日考勤")
    @PostMapping("/export")
    public void export(@RequestBody @Valid AttendanceRecordQuery query) {
        attendanceRecordService.export(query);
    }

}

