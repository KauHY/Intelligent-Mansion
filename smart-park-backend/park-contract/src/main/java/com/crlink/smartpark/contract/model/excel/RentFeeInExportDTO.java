package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.excel.convert.LocalDateCNConverter;
import com.crlink.smartpark.contract.dto.DeductionMessage;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_FORMAT;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.EMPLOYEE_NAME_CODE;

/**
 * <p>
 * 租赁合同费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RentContractFee响应对象", description="租赁合同费用")
public class RentFeeInExportDTO implements Serializable {

    @ExcelProperty(value = "所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ExcelProperty(value = "合同编号")
    @ColumnWidth(20)
    private String contractNo;

    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String companyName;

    @ExcelProperty(value = "收费项")
    @ColumnWidth(20)
    private String chargeName;

    @ExcelProperty(value = "建筑面积（㎡）")
    @ColumnWidth(20)
    private Double totalArea;

    @ExcelProperty(value = "房源名称")
    @ColumnWidth(20)
    private String houseName;

    @ExcelProperty(value = "计费周期")
    @ColumnWidth(20)
    private String cycleDate;

    @ExcelProperty(value = "应收日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate dueDate;

    @ExcelProperty(value = "最晚缴纳日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate latestPayDate;

    @ExcelProperty(value = "数据来源 ", converter = DictConverter.class)
    @DictField(dictType = "RENT_DATA_SOURCE")
    @ColumnWidth(20)
    private Integer dataSource;

    @ExcelProperty(value = "应收金额（元）")
    @ColumnWidth(20)
    private BigDecimal receivableAmount;

    @ExcelProperty(value = "状态", converter = DictConverter.class)
    @DictField(dictType = "RENT_FEE_OUT_STATUS")
    @ColumnWidth(20)
    private Integer billingStatus;


}
