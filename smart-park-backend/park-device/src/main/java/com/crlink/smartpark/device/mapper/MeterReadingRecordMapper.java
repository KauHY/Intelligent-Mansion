package com.crlink.smartpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.device.dto.MeterReadingDTO;
import com.crlink.smartpark.device.entity.MeterReadingRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 每天抄表记录 Mapper 接口
 * </p>
 * */
public interface MeterReadingRecordMapper extends BaseMapper<MeterReadingRecord> {
    //List<MeterReadingRecordDTO> listMeterReadingRecordPage(@Param("q") MeterReadingRecordQuery query);


    List<MeterReadingDTO> queryMaxRecord(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("meterIds") Set<String> meterIds);


    List<MeterReadingDTO> queryMinRecord(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("meterIds") Set<String> meterIds);
}
