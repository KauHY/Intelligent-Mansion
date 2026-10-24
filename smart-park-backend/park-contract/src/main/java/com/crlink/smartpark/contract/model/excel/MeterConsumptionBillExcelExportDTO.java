package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.List2StringConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 租赁合同账单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterConsumptionBillExcelExportDTO", description = "MeterConsumptionBillExcelExportDTO")
public class MeterConsumptionBillExcelExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ExcelProperty(value = "所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ExcelProperty(value = "账单编号")
    @ColumnWidth(20)
    private String billNo;

    @ExcelProperty(value = "所在账期")
    @ColumnWidth(20)
    private String accountPeriod;


    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String companyName;

    @ExcelProperty(value = "所含收费项")
    @ColumnWidth(20)
    private String chargeName;

    @ExcelProperty(value = "房源名称", converter = List2StringConverter.class)
    @ColumnWidth(60)
    private List<String> houseName;

//    @ExcelProperty(value = "计费周期")
//    @ColumnWidth(20)
//    private String feeDate;

//    @ExcelProperty(value = "计费周期 结束日期")
//    private LocalDate endDate;

    @ExcelProperty(value = "应收日期", converter = LocalDateConverter.class)
    @ColumnWidth(20)
    private LocalDate dueDate;

    @ExcelProperty(value = "最晚缴纳日期", converter = LocalDateConverter.class)
    @ColumnWidth(20)
    private LocalDate latestPayDate;


    @ExcelProperty(value = "应收金额(元)")
    @ColumnWidth(20)
    private BigDecimal receivableAmount;

    @ExcelProperty(value = "待收金额(元)")
    @ColumnWidth(20)
    private BigDecimal dueAmount;

    @ExcelProperty(value = "已收金额(元)")
    @ColumnWidth(20)
    private BigDecimal receivedAmount;

    @ExcelProperty(value = "减免金额（元）")
    @ColumnWidth(20)
    private BigDecimal deductionAmount;

    @ExcelProperty(value = "数据来源", converter = DictConverter.class)
    @DictField(dictType = "RENT_DATA_SOURCE")
    @ColumnWidth(20)
    private Integer dataSource;

    @ExcelProperty(value = "收款状态", converter = DictConverter.class)
    @DictField(dictType = "RENT_BILL_STATUS")
    @ColumnWidth(20)
    private Integer status;

    @ExcelProperty(value = "收款日期", converter = LocalDateConverter.class)
    @ColumnWidth(20)
    private LocalDate checkoutDate;

}
