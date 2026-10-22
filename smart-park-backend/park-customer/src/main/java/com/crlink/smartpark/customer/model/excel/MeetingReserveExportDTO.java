package com.crlink.smartpark.customer.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: MeetingReserveExportDTO
 * @Package com.crlink.smartpark.customer.model.excel
 * @Description: 租赁会议室预定订单导出
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingReserveExportDTO请求对象", description = "租赁会议室预订")
public class MeetingReserveExportDTO {

    @ExcelProperty(value = "申请编号")
    private String reserveNo;

    @ExcelProperty(value = "会议室名称")
    @ColumnWidth(20)
    private String meetingId;

    @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    @ColumnWidth(20)
    private String parkOrgId;

    @ExcelProperty(value = "预定时间")
    @ColumnWidth(30)
    private String reserveTimeList;

    @ExcelProperty(value = "提交订单金额(元)")
    @ColumnWidth(20)
    private BigDecimal estimatedAmount;

    @ExcelProperty(value = "实际订单金额(元)")
    @ColumnWidth(20)
    private BigDecimal reviewAmount;

    @ExcelProperty(value = "申请单位")
    @ColumnWidth(20)
    private String applicantUnit;

    @ExcelProperty(value = "联系人")
    @ColumnWidth(20)
    private String applicantName;

    @ExcelProperty(value = "手机号码")
    @ColumnWidth(20)
    private String mobile;

    @ColumnWidth(20)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "提交时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime applicantTime;

    @ExcelProperty(value = "状态", converter = DictConverter.class)
    @DictField(dictType = "CUS_MEETING_AUDIT_STATUS")
    private Integer reviewStatus;
}
