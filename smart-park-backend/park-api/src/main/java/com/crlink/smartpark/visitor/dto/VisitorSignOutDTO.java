package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 签出DTO
 *
 */
@Data
public class VisitorSignOutDTO implements Serializable {
    private static final long serialVersionUID = 7016013007270171684L;

    /**
     * 预约记录ID
     */
    @ApiModelProperty(value = "预约记录ID")
    private String id;

    @ApiModelProperty(value = "签出时间")
    private LocalDateTime signOutTime;

}
