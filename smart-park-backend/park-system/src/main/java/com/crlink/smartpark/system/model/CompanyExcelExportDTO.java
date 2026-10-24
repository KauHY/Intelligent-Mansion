package com.crlink.smartpark.system.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ClassName: CompanyExcelExportDTO
 * @Package com.crlink.smartpark.system.model
 * @Description: 入驻企业导出
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CompanyExcelExportDTO {

    private static final long serialVersionUID = 1L;

    @ColumnWidth(20)
    @ExcelProperty(value = "企业ID")
    private String id;

    @ColumnWidth(20)
    @ExcelProperty(value = "企业名称")
    private String name;

    @ColumnWidth(20)
    @ExcelProperty(value = "企业类型", converter = DictConverter.class)
    @DictField(dictType = "COMPANY_TYPE")
    private Integer companyType;

    @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    @ColumnWidth(20)
    private String parkOrgId;

    @ColumnWidth(30)
    @ExcelProperty(value = "注册地址")
    private String address;

    @ColumnWidth(30)
    @ExcelProperty(value = "统一社会信用代码")
    private String code;

    //TODO 来源
    @ExcelProperty(value = "来源", converter = DictConverter.class)
    @DictField(dictType = "ADD_DATA_SOURCE")
    private Integer source;

    @ColumnWidth(40)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "绑定房屋数量")
    private Integer houseCount;

}
