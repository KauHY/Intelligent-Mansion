package com.crlink.smartpark.system.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.crlink.smartpark.system.enums.Constant.*;

@Data
public class HouseExcelImportDTO implements Serializable {

    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String parkOrgId;

    //    @ExcelProperty("*所属楼栋名称")
    @ExcelProperty(index = 0, value = "*所属楼栋名称")
    @NotEmpty(message = "所属楼栋名称不能为空")
    private String buildingId;

    //    @ExcelProperty("*楼层房号")
    @ExcelProperty(index = 1, value = "*楼层房号")
    @NotEmpty(message = "楼层房号不能为空")
    private String name;

    //    @ExcelProperty("房屋编码")
    @ExcelProperty(index = 2, value = "房屋编码")
    private String code;

    //    @ExcelProperty(value = "楼层属性", converter = DictConverter.class)
    @ExcelProperty(index = 3, value = "楼层属性", converter = DictConverter.class)
    @DictField(dictType = FLOOR_TYPE)
    private Integer floorType;

    //    @ExcelProperty("*所在楼层")
    @ExcelProperty(index = 4, value = "*所在楼层")
    @NotNull(message = "所在楼层不能为空")
    private Integer floor;

    //    @ExcelProperty("建筑面积(㎡)")
    @ExcelProperty(index = 5, value = "*建筑面积(㎡)")
    @NotNull(message = "建筑面积不能为空")
    private Double buildArea;

    //    @ExcelProperty("*套内面积(㎡)")
    @ExcelProperty(index = 6, value = "*套内面积(㎡)")
    @NotNull(message = "套内面积不能为空")
    private Double coverArea;

    //    @ExcelProperty(value = "房屋功能", converter = DictConverter.class)
    @ExcelProperty(index = 7, value = "房屋功能", converter = DictConverter.class)
    @DictField(dictType = HOUSE_FUNCTION)
    @NotNull(message = "房屋功能不能为空")
    private Integer houseFunction;

    //    @ExcelProperty(value = "房屋朝向", converter = DictConverter.class)
    @ExcelProperty(index = 8, value = "房屋朝向", converter = DictConverter.class)
    @DictField(dictType = HOUSE_ORIENTATION)
    private Integer orientation;

    //    @ExcelProperty(value = "*房屋属性", converter = DictConverter.class)
    @ExcelProperty(index = 9, value = "*房屋属性", converter = DictConverter.class)
    @NotNull(message = "房屋属性不能为空")
    @DictField(dictType = HOUSE_PROPERTY)
    private Integer houseProperty;

    @ExcelProperty(index = 10, value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String remark;

    /**
     * 房屋状态：自用，待租， 已租，代售，已售
     */
    //@ExcelIgnore
    //HOUSE_STATUS
    //private Integer houseStatus;


}
