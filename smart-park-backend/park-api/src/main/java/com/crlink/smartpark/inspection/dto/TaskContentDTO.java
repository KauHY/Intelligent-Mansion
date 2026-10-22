package com.crlink.smartpark.inspection.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TaskContentDTO implements Serializable {

    @ApiModelProperty(value = "巡检路线名")
    private String circuitName;

    @ApiModelProperty(value = "是否有序")
    private Boolean orderly;

    @ApiModelProperty(value = "巡检点")
    private List<TaskPointDTO> taskPoints;


    @Data
    public static class TaskPointDTO implements Serializable {
        @ApiModelProperty(value = "巡检点名称")
        private String pointName;

        @ApiModelProperty(value = "巡检位置")
        private String location;
    }

}
