package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.entity.HourReading;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

/**
 * <p>
 * 水电表每小时读数 服务类
 * </p>
 * */
public interface HourReadingService extends IService<HourReading> {

    BigDecimal accumulatedHourCount(LocalDate readingDate, Integer hour, Collection<String> deviceIds);
}
