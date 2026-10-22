package com.crlink.smartpark.system.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.common.excel.convert.ImageConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.common.excel.convert.StatusConverter;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.JobCodesJobNamesConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ContentRowHeight(150)
public class EmployeeExcelExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty(value = "照片", converter = ImageConverter.class)
    @ColumnWidth(35)
    private String pics;

    @ExcelProperty(value = "所属企业", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 4)
    private String companyId;

    @ExcelProperty(value = "关联园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ExcelProperty(value = "员工标签", converter = CommonConvert.class)
    @ColumnWidth(40)
    private List<String> tagNames;


    @ExcelProperty(value = "岗位", converter = JobCodesJobNamesConverter.class)
    private List<Integer> jobCodes;

    /**
     * 2022.03.23 v12 新增字段
     */
    @ExcelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @ExcelProperty(value = "状态", converter = StatusConverter.class)
    private Integer status;

}
