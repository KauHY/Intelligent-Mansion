package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class BillForScreen {
    @ApiModelProperty(value = "累计收费(万元)")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "当年收费(万元)")
    private BigDecimal thisYearAmount;

    @ApiModelProperty(value = "柱状图数据")
    private List<BillListForScreen> billListForScreen;

    public BillForScreen(BigDecimal totalAmount, BigDecimal thisYearAmount) {
        this.totalAmount = totalAmount;
        this.thisYearAmount = thisYearAmount;
    }
}
