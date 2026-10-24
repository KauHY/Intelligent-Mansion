package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 租赁滞纳金费用
 * </p>
 * */
@Data
public class RentLateFeeExportDTO implements Serializable {

    @ExcelProperty(value = "所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ExcelProperty(value = "账单编号")
    @ColumnWidth(20)
    private String billNo;

    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String companyName;

    @ExcelProperty(value = "房源名称")
    @ColumnWidth(20)
    private String houseName;

    @ExcelProperty(value = "建筑面积（㎡）")
    @ColumnWidth(20)
    private Double totalArea;

    @ExcelProperty(value = "所在账期")
    @ColumnWidth(20)
    private String accountPeriod;

    @ExcelProperty(value = "逾期金额（元）")
    @ColumnWidth(20)
    private BigDecimal dueAmount;

    @ExcelProperty(value = "逾期天数")
    @ColumnWidth(20)
    private Integer dueDays;

    @ExcelProperty(value = "应收滞纳金（元）")
    @ColumnWidth(20)
    private BigDecimal receivableFine;

    @ExcelProperty(value = "待收滞纳金（元）")
    @ColumnWidth(20)
    private BigDecimal receiveFine;

    @ExcelProperty(value = "已收滞纳金（元）")
    @ColumnWidth(20)
    private BigDecimal overdueFine;

    @ExcelProperty(value = "免除滞纳金（元）")
    @ColumnWidth(20)
    private BigDecimal reduceFine;

    @ExcelProperty(value = "状态", converter = DictConverter.class)
    @DictField(dictType = "RENT_LATE_STATUS")
    @ColumnWidth(20)
    private Integer status;

}
