package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.controller.response.WarningStatisticsVO;
import com.crlink.smartpark.device.controller.response.WarningTrendVO;
import com.crlink.smartpark.device.dto.MeterWarningRecordDTO;
import com.crlink.smartpark.device.service.WarningRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * 3D大屏的接口
 * 抄表记录
 * */
@Slf4j
@RestController
@RequestMapping("/3d/meterWarning")
@Api(tags = "3D大屏-报警接口")
@Validated
public class Warning3dController {

    @Autowired
    private WarningRecordService warningRecordService;

    @ApiOperation(value = "12报警汇总统计")
    @GetMapping("/todayTotalStatistics")
    public ResponseResult<List<WarningStatisticsVO>> getTodayTotalStatistics(@RequestParam("parkOrgId") String parkOrgId) {
        List<WarningStatisticsVO> result = warningRecordService.getTodayTotalStatistics(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "21报警趋势图统计")
    @GetMapping("/trend")
    public ResponseResult<List<WarningTrendVO>> getMeterReadingTrend(@RequestParam("parkOrgId") String parkOrgId, @RequestParam("time") LocalDate localDate) {
        List<WarningTrendVO> result = warningRecordService.getWarningTrend(parkOrgId, localDate);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "22表计告警")
    @GetMapping("/meterWarningPage")
    public ResponseResult<PageInfo<MeterWarningRecordDTO>> meterWarningPage(@RequestParam("parkOrgId") String parkOrgId, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        PageInfo<MeterWarningRecordDTO> result = warningRecordService.meterWarningPage(parkOrgId, pageNum, pageSize);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "15当前报警统计")
    @GetMapping("/totalStatistics")
    public ResponseResult<List<Integer>> getTotalStatistics(@RequestParam("parkOrgId") String parkOrgId) {
        List<Integer> result = warningRecordService.getTotalStatistics(parkOrgId);
        return ResponseResult.success(result);
    }


}

