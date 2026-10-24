package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 水电表用量记录
 * </p>
 * */
@Data
@TableName(value = "dev_meter_consumption_record", autoResultMap = true)
@ApiModel(description = "水电表用量记录")
public class MeterConsumptionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "对应dev_device_info表主键")
    private String meterId;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    private Integer deviceType;

    @ApiModelProperty(value = "表计属性")
    private Integer effectProperty;

    @ApiModelProperty(value = "表计类别")
    private Integer effectType;

    @ApiModelProperty(value = "表类型")
    private Integer meterType;

    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "最大量程")
    private Integer maxRange;

    @ApiModelProperty(value = "安装位置")
    private String location;

    @ApiModelProperty(value = "楼栋")
    private String buildingId;

    @ApiModelProperty(value = "所属楼层多选")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> floors;

    @ApiModelProperty(value = "所属房屋多选")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> houseIds;

    @ApiModelProperty(value = "初始读数")
    private BigDecimal initialReading;

    @ApiModelProperty(value = "初始时间")
    private LocalDateTime initialTime;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "上次抄表读数")
    private BigDecimal lastReading;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime lastTime;

    @ApiModelProperty(value = "本次抄表读数")
    private BigDecimal thisReading;

    @ApiModelProperty(value = "本次抄表时间")
    private LocalDateTime thisTime;

    @ApiModelProperty(value = "用量")
    private BigDecimal consumption;

    private String createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
