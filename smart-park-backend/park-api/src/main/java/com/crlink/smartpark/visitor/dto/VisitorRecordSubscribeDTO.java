package com.crlink.smartpark.visitor.dto;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 访客记录登记实体
 * */
@Data
@ApiModel(value = "访客记录登记实体")
public class VisitorRecordSubscribeDTO implements Serializable {
    private static final long serialVersionUID = 793710896335440442L;
    /**
     * 预约记录ID
     */
    @ApiModelProperty(value = "预约记录ID")
    private String id;

    /**
     * 访客名
     */
    @ApiModelProperty(value = "访客名", required = true)
    @NotBlank(message = "访客名不能为空")
    private String visitorName;

    /**
     * 访客电话号码
     */
    @ApiModelProperty(value = "访客电话号码", required = true)
    @NotBlank(message = "访客电话号码不能为空")
    private String visitorTel;

    /**
     * 访客身份证号
     */
    @ApiModelProperty(value = "访客身份证号", required = true)
    @NotBlank(message = "访客身份证号不能为空")
    @Pattern(regexp = Constants.REGEX_ID_CARD18, message = "身份证号格式异常")
    private String visitorIdCard;

    /**
     * 访客人数
     */
    @ApiModelProperty(value = "访客人数")
    @Range(min = 1, message = "访客人数异常")
    @NotNull(message = "访客人数异常")
    private Integer visitorCount;

    /**
     * 访客人脸图url路径
     */
    @ApiModelProperty(value = "访客人脸图url路径", required = true)
    @NotBlank(message = "访客人脸图url路径不能为空")
    private String visitorFaceImg;

    /**
     * 来访事由 1:商务交流 2:面试 3:其他
     */
    @ApiModelProperty(value = "来访事由 1:商务交流 2:面试 3:其他", required = true)
    @NotNull(message = "来访事由 1:商务交流 2:面试 3:其他不能为空")
    @Range(min = 1, max = 3, message = "来访事由参数异常")
    private Integer visitReason;

    /**
     * 被访人姓名
     */
    @ApiModelProperty(value = "被访人姓名", required = true)
    @NotBlank(message = "被访人姓名不能为空")
    private String invitorName;

    /**
     * 被访人电话号码
     */
    @ApiModelProperty(value = "被访人电话号码", required = true)
    @NotBlank(message = "被访人电话号码不能为空")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "手机号格式不正确")
    private String invitorTel;

    /**
     * 访客备注
     */
    @ApiModelProperty(value = "访客备注")
    private String visitorRemark;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     * 入驻企业ID,无预约ID时不能为空
     */
    @ApiModelProperty(value = "入驻企业ID,无预约ID时不能为空")
    private String companyId;

    /**
     * 通行策略ID,通行策略ID不能为空
     */
    @ApiModelProperty(value = "通行策略ID,无预约ID时不能为空")
    private String accessPolicyId;

}
