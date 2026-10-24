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
public class CustomerInfoListVO {

    private String id;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    @ApiModelProperty(value = "所属园区")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "统一社会信用代码")
    private String code;

    @ApiModelProperty(value = "客户标签id")
    private String tagId;

    @ApiModelProperty(value = "客户标签")
    private String tagName;

    @ApiModelProperty(value = "客户类型 0：物业公司，1：普通企业")
    private Integer type;

    @ApiModelProperty(value = "客户状态,1，已成交，2未成交，3历史成交")
    private Integer status;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String tel;
}
