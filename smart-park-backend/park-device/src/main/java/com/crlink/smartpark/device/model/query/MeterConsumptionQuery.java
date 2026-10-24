package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.core.validation.CustomValid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 企业或房屋水电用量
 * </p>
 * */
@Data
@ApiModel(value = "MeterConsumption查询对象", description = "企业或房屋水电用量")
public class MeterConsumptionQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "0房屋用量记录，1企业用量记录")
    @NotNull
    @CustomValid({"0", "1"})
    private Integer type;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "楼栋id")
    private String buildingId;

    @ApiModelProperty(value = "房屋id")
    private String houseId;

    @JsonIgnore
    private Set<String> parkOrgIds;

}
