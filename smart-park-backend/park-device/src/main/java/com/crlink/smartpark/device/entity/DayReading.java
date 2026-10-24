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
import java.time.LocalTime;

/**
 * <p>
 * 水电表每天读数
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dev_day_reading")
@ApiModel(value = "DayReading数据库对象", description = "水电表每天读数")
public class DayReading implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "设备id，设备唯一标识")
    private String deviceId;

    @ApiModelProperty(value = "5水表，6电表")
    private String deviceType;

    @ApiModelProperty(value = "开始读数")
    private BigDecimal startReadingNum;

    @ApiModelProperty(value = "结束读数")
    private BigDecimal endReadingNum;

    @ApiModelProperty(value = "用量")
    private BigDecimal consumption;

    @ApiModelProperty(value = "抄表日期")
    private LocalDate readingDate;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime readingDateTime;
    private LocalTime readingTime;

    private LocalDateTime createTime;

}
