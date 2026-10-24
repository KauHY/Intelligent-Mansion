package com.crlink.smartpark.inspection.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InspectionPointDetail对象", description = "")
public class InspectionPointDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "任务Id")
    private String taskId;

    @ApiModelProperty(value = "巡检点名称")
    private String pointName;

    @ApiModelProperty(value = "情况")
    private Boolean state;

    @ApiModelProperty(value = "情况说明")
    private String description;

    @ApiModelProperty(value = "巡检图片")
    private String picture;

    @ApiModelProperty(value = "巡检点巡检状态")
    private Integer pointStatus;

//    private LocalDateTime updateTime;

    private String updateTime;


}
