package com.crlink.smartpark.contract.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 费用统计
 * </p>
 * */
@Data
public class ChargeStaTrendDTO implements Serializable {

    @ApiModelProperty(value = "园区id")
    @NotEmpty(message = "园区id不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "费用类型 0全部  1企业水电费 2公寓水电  3租赁 4物业 5公寓")
//    @NotNull(message = "费用类型不能为空!")
    private Integer chargeType;

    @ApiModelProperty(value = "开始月份")
    private String startMonth;

    @ApiModelProperty(value = "结束月份")
    private String endMonth;


}
