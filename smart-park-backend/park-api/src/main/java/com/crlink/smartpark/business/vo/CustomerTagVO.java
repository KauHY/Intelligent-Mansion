package com.crlink.smartpark.business.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class CustomerTagVO {

    @ApiModelProperty(value = "客户标签id")
    private String id;

    private String parkOrgId;

    @ApiModelProperty(value = "所属园区")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "客户标签名称")
    private String name;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
