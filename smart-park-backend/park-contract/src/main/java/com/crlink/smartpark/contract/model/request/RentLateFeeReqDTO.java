package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 租赁滞纳金费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentLateFee请求对象", description = "租赁滞纳金费用")
public class RentLateFeeReqDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private List<String> ids;

}
