package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 每天抄表记录
 * </p>
 * */
@Data
@TableName(value = "dev_meter_reading_record", autoResultMap = true)
public class MeterReadingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "仪表id")
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

    @ApiModelProperty(value = "抄表读数")
    private BigDecimal meterReading;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime readingTime;

    @ApiModelProperty(value = "修正后读数")
    private BigDecimal correctedReading;

    @ApiModelProperty(value = "备注")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    @ApiModelProperty(value = "导入备注")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String importRemark;

    private String createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;

}
