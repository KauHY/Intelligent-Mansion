package com.crlink.smartpark.system.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;

import java.io.Serializable;

import static com.crlink.smartpark.system.enums.Constant.FLOOR_TYPE;


@Data
public class HouseExcelExportDTO implements Serializable {


    @ExcelProperty("楼层房号")
    private String name;

    @ExcelProperty(value = "所属楼栋单元", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 2)
    private String buildingId;

    @ExcelProperty(value = "关联园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ExcelProperty("房屋编码")
    private String code;

    @ExcelProperty(value = "楼层属性", converter = DictConverter.class)
    @DictField(dictType = FLOOR_TYPE)
    private Integer floorType;

    @ExcelProperty("所在楼层")
    private Integer floor;

    @ExcelProperty("建筑面积(㎡)")
    private Double buildArea;

    @ExcelProperty("套内面积(㎡)")
    private Double coverArea;

    //@ExcelProperty("房屋功能")
    //private Integer houseFunction;
    //
    //@ExcelProperty("房屋朝向")
    //private Integer orientation;
    //
    //@ExcelProperty("*房屋属性")
    //@NotNull(message = "房屋属性不能为空")
    //private Integer houseProperty;

    /**
     * 房屋状态：自用，待租， 已租，代售，已售
     */
    //@ExcelIgnore
    //private Integer houseStatus;

    /**
     * 房屋属性：0自用，1租赁，2销售
     */

    /**
     * 房屋功能；办公 商铺 公寓 仓库 其他
     */
    //private Integer houseFunction;


}
