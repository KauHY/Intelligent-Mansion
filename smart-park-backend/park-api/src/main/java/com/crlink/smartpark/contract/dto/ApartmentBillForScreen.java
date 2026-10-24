package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ApartmentBillForScreen {
    @ApiModelProperty(value = "累计(万元)")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "近一月(万元)")
    private BigDecimal nearOneMonthAmount;

    public ApartmentBillForScreen(BigDecimal totalAmount, BigDecimal nearOneMonthAmount) {
        this.totalAmount = totalAmount;
        this.nearOneMonthAmount = nearOneMonthAmount;
    }
}
