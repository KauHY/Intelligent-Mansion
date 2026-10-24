package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 水电表每小时读数
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dev_hour_reading")
@ApiModel(value = "HourReading数据库对象", description = "水电表每小时读数")
public class HourReading implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "5水表，6电表")
    private Integer deviceType;

    @ApiModelProperty(value = "开始读数")
    private BigDecimal startReadingNum;

    @ApiModelProperty(value = "截止读数")
    private BigDecimal endReadingNum;

    @ApiModelProperty(value = "用量")
    private BigDecimal consumption;

    @ApiModelProperty(value = "抄表日期")
    private LocalDate readingDate;

    @ApiModelProperty(value = "抄表小时，24H")
    private Integer readingHour;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime readingDateTime;


}
