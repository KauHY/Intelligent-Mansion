package com.crlink.smartpark.device.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 抄表记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterReadingRecord请求对象", description = "抄表记录")
public class MeterReadingReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;


    @ApiModelProperty(value = "修正后读数", required = true)
    private BigDecimal correctedReading;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;
    //@ApiModelProperty(value = "抄表时间", required = true)
    //private LocalDateTime readingTime;

}
