package com.crlink.smartpark.business.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CustomerInfoWxVO {

    private String id;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "统一社会信用代码")
    private String code;

    @ApiModelProperty(value = "客户类型 0：物业公司，1：普通企业")
    private Integer type;

    @ApiModelProperty(value = "客户状态,1，已成交，2未成交，3历史成交")
    private Integer status;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactsTel;

    @ApiModelProperty(value = "联系人邮箱")
    private String contactsMail;

    private String employeeId;

    @ApiModelProperty(value = "跟进人")
    @Id2objectField(code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    private String employeeName;
}
