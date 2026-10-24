//package com.crlink.smartpark.device.entity;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.experimental.Accessors;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
///**
// * <p>
// * 抄表记录
// * </p>
// *
////// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@TableName("dev_meter_reading_record")
//@ApiModel(value = "MeterReadingRecord数据库对象", description = "抄表记录")
//public class MeterReadingRecord implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @TableId(value = "id", type = IdType.ASSIGN_ID)
//    private String id;
//
//    private String meterId;
//
//    @ApiModelProperty(value = "抄表读数")
//    private BigDecimal meterReading;
//
//    @ApiModelProperty(value = "抄表时间")
//    private LocalDateTime readingTime;
//
//
//    private Boolean deleted;
//
//}
