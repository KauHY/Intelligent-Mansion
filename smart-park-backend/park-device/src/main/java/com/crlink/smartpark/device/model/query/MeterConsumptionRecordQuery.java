package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 水电表用量记录
 * </p>
 * */
@Data
public class MeterConsumptionRecordQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @JsonIgnore
    private Set<String> parkOrgIds;

    private Integer deviceType;

    private String deviceId;

    @ApiModelProperty(value = "楼栋")
    private String buildingId;

    @ApiModelProperty(value = "所属楼层多选")
    private Integer floor;

    @ApiModelProperty(value = "房屋")
    private String houseId;

    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @JsonIgnore
    private Set<String> meterIds;


}
