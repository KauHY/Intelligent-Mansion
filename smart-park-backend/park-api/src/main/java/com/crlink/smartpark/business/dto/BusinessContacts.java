package com.crlink.smartpark.business.dto;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 客户_业务联系人
 * </p>
 * */
@Data
public class BusinessContacts implements Serializable {

    @ApiModelProperty(value = "联系人")
    @Length(max = 30)
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    @Length(max = 20)
    private String tel;

    @ApiModelProperty(value = "联系人邮箱")
    @Length(max = 30)
    private String mail;

    @ApiModelProperty(value = "联系人部门")
    @Length(max = 30)
    private String department;

    @ApiModelProperty(value = "联系人职位")
    @Length(max = 30)
    private String position;

    @ApiModelProperty(value = "备注")
    @Length(max = 200)
    private String remark;


}
