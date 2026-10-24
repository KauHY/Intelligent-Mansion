package com.crlink.smartpark.device.model.excel;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ConsumptionExcelExportDTO implements Serializable {
    private String time;

    private BigDecimal consumption;
}
