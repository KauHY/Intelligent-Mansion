package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentBillUpdateReqDTO请求对象", description = "租赁合同账单")
public class RentBillUpdateReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "所在账期", required = true)
    @NotBlank
    private String accountPeriod;

}
