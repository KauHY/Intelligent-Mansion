package com.crlink.smartpark.system.enums;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;

public enum CompanyTaxLevelEnum {
    TAX_L10(1, 10, "10w以下"), TAX_20(2, 20, "10-20万"), TAX_30(3, 30, "20-30万"),
    TAX_50(4, 50, "30-50万"), TAX_G50(5, 50, "50万以上");

    public int level;
    public int tax;
    private String label;

    CompanyTaxLevelEnum(int level, int tax, String label) {
        this.level = level;
        this.tax = tax;
        this.label = label;
    }

    public static int getTaxLevel(BigDecimal tax) {
        if (tax == null || NumberUtil.isLessOrEqual(tax, BigDecimal.valueOf(TAX_L10.tax))) {
            return TAX_L10.level;
        } else if (NumberUtil.isLessOrEqual(tax, BigDecimal.valueOf(TAX_20.tax))) {
            return TAX_20.level;
        } else if (NumberUtil.isLessOrEqual(tax, BigDecimal.valueOf(TAX_30.tax))) {
            return TAX_30.level;
        } else if (NumberUtil.isLessOrEqual(tax, BigDecimal.valueOf(TAX_50.tax))) {
            return TAX_50.level;
        } else {
            return TAX_G50.level;
        }
    }

}
