package com.crlink.smartpark.attendance.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 外勤申请记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FieldworkApplyRecord请求对象", description="外勤申请记录")
public class    FieldworkApplyRecordReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "外勤开始时间", required = true)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS, timezone = Constants.TIMEZONE)
    @NotNull
    private LocalDateTime fieldworkStartTime;

    @ApiModelProperty(value = "外勤结束时间", required = true)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS, timezone = Constants.TIMEZONE)
    @NotNull
    private LocalDateTime fieldworkEndTime;

    @ApiModelProperty(value = "事由", required = true)
    @NotBlank(message = "事由不能为空")
    private String reason;

    @ApiModelProperty(value = "地点", required = true)
    @NotBlank(message = "事由不能为空")
    private String situation;

}
