package com.crlink.smartpark.visitor.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 车辆管理-批量导入
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "车辆管理-批量导入", description = "车辆管理-批量导入")
public class CarImportDTO implements Serializable {
    private static final long serialVersionUID = -1864079343470512320L;

    @NotBlank(message = "车牌号不能为空")
    @ExcelProperty(value = "*车牌号")
    private String carNum;

    @NotBlank(message = "车主姓名不能为空")
    @ExcelProperty(value = "*车主姓名")
    private String carOwnerName;

    @NotBlank(message = "手机号不能为空")
    @Length(max = 11, message = "手机号最长11位数字")
    @ExcelProperty(value = "*手机号")
    private String mobile;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    @NotNull(message = "停车类型不能为空")
    @ExcelProperty(value = "*停车类型", converter = DictConverter.class)
    @DictField(dictType = DictConstant.PARK_TYPE)
    private Integer parkType;

    @NotBlank(message = "授权有效开始日期不能为空")
    @ExcelProperty(value = "*授权有效开始日期")
    private String authStartDateStr;

    @NotBlank(message = "授权有效结束日期不能为空")
    @ExcelProperty(value = "*授权有效结束日期")
    private String authEndDateStr;

}
