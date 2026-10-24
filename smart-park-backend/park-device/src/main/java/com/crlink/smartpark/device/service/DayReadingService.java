package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.entity.DayReading;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 水电表每天读数 服务类
 * </p>
 * */
public interface DayReadingService extends IService<DayReading> {
    public List<DayReading> queryMaxAllRecord(LocalDate start, LocalDate end);

    BigDecimal accumulatedDayCount(LocalDate startDate, LocalDate endDate, Collection<String> deviceIds);
}
