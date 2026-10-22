package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CardExcelExportVO implements Serializable {


    @ExcelProperty("卡号")
    @ColumnWidth(20)
    private String cardNo;

    @ApiModelProperty(value = "园区组织Id")
    @ExcelProperty(value = "所属园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    @ColumnWidth(20)
    private String parkOrgId;


    @ExcelProperty(value = "卡介质", converter = DictConverter.class)
    @DictField(dictType = "CARD_MEDIUM")
    @ColumnWidth(20)
    private Integer cardMedium;

    @ExcelProperty(value = "卡类型", converter = DictConverter.class)
    @DictField(dictType = "CARD_TYPE")
    @ColumnWidth(20)
    private Integer cardType;

    @ExcelProperty(value = "持卡人", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 6)
    @ColumnWidth(20)
    private String employeeId;

    @ExcelProperty(value = "联系电话")
    @ColumnWidth(20)
    private String mobile;

    @ApiModelProperty(value = "所属企业Id")
    @ExcelProperty(value = "关联企业", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 4)
    @ColumnWidth(20)
    private String companyId;

    @ExcelProperty("有效期")
    @ColumnWidth(30)
    private String limitDate;


    @ApiModelProperty(value = "状态：CARD_STATUS：1已绑定、2已挂失、3已注销、4已解绑")
    @ExcelProperty(value = "状态", converter = DictConverter.class)
    @DictField(dictType = "CARD_STATUS")
    @ColumnWidth(20)
    private Integer status;
}
