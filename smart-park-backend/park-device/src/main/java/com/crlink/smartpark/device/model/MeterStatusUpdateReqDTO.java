package com.crlink.smartpark.device.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MeterStatusUpdateReqDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("状态")
    private Integer usingStatus;
}
