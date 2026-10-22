package com.crlink.smartpark.inspection.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InspectionPointDetail对象", description = "")
public class TaskPointDetailReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String id;

    @ApiModelProperty(value = "情况")
    private Boolean state;

    @ApiModelProperty(value = "情况说明")
    private String description;

    @ApiModelProperty(value = "巡检图片")
    private String picture;

}
