package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 抄表记录
 * </p>
 * */
@Data
public class MeterReadingRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @JsonIgnore
    private Set<String> parkOrgIds;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    private Integer deviceType;

    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "表计属性")
    private Integer effectProperty;

    @ApiModelProperty(value = "表类型（数据来源）")
    private Integer meterType;

    @ApiModelProperty(value = "抄表时间1")
    private LocalDateTime readingTimeStart;

    @ApiModelProperty(value = "抄表时间1")
    private LocalDateTime readingTimeEnd;


}
