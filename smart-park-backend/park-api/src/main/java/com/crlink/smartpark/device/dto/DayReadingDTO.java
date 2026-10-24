package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 水电表每天读数
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DayReading响应对象", description = "水电表每天读数")
public class DayReadingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "设备id，设备唯一标识")
    private String deviceId;

    @ApiModelProperty(value = "5水表，6电表")
    private String deviceType;

    @ApiModelProperty(value = "本次读数")
    private BigDecimal reading;

    @ApiModelProperty(value = "用量")
    private BigDecimal consumption;

    @ApiModelProperty(value = "抄表日期")
    private LocalDate readingDate;

    @ApiModelProperty(value = "抄表时间")
    private LocalTime readingTime;

    private LocalDateTime createTime;


}
