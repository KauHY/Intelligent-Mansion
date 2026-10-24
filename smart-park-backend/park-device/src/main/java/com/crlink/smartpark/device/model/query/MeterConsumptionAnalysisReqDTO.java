package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MeterConsumptionAnalysisReqDTO implements Serializable {
    @ApiModelProperty(value = "园区id")
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    @CustomValid({"5", "6"})
    private Integer deviceType;

    @ApiModelProperty(value = "楼栋")
    private String buildingId;

    @ApiModelProperty(value = "表计名称")
    private String meterId;

    @ApiModelProperty(value = "年")
    @NotNull(message = "年份不能为空")
    private Integer year;

    @ApiModelProperty(value = "月份")
    private Integer month;
}
