package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.dto.AttendanceMonthRecordDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceMonthRecordQuery;
import com.crlink.smartpark.attendance.service.AttendanceMonthRecordService;
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
 * 考勤月度统计
 * */
@Slf4j
@RestController
@RequestMapping("/attendance-month-record")
@Validated
@Api(tags = "考勤管理")
public class AttendanceMonthRecordController {

    @Autowired
    private AttendanceMonthRecordService attendanceMonthRecordService;

    @ApiOperation(value = "月度考勤-分页查询", tags = "月度考勤")
    @PostMapping("/listAttendanceMonthRecordPage")
    public ResponseResult<PageInfo<AttendanceMonthRecordDTO>> listAttendanceMonthRecordPage(@RequestBody @Valid AttendanceMonthRecordQuery query) {
        return ResponseResult.success(attendanceMonthRecordService.listAttendanceMonthRecordPage(query));
    }

    @ApiOperation(value = "月度考勤-导出", tags = "月度考勤")
    @PostMapping("/export")
    public void export(@RequestBody @Valid AttendanceMonthRecordQuery query) {
        attendanceMonthRecordService.export(query);
    }

}

