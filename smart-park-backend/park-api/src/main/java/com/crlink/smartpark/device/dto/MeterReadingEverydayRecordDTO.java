package com.crlink.smartpark.device.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 每天抄表记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeterReadingEverydayRecord响应对象", description="每天抄表记录")
public class MeterReadingEverydayRecordDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "仪表id")
    private String meterId;

    @ApiModelProperty(value = "抄表读数")
    private BigDecimal meterReading;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime readingTime;

    @ApiModelProperty(value = "修正后读数")
    private BigDecimal correctedReading;

    @ApiModelProperty(value = "抄表年份")
    private Integer year;

    @ApiModelProperty(value = "抄表月份")
    private Integer month;

    @ApiModelProperty(value = "抄表天")
    private Integer day;

    @ApiModelProperty(value = "备注")
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
