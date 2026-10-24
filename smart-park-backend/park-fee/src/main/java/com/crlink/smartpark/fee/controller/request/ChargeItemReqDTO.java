package com.crlink.smartpark.fee.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 收费项
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ChargeItemReq对象", description = "收费项")
public class ChargeItemReqDTO implements Serializable {

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
