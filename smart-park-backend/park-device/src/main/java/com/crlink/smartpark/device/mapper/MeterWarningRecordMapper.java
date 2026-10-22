package com.crlink.smartpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.device.dto.MeterWarningRecordDTO;
import com.crlink.smartpark.device.entity.MeterWarningRecord;
import com.crlink.smartpark.device.model.query.MeterWarningRecordQuery;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 仪表异常告警记录 Mapper 接口
 * </p>
 * */
public interface MeterWarningRecordMapper extends BaseMapper<MeterWarningRecord> {
    List<MeterWarningRecordDTO> listMeterWarningRecordPage(@Param("q") MeterWarningRecordQuery query);

    Integer countMeterWarning(@Param("parkOrgId") String parkOrgId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") Integer status);
}
