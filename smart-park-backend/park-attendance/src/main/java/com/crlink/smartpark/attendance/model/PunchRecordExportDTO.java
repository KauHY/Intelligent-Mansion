package com.crlink.smartpark.attendance.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.excel.convert.ImageConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 打卡记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PunchRecord导出对象", description="打卡记录")
@ContentRowHeight(150)
public class PunchRecordExportDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "园区名")
    @ExcelProperty("所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ApiModelProperty(value = "公司名")
    @ExcelProperty("所属公司")
    @ColumnWidth(20)
    private String companyName;

    @ApiModelProperty(value = "员工名")
    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String employeeName;

    @ApiModelProperty(value = "打卡具体时间")
    @ExcelProperty(value = "打卡时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime punchDateTime;

    @ApiModelProperty(value = "打卡地点")
    @ExcelProperty("打卡地点")
    @ColumnWidth(20)
    private String location;

    @ApiModelProperty(value = "打卡类型:1-上下班打卡；2-外勤打卡")
    @ExcelProperty(value = "打卡方式", converter = DictConverter.class)
    @DictField(dictType = DictConstant.PUNCH_TYPE)
    @ColumnWidth(20)
    private Integer type;

    @ApiModelProperty(value = "外勤事由")
    @ExcelProperty("外勤事由")
    @ColumnWidth(20)
    private String reason;

    @ApiModelProperty(value = "图片key")
    @ExcelProperty(value = "外勤照片", converter = ImageConverter.class)
    @ColumnWidth(20)
    private String picKey;

}
