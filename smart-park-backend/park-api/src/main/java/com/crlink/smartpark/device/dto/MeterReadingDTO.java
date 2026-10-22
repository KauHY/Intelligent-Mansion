package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 抄表记录
 * </p>
 * */
@Data
public class MeterReadingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表id")
    private String meterId;

    @ApiModelProperty(value = "抄表读数")
    private BigDecimal meterReading;

    @ApiModelProperty(value = "修正后读数")
    private BigDecimal correctedReading;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime readingTime;


}
