package com.crlink.smartpark.system.vo;

import com.crlink.smartpark.system.enums.CompanyTaxLevelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class CompanyOverviewDTO implements Serializable {
    @ApiModelProperty("入驻企业总数")
    private Integer companyTotal = 0;
    @ApiModelProperty("税收分布")
    private Map<Integer, BigDecimal> taxType = new HashMap<>();
    @ApiModelProperty("入驻人数总数")
    private Integer employeeTotal = 0;
    @ApiModelProperty("行业分布")
    private Map<Integer, BigDecimal> industryType = new HashMap<>();
    ;
    @ApiModelProperty("入驻率")
    private BigDecimal occupancyRate;

    public CompanyOverviewDTO() {
        for (CompanyTaxLevelEnum companyTaxLevelEnum : CompanyTaxLevelEnum.values()) {
            taxType.put(companyTaxLevelEnum.level, BigDecimal.ZERO);
        }
    }
}
