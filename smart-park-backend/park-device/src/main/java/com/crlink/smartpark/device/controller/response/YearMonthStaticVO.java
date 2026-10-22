package com.crlink.smartpark.device.controller.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class YearMonthStaticVO {
    private List<BigDecimal> lastYear = Lists.newArrayListWithCapacity(12);
    private List<BigDecimal> currentYear = Lists.newArrayList();
    private BigDecimal currentYearAverage;
}
