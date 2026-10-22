package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报事投诉、报修服务表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Order对象", description = "报事投诉、报修服务表")
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "报事报修工单编号")
    private String orderNo;

    @ApiModelProperty(value = "园区编码", required = true)
    @NotNull
    private String parkOrgId;

    @ApiModelProperty(value = "报事报修反馈人", required = true)
    @NotBlank(message = "反馈人不能为空")
    @Length(max = 20, message = "反馈人最多输入20个字")
    private String orderOwnerName;

    @ApiModelProperty(value = "报事报修人电话", required = true)
    @NotBlank(message = "反馈人电话不能为空")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    private String orderOwnerMobile;

    @ApiModelProperty(value = "报事报修类型：1-报事投诉、2-报修服务", required = true)
    @Min(value = 1)
    @Max(value = 2)
    private Integer orderType;

    @ApiModelProperty(value = "报事投诉类型 ORDERTYPE-1：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务、20-其他	" +
            "报修服务类型ORDERTYPE-2：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备、26-其他", required = true)
    @NotNull(message = "报事投诉类型不能为空!")
    @Min(value = 11, message = "类型选择错误")
    @Max(value = 26, message = "类型选择错误")
    private Integer orderTypeItem;

    @ApiModelProperty(value = "报事报修内容", required = true)
//    @Length(max = 1000, message = "内容最多1000字！")
    @NotBlank(message = "内容不能为空")
    private String orderContent;

    @ApiModelProperty(value = "报事报修创建时间")
    private LocalDateTime orderTime;

    @ApiModelProperty(value = "处理状态：1待派单,2待处理,8已完成,9已撤单,")
    private Integer orderState;

    @ApiModelProperty(value = "图片uri，多个时逗号分隔")
//    @NotNull(message = "图片不能为空！")
    private String orderUrl;

    @ApiModelProperty(value = "报事报修详细地址", required = true)
    @NotBlank(message = "详细地址不能为空")
    @Length(max = 30, message = "详细地址最多输入30个字")
    private String orderAddress;


}
