package com.crlink.smartpark.attendance.model.vo;

import com.crlink.smartpark.attendance.entity.PunchRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AttendanceRecordDateVO {

    @ApiModelProperty(value = "考勤结果，1：正常 2：异常，为null代表该天结果未生成")
    private Integer attendanceResult;

    @ApiModelProperty(value = "打卡记录")
    private List<PunchRecord> punchRecordList;
}
