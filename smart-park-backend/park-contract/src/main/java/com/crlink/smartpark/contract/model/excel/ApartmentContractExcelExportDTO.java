package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;

import com.crlink.smartpark.common.excel.convert.LocalDateCNConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;

import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @ClassName: ApartmentContractExcelExportDTO
 * @Package com.crlink.smartpark.contract.model.excel
 * @Description: 公寓合同 type = 5
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApartmentContractExcelExportDTO {
    @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    @ColumnWidth(20)
    private String parkOrgId;

    @ExcelProperty(value = "合同编号")
    @ColumnWidth(20)
    private String no;

    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String nameB;

    /**
     * todo 特殊处理
     */
    @ExcelProperty(value = "房源名称")
    @ColumnWidth(20)
    private String houseMessagesStr;

    // 公寓合同
    @ExcelProperty(value = "租期（天）")
    @ColumnWidth(20)
    private Integer leaseTermDay;
    @ExcelProperty(value = "客房数(间)")
    @ColumnWidth(20)
    private Integer numberOfRooms;

    @ExcelProperty(value = "租金（元）")
    @ColumnWidth(20)
    private BigDecimal rentAmount;
    @ExcelProperty(value = "押金（元）")
    @ColumnWidth(20)
    private BigDecimal deposit;

    /**
     * todo 2021年11月1号
     */
    @ExcelProperty(value = "起租日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate startDate;
    /**
     * 2021年11月1号
     */
    @ExcelProperty(value = "截止日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate endDate;
    /**
     * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
     */
    @ExcelProperty(value = "合同状态", converter = DictConverter.class)
    @DictField(dictType = "CONTRACT_STATE")
    @ColumnWidth(20)
    private Integer state;

    /**
     * todo 经办人id，多个时；分隔
     */
    @ExcelProperty(value = "经办人")
    @ColumnWidth(20)
    private String employeeId;

    /**
     * 2021年11月1号
     */
    @ExcelProperty(value = "办理日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate contractedDate;

    @ExcelProperty(value = "承租人姓名")
    @ColumnWidth(20)
    private String contactB;

}
