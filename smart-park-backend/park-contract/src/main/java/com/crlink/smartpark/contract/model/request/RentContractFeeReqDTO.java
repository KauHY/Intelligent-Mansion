package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 租赁合同费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractFee请求对象", description = "租赁合同费用")
public class RentContractFeeReqDTO implements Serializable {

    @ApiModelProperty(value = "对象ids")
    @NotEmpty(message = "对象ids不能为空")
    private List<String> ids;

    @ApiModelProperty(value = "应收日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "最晚缴纳日期")
    private LocalDate latestPayDate;


}
