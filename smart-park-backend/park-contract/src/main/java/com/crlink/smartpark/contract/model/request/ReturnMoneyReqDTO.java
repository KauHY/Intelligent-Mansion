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
 * 退款
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ReturnMoneyReqDTO请求对象", description = "结算缴费")
public class ReturnMoneyReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "退款方式:1线下银行转账")
    @NotNull(message = "退款方式不能为空")
    private Integer payType;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "退款原因", required = true)
    @NotBlank(message = "退款原因不能为空")
    @Length(max = 30, message = "退款原因不能超过30字")
    private String returnReason;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;

    @ApiModelProperty(value = "押金：false 其余为：true")
    private Boolean fromTrade;
}
