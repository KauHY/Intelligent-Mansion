package com.crlink.smartpark.customer.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.Constants.TIME_PATTERN;

@Data
public class GoodsReleaseExcelExportDTO implements Serializable {

    @ExcelProperty(value = "申请编号")
    private String goodsNo;

    @ExcelProperty(value = "申请人")
    private String submitterName;

    @ExcelProperty(value = "手机号")
    private String submitterMobile;

    @ExcelProperty(value = "所在园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ExcelProperty(value = "所在公司")
    private String company;

    @ExcelProperty(value = "放行时间")
    private String releaseDatetime;

    @ExcelProperty(value = "放行物品")
    private String goodsList;

    @ExcelProperty(value = "申请时间", converter = CommonConvert.class)
    @DateTimeFormat(TIME_PATTERN)
    private LocalDateTime createTime;

    @ExcelProperty(value = "状态", converter = DictConverter.class)
    @DictField(dictType = "GOODS_RELEASE_STATUS")
    private Integer status;

}
