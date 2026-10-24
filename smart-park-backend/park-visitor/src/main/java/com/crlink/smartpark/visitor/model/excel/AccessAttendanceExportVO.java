package com.crlink.smartpark.visitor.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 园区运营平台-人行管理-通行记录关联考勤
 */
@Data
@ApiModel(value = "人行抓拍关联考勤")
@ContentRowHeight(20)
public class AccessAttendanceExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String userName;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    @ExcelProperty("日期")
    @ColumnWidth(20)
    private String recognizeDate;

    /**
     * 星期
     */
    @ApiModelProperty(value = "星期")
    @ExcelProperty("星期")
    @ColumnWidth(20)
    private String dayOfWeek;

    /**
     * 上班打卡状态
     */
    @ApiModelProperty(value = "上班打卡状态")
    @ExcelProperty("上班打卡状态")
    @ColumnWidth(20)
    private String statusOfPuncnIn;

    /**
     * 最早打卡时间
     */
    @ApiModelProperty(value = "最早打卡时间")
    @ExcelProperty("最早打卡时间")
    @ColumnWidth(20)
    private String earliestTime;

    /**
     * 下班打卡状态
     */
    @ApiModelProperty(value = "下班打卡状态")
    @ExcelProperty("下班打卡状态")
    @ColumnWidth(20)
    private String statusOfPuncnOut;

    /**
     * 最晚打卡时间
     */
    @ApiModelProperty(value = "最晚打卡时间")
    @ExcelProperty("最晚打卡时间")
    @ColumnWidth(20)
    private String latestTime;

}
