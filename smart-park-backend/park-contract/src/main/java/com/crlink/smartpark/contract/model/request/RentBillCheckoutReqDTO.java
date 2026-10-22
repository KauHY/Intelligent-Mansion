package com.crlink.smartpark.contract.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentBillCheckoutReqDTO请求对象", description = "租赁合同账单")
public class RentBillCheckoutReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ids", required = true)
    @NotEmpty
    private Set<String> ids;

    @ApiModelProperty(value = "支付方式:1线下银行转账", required = true)
    private Integer payType;

    @ApiModelProperty(value = "收款日期", required = true)
    @NotNull
    private LocalDate checkoutDate;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;

    @ApiModelProperty(value = "fromLeave")
    @JsonIgnore
    private Boolean fromLeave;

}
