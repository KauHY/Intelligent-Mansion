package com.crlink.smartpark.customer.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.Constants;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 设备台账
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProGoodsManagerExportDTO响应对象", description="物品管理")
public class ProGoodsManagerExportDTO implements Serializable {

    @ExcelProperty("物品编码")
    @ColumnWidth(20)
    private String goodsNo;

    @ExcelProperty("物品名称")
    @ColumnWidth(20)
    private String goodsName;

    @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    @ColumnWidth(20)
    private String parkOrgId;

    @ColumnWidth(20)
    @ExcelProperty(value = "物品类别", converter = DictConverter.class)
    @DictField(dictType = "PRO_GOODS_TYPE")
    private Integer goodsType;

    @ExcelProperty("单位")
    @ColumnWidth(20)
    private String unit;

    @ExcelProperty("入库单价(元)")
    @ColumnWidth(20)
    private BigDecimal unitPrice;

    @ColumnWidth(20)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "更新时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

    @ExcelProperty("库存下限预警值")
    @ColumnWidth(20)
    private Integer minStock;





}
