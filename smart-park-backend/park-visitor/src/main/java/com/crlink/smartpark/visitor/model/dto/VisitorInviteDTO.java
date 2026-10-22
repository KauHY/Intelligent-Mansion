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
public class VisitorInviteDTO {

    @ApiModelProperty(value = "访客类型ID", required = true)
    @NotBlank(message = "访客类型ID不能为空")
    private String visitorTypeId;

    @ApiModelProperty(value = "通行策略ID", required = true)
    @NotBlank(message = "通行策略ID不能为空")
    private String accessPolicyId;

    @ApiModelProperty(value = "访客名", required = true)
    @NotBlank(message = "访客名不能为空")
    private String visitorName;

    @ApiModelProperty(value = "访客电话号码", required = true)
    @NotBlank(message = "访客电话号码不能为空")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "访客电话号码不合法")
    private String visitorTel;

    @ApiModelProperty(value = "访客身份证号")
    @Pattern(regexp = Constants.REGEX_ID_CARD18, message = "身份证号格式异常")
    private String visitorIdCard;

    @ApiModelProperty(value = "访客车牌号")
    @Pattern(regexp = Constants.REGEX_CAR_PLATE, message = "访客车牌号不合法")
    @Length(max = 8, message = "访客车牌号不合法")
    private String carNum;

    @ApiModelProperty(value = "访客人脸图")
    private String visitorFaceImg;

    @ApiModelProperty(value = "邀请人备注")
    @Length(max = 30, message = "邀请人备注不能超过30个字符")
    private String invitorRemark;

    @ApiModelProperty(value = "来访事由 1:商务交流 2:面试 3:其他", required = true)
    @Range(min = 1, max = 3, message = "来访事由不合法")
    @NotNull(message = "来访事由不能为空")
    private Integer visitReason;

    @ApiModelProperty(value = "来访日期", required = true)
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    @NotNull(message = "来访日期不能为空")
    private LocalDate visitDate;

    @ApiModelProperty(value = "公司ID")
    @NotBlank(message = "公司ID不能为空")
    private String companyId;

}
