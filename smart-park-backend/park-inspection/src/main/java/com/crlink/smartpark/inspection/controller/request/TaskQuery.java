package com.crlink.smartpark.inspection.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskQuery extends BaseQuery {
    private String orgId;
    @ApiModelProperty("巡检任务")
    private String taskName;
    @ApiModelProperty("巡检状态")
    private Integer taskStatus;
    @ApiModelProperty("巡检人")
    private String employeeName;

    @ApiModelProperty("巡检时间")
    private LocalDateTime completeStart;
    private LocalDateTime completeEnd;


}
