package com.crlink.smartpark.customer.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.CreateByNameConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 库存出入库记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProStockRecord响应对象", description = "库存出入库记录")
public class ProStockRecordExportDTO implements Serializable {

    @ColumnWidth(20)
    @ExcelProperty(value = "出入库单号")
    private String recordNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "物品编码")
    private String goodsNo;

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
    @ExcelProperty(value = "单位")
    private String unit;

    @ColumnWidth(20)
    @ExcelProperty(value = "入库数量")
    private String stockInNum;

    @ColumnWidth(20)
    @ExcelProperty(value = "入库单价（元）")
    private String stockInUnitPrice;

    @ColumnWidth(20)
    @ExcelProperty(value = "操作后库存")
    private String currentNum;

    @ColumnWidth(20)
    @ExcelProperty(value = "操作后均价（元）")
    private String currentAveragePrice;

    @ColumnWidth(20)
    @ExcelProperty(value = "备注")
    private String remark;

    @ColumnWidth(20)
    @ExcelProperty(value = "出入库方式", converter = DictConverter.class)
    @DictField(dictType = "PRO_STOCK_TYPE")
    private Integer stockType;

    @ColumnWidth(20)
    @ExcelProperty(value = "操作人", converter = CreateByNameConverter.class)
    private String createBy;

    @ColumnWidth(20)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "更新时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime updateTime;


}
