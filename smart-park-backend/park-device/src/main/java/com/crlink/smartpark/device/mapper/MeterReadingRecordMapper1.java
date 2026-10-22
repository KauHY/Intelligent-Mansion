package com.crlink.smartpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.device.dto.MeterConsumptionRecordDTO;
import com.crlink.smartpark.device.dto.MeterReadingDTO;
import com.crlink.smartpark.device.dto.MeterReadingRecordDTO;
import com.crlink.smartpark.device.entity.MeterReadingRecord;
import com.crlink.smartpark.device.model.query.MeterConsumptionRecordQuery;
import com.crlink.smartpark.device.model.query.MeterReadingRecordQuery;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 抄表记录 Mapper 接口
 * </p>
 * */
public interface MeterReadingRecordMapper1 extends BaseMapper<MeterReadingRecord> {
    List<MeterReadingRecordDTO> listMeterReadingRecordPage(@Param("q") MeterReadingRecordQuery query);

    List<MeterConsumptionRecordDTO> listMeterConsumptionRecordPage(@Param("q") MeterConsumptionRecordQuery query);

    List<MeterReadingDTO> queryMaxRecord(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("meterIds") Set<String> meterIds);

    List<MeterReadingDTO> queryMinRecord(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("meterIds") Set<String> meterIds);

}
