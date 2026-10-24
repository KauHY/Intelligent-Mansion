package com.crlink.smartpark.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.inspection.dto.resultmap.PlanCircuitEntity;
import com.crlink.smartpark.inspection.entity.InspectionPlan;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 巡检计划 Mapper 接口
 * </p>
 * */
public interface InspectionPlanMapper extends BaseMapper<InspectionPlan> {
    List<PlanCircuitEntity> queryCircuitByPlanId(@Param("planIds") Collection<String> planIds);
}
