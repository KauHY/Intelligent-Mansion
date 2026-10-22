package com.crlink.smartpark.visitor.model.dto;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class AppointmentApplyDTO {

    @ApiModelProperty(value = "访客名", required = true)
    @NotBlank(message = "访客名不允许为空")
    @Length(max = 30, message = "访客备注不能超过30个字符")
    private String visitorName;

    @ApiModelProperty(value = "访客电话号码", required = true)
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "访客电话号码格式不合法")
    private String visitorTel;

    @ApiModelProperty(value = "访客身份证号", required = true)
    @Pattern(regexp = Constants.REGEX_ID_CARD18, message = "访客身份证号不合法")
    @NotBlank(message = "访客身份证号不能为空")
    private String visitorIdCard;

    @ApiModelProperty(value = "访客车牌号")
    @Pattern(regexp = Constants.REGEX_CAR_PLATE, message = "访客车牌号不合法")
    @Length(max = 8, message = "访客车牌号不合法")
    private String carNum;

    @ApiModelProperty(value = "访客人脸图", required = true)
    @NotBlank(message = "访客人脸图不能为空")
    private String visitorFaceImg;

    @ApiModelProperty(value = "访客人数")
    @NotNull(message = "访客人数必填")
    @Range(min = 1, max = 999, message = "访客人数最大为1-999")
    private Integer visitorCount;

    @ApiModelProperty(value = "访客备注")
    @Length(max = 30, message = "访客备注不能超过30个字符")
    private String visitorRemark;

    @ApiModelProperty(value = "来访事由 1:商务交流 2:面试 3:其他", required = true)
    @Range(min = 1, max = 3, message = "来访事由不合法")
    @NotNull(message = "来访事由不能为空")
    private Integer visitReason;

    @ApiModelProperty(value = "访问的企业ID,被访人的公司，邀请人的公司")
    @NotBlank(message = "访问企业不能为空")
    private String companyId;

    @ApiModelProperty(value = "被访人姓名，邀请人姓名", required = true)
    @NotBlank(message = "被访人姓名不允许为空")
    @Length(max = 30, message = "访客备注不能超过30个字符")
    private String invitorName;

    @ApiModelProperty(value = "被访人电话号码，邀请人电话", required = true)
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "被访人电话号码格式不合法")
    private String invitorTel;

    @ApiModelProperty(value = "来访日期", required = true)
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    @NotNull(message = "来访日期不允许为空")
    private LocalDate visitDate;

}
