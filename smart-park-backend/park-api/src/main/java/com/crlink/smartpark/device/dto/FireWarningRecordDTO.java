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
 * 报警记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FireWarningRecord响应对象", description = "报警记录")
public class FireWarningRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录id")
    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "消防设备id")
    private String deviceId;

    @ApiModelProperty(value = "报警类型")
    private Integer warnType;

    @ApiModelProperty(value = "告警是否已取消：0未处理，1已处理")
    private Integer canceled;

    @ApiModelProperty(value = "报警时间")
    private LocalDateTime warnTime;

    @ApiModelProperty(value = "确认结果，1,真实火警，2误报，3消防演习")
    private Integer realReasonType;

    @ApiModelProperty(value = "处理意见")
    private String opinion;

    @ApiModelProperty(value = "记录的状态,0待处理，1已处理")
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

}
