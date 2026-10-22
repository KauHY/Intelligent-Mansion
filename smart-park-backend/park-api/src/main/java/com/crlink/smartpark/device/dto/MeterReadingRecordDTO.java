package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 抄表记录
 * </p>
 * */
@Data
public class MeterReadingRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "抄表记录id")
    private String id;

    @ApiModelProperty(value = "表id")
    private String meterId;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    @ApiModelProperty(value = "园区名")
    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    private Integer deviceType;

    @ApiModelProperty(value = "表计属性")
    private Integer effectProperty;

    @ApiModelProperty(value = "表计类别")
    private Integer effectType;

    @ApiModelProperty(value = "表类型（2智能表，1普通表）")
    private Integer meterType;

    @ApiModelProperty(value = "抄表读数")
    private BigDecimal meterReading;

    @ApiModelProperty(value = "修正后读数")
    private BigDecimal correctedReading;

    @ApiModelProperty(value = "抄表时间")
    private LocalDateTime readingTime;


    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "导入备注")
    private String importRemark;

}
