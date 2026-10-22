package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.device.entity.HourReading;
import com.crlink.smartpark.device.mapper.HourReadingMapper;
import com.crlink.smartpark.device.service.HourReadingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * 水电表每小时读数 服务实现类
 * */
@Service
public class HourReadingServiceImpl extends ServiceImpl<HourReadingMapper, HourReading> implements HourReadingService {
    @Override
    public BigDecimal accumulatedHourCount(LocalDate readingDate, Integer hour, Collection<String> deviceIds) {
        if (CollUtil.isEmpty(deviceIds)) {
            return BigDecimal.ZERO;
        }
        List<HourReading> list = this.lambdaQuery().select(HourReading::getId, HourReading::getConsumption)
                .eq(HourReading::getReadingDate, readingDate)
                .eq(Objects.nonNull(hour), HourReading::getReadingHour, hour)
                .in(HourReading::getDeviceId, deviceIds).list();
        BigDecimal result = list.parallelStream().map(HourReading::getConsumption).reduce(BigDecimal.ZERO, (a, b) -> NumberUtil.add(a, b));
        return result;
    }
}
