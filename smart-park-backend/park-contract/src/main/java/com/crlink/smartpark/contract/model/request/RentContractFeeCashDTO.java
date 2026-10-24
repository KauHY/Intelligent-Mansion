package com.crlink.smartpark.contract.model.request;

import com.crlink.smartpark.contract.dto.DeductionMessage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 租赁合同费用
 * </p>
 * */
@Data
public class RentContractFeeCashDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    @NotEmpty
    private String id;

    @ApiModelProperty(value = "是否抵扣 1是 0否")
    @NotNull
    private Integer deduction;

    @ApiModelProperty(value = "抵扣内容")
    private List<DeductionMessage> deductionMsg;

    @ApiModelProperty(value = "待抵扣总金额")
    private BigDecimal offsetAmount;

    @ApiModelProperty(value = "待退还金额")
    private BigDecimal refundedAmount;

    @ApiModelProperty(value = "备注")
    private String remark;


}
