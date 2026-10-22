package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentBillDeductionFeeDTO请求对象", description = "租赁合同账单")
public class RentBillDeductionFeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账单id", required = true)
    @NotBlank(message = "账单id不能为空！")
    private String id;

    @ApiModelProperty(value = "减免金额")
    @NotNull(message = "减免金额不能为空！")
    private BigDecimal deductionAmount;

    @ApiModelProperty(value = "附件key")
    @Length(max = 800, message = "附件key超长")
    private String file;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;

}
