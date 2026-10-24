package com.crlink.smartpark.device.service;

import com.crlink.smartpark.device.controller.response.WarningStatisticsVO;
import com.crlink.smartpark.device.controller.response.WarningTrendVO;
import com.crlink.smartpark.device.dto.MeterWarningRecordDTO;
import com.github.pagehelper.PageInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 仪表异常告警记录 服务类
 * </p>
 */
public interface WarningRecordService {


    PageInfo<MeterWarningRecordDTO> meterWarningPage(String parkOrgId, Integer pageNum, Integer pageSize);

    List<WarningTrendVO> getWarningTrend(String parkOrgId, LocalDate localDate);

    List<Integer> getTotalStatistics(String parkOrgId);

    List<WarningStatisticsVO> getTodayTotalStatistics(String parkOrgId);
}
