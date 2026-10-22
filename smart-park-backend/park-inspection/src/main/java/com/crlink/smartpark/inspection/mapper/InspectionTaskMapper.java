package com.crlink.smartpark.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.inspection.entity.InspectionTask;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 * */
public interface InspectionTaskMapper extends BaseMapper<InspectionTask> {
    public List<InspectionTask> queryPendingTasksByEmployeeId(@Param("employeeId") String employeeId, @Param("currentDate") LocalDate currentDate);

    List<InspectionTask> queryHistoryTasksByEmployeeId(@Param("employeeId") String employeeId, @Param("currentDate") LocalDate currentDate);
}
