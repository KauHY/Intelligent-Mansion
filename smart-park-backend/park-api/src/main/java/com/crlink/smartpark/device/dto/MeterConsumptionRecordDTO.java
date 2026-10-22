package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.*;

/**
 * <p>
 * 水电表用量记录
 * </p>
 * */
@Data
@ApiModel(description = "水电表用量记录")
public class MeterConsumptionRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "仪表id")
    private String meterId;
    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "楼栋")
    @Id2objectField(code = BUILDING_NAME_CODE, targetFieldName = "buildingName")
    private String buildingId;
    @ApiModelProperty(value = "楼栋")
    private String buildingName;


    @ApiModelProperty(value = "所属楼层多选")
    private List<Integer> floors;

    @ApiModelProperty(value = "房屋id")
    @Id2objectField(code = HOUSE_NAME_CODE, targetFieldName = "houseNames")
    private List<String> houseIds;
    private List<String> houseNames;

    @ApiModelProperty(value = "名称", required = true)
    private String deviceName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    private Integer deviceType;

    @ApiModelProperty(value = "表计属性")
    private Integer effectProperty;

    @ApiModelProperty(value = "表计类别")
    private Integer effectType;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "上次抄表读数")
    private BigDecimal lastReading;

    @ApiModelProperty(value = "上次抄表时间")
    private LocalDateTime lastTime;

    @ApiModelProperty(value = "本次抄表读数")
    private BigDecimal thisReading;

    @ApiModelProperty(value = "本次抄表时间")
    private LocalDateTime thisTime;

    @ApiModelProperty(value = "本月实际用量")
    private BigDecimal consumption;


}
