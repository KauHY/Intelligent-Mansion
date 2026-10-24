package com.crlink.smartpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.device.entity.DayReading;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 水电表每天读数 Mapper 接口
 * </p>
 * */
public interface DayReadingMapper extends BaseMapper<DayReading> {
    List<DayReading> queryMaxAllRecord(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
