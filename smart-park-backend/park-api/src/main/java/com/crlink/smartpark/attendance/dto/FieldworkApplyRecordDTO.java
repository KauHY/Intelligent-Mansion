package com.crlink.smartpark.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 外勤申请记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FieldworkApplyRecord响应对象", description="外勤申请记录")
public class FieldworkApplyRecordDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    private String parkOrgName;

    @ApiModelProperty(value = "公司")
    private String companyId;

    @ApiModelProperty(value = "公司名")
    private String companyName;

    @ApiModelProperty(value = "申请人id")
    private String employeeId;

    @ApiModelProperty(value = "申请人姓名")
    private String employeeName;

    @ApiModelProperty(value = "申请人号码")
    private String mobile;

    @ApiModelProperty(value = "外勤开始时间")
    private LocalDateTime fieldworkStartTime;

    @ApiModelProperty(value = "外勤结束时间")
    private LocalDateTime fieldworkEndTime;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "地点")
    @ExcelProperty("外勤地点")
    @ColumnWidth(20)
    private String situation;

    @ApiModelProperty(value = "事由")
    @ExcelProperty("事由")
    @ColumnWidth(20)
    private String reason;

    @ApiModelProperty(value = "状态：1-待审批；2-审核通过；3-审批不通过；4-已取消")
    private Integer state;

    @ApiModelProperty(value = "审批意见")
    private String auditOpinion;


}
