package com.crlink.smartpark.business.dto;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CustomerInfoWxReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "客户名称", required = true)
    @Length(max = 30, message = "客户名称不能超过30字")
    private String name;

    @ApiModelProperty(value = "园区id", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "统一社会信用代码", required = true)
    @Length(min = 18, max = 18)
    private String code;

    @ApiModelProperty(value = "客户类型（COMPANY_TYPE） 0物业公司、1其他、2科研院所、3事业单位、4机关单位、5科技型企业", required = true)
    private Integer type;

    @ApiModelProperty(value = "客户状态,1，已成交，2未成交，3历史成交", required = true)
    private Integer status;

    @ApiModelProperty(value = "联系人", required = true)
    @Length(max = 30, message = "不能超过30字")
    private String contacts;

    @ApiModelProperty(value = "联系电话", required = true)
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    private String contactsTel;

    @ApiModelProperty(value = "联系人邮箱")
    private String contactsMail;

    @ApiModelProperty(value = "跟进人", required = true)
    private String employeeId;
}
