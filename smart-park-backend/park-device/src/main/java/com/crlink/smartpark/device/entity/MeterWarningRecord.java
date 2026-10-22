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
import java.time.LocalDateTime;

/**
 * <p>
 * 仪表异常告警记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dev_meter_warning_record")
@ApiModel(value = "MeterWarningRecord数据库对象", description = "仪表异常告警记录")
public class MeterWarningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String deviceId;
    private String paramId;
    private String paramName;
    private String paramValue;

    private Integer deviceType;

    @ApiModelProperty(value = "告警事件")
    private Integer warningCode;

    @ApiModelProperty(value = "告警等级")
    private Integer warningLevel;

    @ApiModelProperty(value = "告警时间")
    private LocalDateTime warningTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
