package com.crlink.smartpark.business.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@ApiModel(value = "商机excel导出模型")
public class BusinessChanceExcelVO implements Serializable {

    @ExcelProperty(value = "关联园区")
    @ColumnWidth(30)
    private String parkOrgName;

    @ExcelProperty(value = "商机名称")
    @ColumnWidth(30)
    private String chanceName;

    @ExcelProperty(value = "商机阶段", converter = DictConverter.class)
    @DictField(dictType = DictConstant.BUSINESS_CHANCE_STAGE)
    private Integer chanceStage;

    @ExcelProperty(value = "跟进人")
    @ColumnWidth(30)
    private String followEmployeeName;

    @ExcelProperty(value = "跟进时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime latestFollowTime;

    @ExcelProperty(value = "更新时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime updateTime;

}
