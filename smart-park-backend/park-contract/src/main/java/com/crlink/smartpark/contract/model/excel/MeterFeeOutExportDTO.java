package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.LocalDateCNConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 已出水电费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterFeeOutExportDTO响应对象", description = "已出水电费用")
public class MeterFeeOutExportDTO implements Serializable {

    @ExcelProperty(value = "所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ExcelProperty(value = "收费项")
    @ColumnWidth(20)
    private String chargeName;

    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String companyName;

    @ExcelProperty(value = "房源名称")
    @ColumnWidth(20)
    private String houseName;

    @ExcelProperty(value = "结算月份")
    @ColumnWidth(20)
    private String settledMonth;

    @ExcelProperty(value = "应收日期", converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate dueDate;

    @ExcelProperty(value = "最晚缴纳日期", converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate latestPayDate;

    @ExcelProperty(value = "结算时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime settledTime;

    @ExcelProperty(value = "应收金额（元）")
    @ColumnWidth(20)
    private BigDecimal receivableAmount;

    @ExcelProperty(value = "待收金额（元）")
    @ColumnWidth(20)
    private BigDecimal dueAmount;

    @ExcelProperty(value = "已收金额（元）")
    @ColumnWidth(20)
    private BigDecimal receivedAmount;

    @ExcelProperty(value = "减免金额（元）")
    @ColumnWidth(20)
    private BigDecimal deductionAmount;

    @ExcelProperty(value = "数据来源 ", converter = DictConverter.class)
    @DictField(dictType = "RENT_DATA_SOURCE")
    @ColumnWidth(20)
    private Integer dataSource;

    @ExcelProperty(value = "费用状态", converter = DictConverter.class)
    @DictField(dictType = "RENT_FEE_BILL_STATUS")
    @ColumnWidth(20)
    private Integer feeStatus;

}
