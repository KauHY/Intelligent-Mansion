package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.device.entity.MonthReading;
import com.crlink.smartpark.device.mapper.MonthReadingMapper;
import com.crlink.smartpark.device.service.MonthReadingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


/**
 * 水电表每月读数 服务实现类
 * */
@Service
public class MonthReadingServiceImpl extends ServiceImpl<MonthReadingMapper, MonthReading> implements MonthReadingService {

    @Override
    public BigDecimal accumulatedMonthCount(LocalDateTime startTime, LocalDateTime endTime, Collection<String> deviceIds) {
        if (CollUtil.isEmpty(deviceIds)) {
            return BigDecimal.ZERO;
        }
        List<MonthReading> list = this.lambdaQuery().select(MonthReading::getId, MonthReading::getConsumption)
                .ge(MonthReading::getReadingDateTime, startTime).lt(MonthReading::getReadingDateTime, endTime)
                .in(MonthReading::getDeviceId, deviceIds).list();
        BigDecimal result = list.parallelStream().map(MonthReading::getConsumption).reduce(BigDecimal.ZERO, (a, b) -> NumberUtil.add(a, b));
        return result;
    }
}
