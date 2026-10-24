package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.excel.convert.LocalDateCNConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.contract.dto.DeductionMessage;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.CreateByNameConverter;
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
import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_TIME_FORMAT;
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
public class RentFeeCashExportDTO implements Serializable {

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

    @ExcelProperty(value = "应收金额（元）")
    @ColumnWidth(20)
    private BigDecimal receivableAmount;

    @ExcelProperty(value = "待收金额（元）")
    @ColumnWidth(20)
    private BigDecimal dueAmount;

    @ExcelProperty(value = "已收金额（元）")
    @ColumnWidth(20)
    private BigDecimal receivedAmount;

    @ExcelProperty(value = "已抵扣金额（元）")
    @ColumnWidth(20)
    private BigDecimal offsetAmount;

    @ExcelProperty(value = "已退款金额（元）")
    @ColumnWidth(20)
    private BigDecimal refundedAmount;

    @ExcelProperty(value = "退租日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate leaseDate;

    @ExcelProperty(value = "状态 ", converter = DictConverter.class)
    @DictField(dictType = "RENT_CASH_STATUS")
    @ColumnWidth(20)
    private Integer cashStatus;

    @ExcelProperty(value = "备注")
    @ColumnWidth(20)
    private String remark;

    @ExcelProperty(value = "经办人", converter = CreateByNameConverter.class)
    @ColumnWidth(20)
    private String userId;


    @ExcelProperty(value = "办理时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime handleTime;


}
