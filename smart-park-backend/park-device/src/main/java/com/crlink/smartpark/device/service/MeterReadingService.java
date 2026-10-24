package com.crlink.smartpark.device.service;

import com.crlink.smartpark.device.controller.response.*;

import java.time.LocalDate;
import java.util.List;

public interface MeterReadingService {

    int YEAR_TYPE = 1;
    int MONTH_TYPE = 2;
    int DAY_TYPE = 3;

    /**
     * 能耗设备统计
     *
     * @param parkOrgId
     * @param localDate
     * @return
     */
    MeterReadingStatisticsVO getMeterReadingStatistics(String parkOrgId, LocalDate localDate);

    /**
     * 水电能耗总数统计
     *
     * @param parkOrgId
     * @return
     */
    MeterReadingTotalStatisticsVO getMeterReadingTotalStatistics(String parkOrgId);

    /**
     * 能耗趋势图
     *
     * @param parkOrgId
     * @param timeType
     * @return
     */
    List<MeterReadingTrendVO> getMeterReadingTrend(String parkOrgId, Integer timeType);

    List<MeterDistributionVO> getDistribution(String parkOrgId, Integer type);

    /**
     * 获取该月份的用水或用电汇总
     *
     * @param parkOrgId
     * @param localDate
     * @param type
     * @return
     */
    ReadingStatisticsVO getMonthConsumption(String parkOrgId, LocalDate localDate, Integer type);

    List<ElectricityConsumptionVO> getMonthElectricityConsumption(String parkOrgId, LocalDate localDate);

    YearMonthStaticVO getYearMonthStatics(String parkOrgId, LocalDate localDate, Integer type);

    /**
     * 根据设备编码统计日总量、月总量、年总量
     * @param deviceId 设备编码
     * @param type 设备类型
     * @return 返回设备能耗统计
     */
    DeviceSummaryVO summary(String deviceId, Integer type);
}
