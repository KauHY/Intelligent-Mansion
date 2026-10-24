package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 仪表异常告警记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterWarningRecord响应对象", description = "仪表异常告警记录")
public class MeterWarningRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    @ApiModelProperty(value = "园区名")
    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    private String meterId;
    private String deviceId;
    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    private Integer deviceType;

    @ApiModelProperty(value = "告警事件")
    private Integer warningCode;

    @ApiModelProperty(value = "告警等级")
    private Integer warningLevel;

    @ApiModelProperty(value = "告警时间")
    private LocalDateTime warningTime;

}
