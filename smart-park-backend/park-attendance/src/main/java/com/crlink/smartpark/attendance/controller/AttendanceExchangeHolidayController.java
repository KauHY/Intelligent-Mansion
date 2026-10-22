package com.crlink.smartpark.attendance.controller;

import com.crlink.smartpark.attendance.dto.AttendanceExchangeHolidayDTO;
import com.crlink.smartpark.attendance.service.AttendanceExchangeHolidayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * park-attendance
 * 节假日调休
 *
 */
@Slf4j
@RestController
@RequestMapping("/attendance-exchange-holiday")
@Validated
@Api(tags = "节假日调休")
public class AttendanceExchangeHolidayController {

    @Autowired
    private AttendanceExchangeHolidayService attendanceExchangeHolidayService;

    @ApiOperation(value = "节假日调休-获取所有调休记录")
    @PostMapping("/getAllExchangeHolidays")
    public List<AttendanceExchangeHolidayDTO> getAllExchangeHolidays() {
        return attendanceExchangeHolidayService.getAllExchangeHolidays();
    }
}

