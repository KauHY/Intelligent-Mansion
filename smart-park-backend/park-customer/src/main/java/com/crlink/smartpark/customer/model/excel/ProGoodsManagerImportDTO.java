package com.crlink.smartpark.customer.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 物品管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProGoodsManagerImportDTO响应对象", description = "物品管理")
public class ProGoodsManagerImportDTO implements Serializable {

    @ExcelProperty(value = "*物品名称")
    @NotBlank(message = "物品名称不能为空")
    @Length(max = 20, message = "物品名称最长20个字符")
    private String goodsName;

    @ExcelProperty(value = "*物品类别", converter = DictConverter.class)
    @DictField(dictType = "PRO_GOODS_TYPE")
    @NotNull(message = "物品类别不能为空")
    private Integer goodsType;

    @ExcelProperty(value = "*计量单位")
    @NotBlank(message = "计量单位不能为空")
    @Length(max = 20, message = "计量单位最长20个字符")
    private String unit;

    @ExcelProperty(value = "库存下限预警值")
    private Integer minStock;

    @ExcelProperty(value = "品牌")
    @Length(max = 20, message = "品牌名最长20个字符")
    private String brand;

    @ExcelProperty(value = "型号")
    @Length(max = 20, message = "型号名最长20个字符")
    private String model;

    @ExcelProperty(value = "入库单价(元)")
    private BigDecimal unitPrice;

    @ExcelProperty(value = "备注")
    @Length(max = 99, message = "备注最长99个字符")
    private String remark;


}
