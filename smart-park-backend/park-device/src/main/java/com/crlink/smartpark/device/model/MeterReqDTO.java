package com.crlink.smartpark.device.model;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 仪表（电表和水表）
 * </p>
 *
  */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Meter请求对象", description = "仪表（电表和水表）")
public class MeterReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id", required = true)
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "能耗类别（1电能耗，2水能耗）", required = true)
    @NotNull
    @CustomValid({"5", "6"})
    private Integer deviceType;

    @ApiModelProperty(value = "表计属性", required = true)
    private Integer effectProperty;

    @ApiModelProperty(value = "表计类别", required = true)
    private Integer effectType;

    @ApiModelProperty(value = "表类型", required = true)
    @NotNull
    private Integer meterType;

    @ApiModelProperty(value = "名称", required = true)
    @Length(min = 1, max = 30)
    private String deviceName;

    @ApiModelProperty(value = "设备id", required = true)
    @Length(max = 30)
    private String deviceId;

    @ApiModelProperty(value = "最大量程", required = true)
    @NotNull
    private Integer maxRange;

    @ApiModelProperty(value = "安装位置", required = true)
    @Length(max = 30)
    private String location;

    @ApiModelProperty(value = "楼栋", required = true)
    @NotEmpty
    private String buildingId;

    @ApiModelProperty(value = "所属楼层多选", required = true)
    @NotEmpty
    private List<Integer> floors;

    @ApiModelProperty(value = "所属房屋多选")
    private List<String> houseIds;

    @ApiModelProperty(value = "初始读数", required = true)
    @NotNull
    private BigDecimal initialReading;

    @ApiModelProperty(value = "初始时间", required = true)
    @NotNull
    private LocalDateTime initialTime;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;


}
