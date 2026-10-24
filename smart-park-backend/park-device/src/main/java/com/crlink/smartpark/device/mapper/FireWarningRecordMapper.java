package com.crlink.smartpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.device.dto.FireWarningRecordDTO;
import com.crlink.smartpark.device.entity.FireWarningRecord;
import com.crlink.smartpark.device.model.query.FireWarningRecordQuery;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 报警记录 Mapper 接口
 * </p>
 * */
public interface FireWarningRecordMapper extends BaseMapper<FireWarningRecord> {

    List<FireWarningRecordDTO> listFireWarningRecordPage(@Param("q") FireWarningRecordQuery query);

    Integer countFireWarning(@Param("parkOrgId") String parkOrgId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") Integer status);
}
