package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.core.validation.CustomValid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 仪表（电表和水表）
 * </p>
 *
  */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Meter查询对象", description = "仪表（电表和水表）")
public class MeterQuery extends BaseQuery implements Serializable {


    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "仪表id")
    private String meterId;
    private String deviceId;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    @NotNull
    @CustomValid({"5", "6"})
    private Integer deviceType;

    @ApiModelProperty(value = "表计属性")
    private Integer effectProperty;

    @ApiModelProperty(value = "表类型")
    private Integer meterType;

    @ApiModelProperty(value = "名称")
    private String deviceName;


    @ApiModelProperty(value = "楼栋")
    private String buildingId;

    @ApiModelProperty(value = "所属楼层多选")
    private Integer floor;

    @ApiModelProperty(value = "所属房屋多选")
    private String houseId;

    /**
     * 状态：0离线1在线
     */
    @ApiModelProperty(value = "运行状态")
    private Integer status;

    /**
     * 使用状态,1正常使用
     */
    @ApiModelProperty(value = "使用状态")
    private Integer usingStatus;

    @JsonIgnore
    private Set<String> parkOrgIds;


}
