package com.crlink.smartpark.contract.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 费用统计
 * </p>
 * */
@Data
public class ChargeStaReqDTO implements Serializable {

    @ApiModelProperty(value = "园区id")
    @NotEmpty(message = "园区id不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "费用类型 0全部  1企业水电费 2公寓水电  3租赁 4物业 5公寓")
    private Integer chargeType;

    @ApiModelProperty(value = "时间范围月份")
    private String month;


}
