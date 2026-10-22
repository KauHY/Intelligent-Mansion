package com.crlink.smartpark.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.inspection.dto.resultmap.CircuitPointEntity;
import com.crlink.smartpark.inspection.entity.InspectionCircuit;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 巡检路线 Mapper 接口
 * </p>
 * */
public interface InspectionCircuitMapper extends BaseMapper<InspectionCircuit> {
    List<CircuitPointEntity> queryPointByCircuitIds(@Param("circuitIds") Collection<String> circuitIds);
}
