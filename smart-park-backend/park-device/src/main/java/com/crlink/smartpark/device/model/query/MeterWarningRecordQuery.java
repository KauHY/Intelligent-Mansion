package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 仪表异常告警记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterWarningRecord查询对象", description = "仪表异常告警记录")
public class MeterWarningRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "表计名称")
    private String deviceName;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @JsonIgnore
    @ApiModelProperty(value = "园区ids")
    private Set<String> parkOrgIds;

    @ApiModelProperty(value = "告警事件")
    private Integer warningCode;

    @ApiModelProperty(value = "告警时间")
    private LocalDateTime warningTimeStart;

    @ApiModelProperty(value = "告警时间")
    private LocalDateTime warningTimeEnd;


}
