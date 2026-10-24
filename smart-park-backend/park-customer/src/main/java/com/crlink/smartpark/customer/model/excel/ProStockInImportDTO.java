package com.crlink.smartpark.customer.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: ProStockInImportDTO
 * @Package com.crlink.smartpark.customer.model.excel
 * @Description: 物品批量入库
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProStockInImportDTO响应对象", description = "库存管理")
public class ProStockInImportDTO implements Serializable {

    @ExcelProperty("*物品编码")
    @NotBlank(message = "物品编码不能为空")
    private String goodsNo;

    @ExcelProperty(value = "*物品名称")
    @NotBlank(message = "物品名称不能为空")
    @Length(max = 20, message = "物品名称最长20个字符")
    private String goodsName;

    @ExcelProperty(value = "*所属仓库")
    @NotBlank(message = "所属仓库不能为空")
    @Length(max = 20, message = "所属仓库最长20个字符")
    private String goodsHouse;

    @ExcelProperty(value = "*入库数量")
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量最小为1")
    private Integer stockInNum;

    @ExcelProperty(value = "入库单价(元)")
    private BigDecimal stockInUnitPrice;

    @ExcelProperty(value = "备注")
    private String remark;

}
