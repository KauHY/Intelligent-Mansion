package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class BillListForScreen {
    @ApiModelProperty(value = "年月")
    private String month;

    @ApiModelProperty(value = "费用数据")
    private Map<Integer, BigDecimal> amountData;

}
