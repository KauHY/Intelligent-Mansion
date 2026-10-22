package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 水电表每月读数
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MonthReading响应对象", description = "水电表每月读数")
public class MonthReadingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "设备id，设备唯一标识")
    private String deviceId;

    @ApiModelProperty(value = "5水表，6电表")
    private Integer deviceType;

    @ApiModelProperty(value = "本次读数")
    private BigDecimal reading;

    @ApiModelProperty(value = "用量")
    private BigDecimal consumption;

    @ApiModelProperty(value = "抄表年份")
    private Integer year;

    @ApiModelProperty(value = "抄表月份")
    private Integer month;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime readingTime;

    private LocalDateTime createTime;


}
