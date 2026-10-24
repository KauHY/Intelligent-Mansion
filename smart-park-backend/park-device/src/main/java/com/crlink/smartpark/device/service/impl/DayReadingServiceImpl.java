package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.device.entity.DayReading;
import com.crlink.smartpark.device.mapper.DayReadingMapper;
import com.crlink.smartpark.device.service.DayReadingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


/**
 * 水电表每天读数 服务实现类
 * */
@Service
public class DayReadingServiceImpl extends ServiceImpl<DayReadingMapper, DayReading> implements DayReadingService {

    @Override
    public List<DayReading> queryMaxAllRecord(LocalDate start, LocalDate end) {
        return baseMapper.queryMaxAllRecord(start, end);
    }

    @Override
    public BigDecimal accumulatedDayCount(LocalDate startDate, LocalDate endDate, Collection<String> deviceIds) {
        if (CollUtil.isEmpty(deviceIds)) {
            return BigDecimal.ZERO;
        }
        List<DayReading> list = this.lambdaQuery().select(DayReading::getId, DayReading::getConsumption)
                .ge(DayReading::getReadingDate, startDate).lt(DayReading::getReadingDate, endDate)
                .in(DayReading::getDeviceId, deviceIds).list();
        BigDecimal result = list.parallelStream().map(DayReading::getConsumption).reduce(BigDecimal.ZERO, (a, b) -> NumberUtil.add(a, b));
        return result;
    }
}
