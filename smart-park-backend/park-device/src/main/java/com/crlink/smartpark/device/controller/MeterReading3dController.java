package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.controller.response.*;
import com.crlink.smartpark.device.service.MeterReadingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/3d/meterReading")
@Api(tags = "3D大屏-能耗接口")
@Validated
public class MeterReading3dController {

    @Autowired
    private MeterReadingService meterReadingService;


    @ApiOperation(value = "13能耗设备统计")
    @GetMapping("/statistics")
    public ResponseResult<MeterReadingStatisticsVO> getMeterReadingStatistics(@RequestParam("parkOrgId") String parkOrgId, @RequestParam("time") LocalDate localDate) {
        MeterReadingStatisticsVO result = meterReadingService.getMeterReadingStatistics(parkOrgId, localDate);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "18能耗统计")
    @GetMapping("/totalStatistics")
    public ResponseResult<MeterReadingTotalStatisticsVO> getMeterReadingTotalStatistics(@RequestParam("parkOrgId") String parkOrgId) {
        MeterReadingTotalStatisticsVO result = meterReadingService.getMeterReadingTotalStatistics(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "19能耗趋势图统计")
    @GetMapping("/trend")
    public ResponseResult<List<MeterReadingTrendVO>> getMeterReadingTrend(@RequestParam("parkOrgId") String parkOrgId, @RequestParam("timeType") Integer timeType) {
        List<MeterReadingTrendVO> result = meterReadingService.getMeterReadingTrend(parkOrgId, timeType);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "20能耗分布图")
    @GetMapping("/distribution")
    public ResponseResult<List<MeterDistributionVO>> getDistribution(@RequestParam("parkOrgId") String parkOrgId, Integer type) {
        List<MeterDistributionVO> result = meterReadingService.getDistribution(parkOrgId, type);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "31获取月份用电量或用水量汇总")
    @GetMapping("/monthConsumptionStatics")
    public ResponseResult<ReadingStatisticsVO> getMonthConsumption(@RequestParam("parkOrgId") String parkOrgId,
                                                                   @RequestParam("time") LocalDate localDate,
                                                                   Integer type) {
        ReadingStatisticsVO result = meterReadingService.getMonthConsumption(parkOrgId, localDate, type);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "32按用电类型获取此类型下用电量")
    @GetMapping("/monthElectricityConsumption")
    public ResponseResult<List<ElectricityConsumptionVO>> getMonthElectricityConsumption(@RequestParam("parkOrgId") String parkOrgId,
                                                                                         @RequestParam("time") LocalDate localDate) {
        List<ElectricityConsumptionVO> result = meterReadingService.getMonthElectricityConsumption(parkOrgId, localDate);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "33按照月份统计用电量、用水量数据")
    @GetMapping("/yearMonthStatics")
    public ResponseResult<YearMonthStaticVO> getYearMonthStatics(@RequestParam("parkOrgId") String parkOrgId,
                                                                 @RequestParam("time") LocalDate localDate,
                                                                 Integer type) {
        YearMonthStaticVO result = meterReadingService.getYearMonthStatics(parkOrgId, localDate, type);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "设备能耗统计")
    @GetMapping("/summary")
    public ResponseResult<DeviceSummaryVO> summary(@RequestParam("deviceId") String deviceId, @RequestParam("type") Integer type) {
        if (StringUtils.isBlank(deviceId)) {
            return ResponseResult.success(new DeviceSummaryVO());
        }
        return ResponseResult.success(meterReadingService.summary(deviceId, type));
    }
}

