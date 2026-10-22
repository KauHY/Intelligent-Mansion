package com.crlink.smartpark.system.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmployeeExcelImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String parkOrgId;

    @ExcelIgnore
    private String companyId;

    //    @ExcelProperty("*姓名")
    @ExcelProperty(index = 0, value = "*姓名")
    private String name;

    //    @ExcelProperty("*手机号")
    @ExcelProperty(index = 1, value = "*手机号")
    private String mobile;

    //    @ExcelProperty(value = "*照片文件路径", converter = ImageConverter.class)
    // @ExcelProperty(index = 2, value = "*照片文件路径", converter = PictureConverter.class)
    @ExcelProperty(index = 2, value = "*照片文件路径")
    private String pics;

    private String uploadPic;


    //    @ExcelProperty(value = "员工标签", converter = List2StringConverter.class)
    @ExcelProperty(index = 3, value = "员工标签", converter = CommonConvert.class)
    private List<String> tagNames;


    //    @ExcelProperty(value = "岗位", converter = JobCodesJobNamesConverter.class)
    @ExcelProperty(index = 4, value = "岗位", converter = CommonConvert.class)
    private List<String> jobIds;

    /**
     * 2022.03.23 v12 新增字段
     */
    @ExcelProperty(index = 5, value = "备注")
    private String remark;

}
