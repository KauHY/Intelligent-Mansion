package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.entity.MonthReading;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * <p>
 * 水电表每月读数 服务类
 * </p>
 * */
public interface MonthReadingService extends IService<MonthReading> {

    BigDecimal accumulatedMonthCount(LocalDateTime startTime, LocalDateTime endTime, Collection<String> deviceIds);
}
