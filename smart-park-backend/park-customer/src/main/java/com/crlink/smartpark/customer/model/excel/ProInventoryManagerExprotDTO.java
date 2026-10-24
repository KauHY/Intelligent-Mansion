package com.crlink.smartpark.customer.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: ProInventoryManagerExprotDTO
 * @Package com.crlink.smartpark.customer.model.excel
 * @Description: 盘点管理导出
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProInventoryManager响应对象", description = "盘点管理")
public class ProInventoryManagerExprotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "盘点月份")
    @ColumnWidth(20)
    @ExcelProperty(value = "盘点月份")
    private String month;

    @ColumnWidth(20)
    @ExcelProperty(value = "物品名称")
    private String goodsName;

    @ColumnWidth(20)
    @ExcelProperty(value = "物品类别", converter = DictConverter.class)
    @DictField(dictType = "PRO_GOODS_TYPE")
    private Integer goodsType;

    @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    @ColumnWidth(20)
    private String parkOrgId;

    @ColumnWidth(20)
    @ExcelProperty(value = "所属仓库")
    private String goodsHouse;

    @ColumnWidth(20)
    @ExcelProperty(value = "上月结余数量")
    private String lastBalance;

    @ColumnWidth(20)
    @ExcelProperty(value = "上月结余总额（元）")
    private String lastBalanceTotal;

    @ColumnWidth(20)
    @ExcelProperty(value = "本月库存数")
    private Integer currentBalance;

    @ColumnWidth(20)
    @ExcelProperty(value = "本月库存总额（元）")
    private BigDecimal currentBalanceTotal;

    @ColumnWidth(20)
    @ExcelProperty(value = "本月入库数量")
    private Integer currentStockInNumber;

    @ColumnWidth(20)
    @ExcelProperty(value = "本月出库数量")
    private Integer currentStockOutNumber;

    @ColumnWidth(20)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "盘点时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime inventoryTime;

}
