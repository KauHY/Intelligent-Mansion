package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.List2StringConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractTradeExcelExportDTO", description = "租赁合同交易明细")
public class RentContractTradeExcelExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ExcelProperty(value = "订单编号")
    @ColumnWidth(20)
    private String tradeNo;

    @ExcelProperty(value = "账单编号")
    @ColumnWidth(20)
    private String billNo;

    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String companyName;

    @ExcelProperty(value = "支付方式", converter = DictConverter.class)
    @DictField(dictType = "RENT_FEE_PAY_TYPE")
    @ColumnWidth(20)
    private Integer payType;

    @ExcelProperty(value = "订单类型", converter = DictConverter.class)
    @DictField(dictType = "RENT_TRADE_TYPE")
    @ColumnWidth(20)
    private Integer type;

    @ExcelProperty(value = "账单应收金额(元)")
    @ColumnWidth(20)
    private BigDecimal receivableAmount;

    @ExcelProperty(value = "实付金额(元)")
    @ColumnWidth(20)
    private BigDecimal receivedAmount;

    @ExcelProperty(value = "支付时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @ExcelProperty(value = "房源名称", converter = List2StringConverter.class)
    @ColumnWidth(60)
    private List<String> houseName;

    @ExcelProperty(value = "所在账期")
    @ColumnWidth(20)
    private String accountPeriod;

    @ExcelProperty(value = "所含收费项")
    @ColumnWidth(20)
    private String chargeName;

}
