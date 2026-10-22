package com.crlink.smartpark.inspection.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class InspectionTaskDetailVO implements Serializable {
    private String taskId;
    @ApiModelProperty(value = "巡检任务")
    private String taskName;

    @ApiModelProperty(value = "巡检点名称")
    private String pointName;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime updateTime;
}
