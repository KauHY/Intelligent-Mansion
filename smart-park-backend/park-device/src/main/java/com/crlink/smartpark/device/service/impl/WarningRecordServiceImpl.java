package com.crlink.smartpark.device.service.impl;

import com.crlink.smartpark.device.controller.response.WarningStatisticsVO;
import com.crlink.smartpark.device.controller.response.WarningTrendVO;
import com.crlink.smartpark.device.dto.MeterWarningRecordDTO;
import com.crlink.smartpark.device.model.query.MeterWarningRecordQuery;
import com.crlink.smartpark.device.service.FireWarningRecordService;
import com.crlink.smartpark.device.service.MeterWarningRecordService;
import com.crlink.smartpark.device.service.WarningRecordService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


/**
 * 仪表异常告警记录 服务实现类
 * */
@Service
public class WarningRecordServiceImpl implements WarningRecordService {
    @Autowired
    private MeterWarningRecordService meterWarningRecordService;

    @Autowired
    private FireWarningRecordService fireWarningRecordService;

    @Override
    public PageInfo<MeterWarningRecordDTO> meterWarningPage(String parkOrgId, Integer pageNum, Integer pageSize) {
        MeterWarningRecordQuery query = new MeterWarningRecordQuery();
        query.setParkOrgId(parkOrgId);
        //query.setDeviceType(6); //电表
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        PageInfo<MeterWarningRecordDTO> result = meterWarningRecordService.list3dMeterWarningRecordPage(query);
        return result;
    }

    @Override
    public List<WarningTrendVO> getWarningTrend(String parkOrgId, LocalDate localDate) {
        LocalDateTime now = LocalDateTime.now();
        List<WarningTrendVO> result = Lists.newArrayList();
        for (int i = 0; i < 12; i++) {
            LocalDateTime temp = now.withMonth(i + 1);
            LocalDateTime sTime = temp.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
            LocalDateTime eTime = temp.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            Integer meterWarningCount = meterWarningRecordService.countMeterWarning(parkOrgId, sTime, eTime, null);
            Integer fireWarningCount = fireWarningRecordService.countFireWarning(parkOrgId, sTime, eTime, null);
            result.add(new WarningTrendVO(meterWarningCount, fireWarningCount, meterWarningCount + fireWarningCount, temp.getMonthValue()));
        }
        return result;
    }

    @Override
    public List<Integer> getTotalStatistics(String parkOrgId) {
        List<Integer> result = Lists.newArrayList();
        Integer meterWarningCount = meterWarningRecordService.countMeterWarning(parkOrgId, null, null, null);
        result.add(meterWarningCount);
        Integer fireWarningCount = fireWarningRecordService.countFireWarning(parkOrgId, null, null, null);
        result.add(fireWarningCount);
        return result;
    }

    @Override
    public List<WarningStatisticsVO> getTodayTotalStatistics(String parkOrgId) {
        List<WarningStatisticsVO> result = Lists.newArrayList();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.with(LocalTime.MIN);//当天00:00:00
        LocalDateTime endTime = now.with(LocalTime.MAX);  //当天23:59:59
        Integer handledMeterCount = meterWarningRecordService.countMeterWarning(parkOrgId, startTime, endTime, 1);
        Integer unhandledMeterCount = meterWarningRecordService.countMeterWarning(parkOrgId, startTime, endTime, 0);
        result.add(new WarningStatisticsVO(handledMeterCount, unhandledMeterCount, handledMeterCount + unhandledMeterCount));
        Integer handledFireCount = fireWarningRecordService.countFireWarning(parkOrgId, null, null, 1);
        Integer unhandledFireCount = fireWarningRecordService.countFireWarning(parkOrgId, null, null, 0);
        result.add(new WarningStatisticsVO(handledFireCount, unhandledFireCount, handledFireCount + unhandledFireCount));
        return result;
    }

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime lastYear = LocalDateTime.of(localDate.minusYears(1).withDayOfMonth(1), LocalTime.of(0, 0, 0));
        LocalDateTime localDateTime = lastYear.plusMonths(1).minusSeconds(1);
        System.out.println(localDateTime);
    }
}
