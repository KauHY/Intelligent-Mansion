package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @ClassName: ChargeItemMessage
 * @Package com.crlink.smartpark.contract.dto
 * @Description: 收费项详细属性
 * @Version 1.0
 */
@Data
@ApiModel(value = "ChargeItemMessage对象", description = "合同")
public class ChargeItemMessage {


    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区")
    @NotBlank(message = "园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "收费名称")
    @NotBlank(message = "收费名称不能为空")
    @Length(min = 1, max = 30)
    private String chargeName;

    @ApiModelProperty(value = "费用类型")
    @NotNull(message = "费用类型不能为空")
    private Integer feeType;

    @ApiModelProperty(value = "收款方")
    @NotBlank(message = "收款方不能为空")
    private String payeeId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "备注")
    @Length(max = 99)
    private String remark;
}
