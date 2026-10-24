package com.crlink.smartpark.customer.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EmployeeListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    @ApiModelProperty(value = "照片（英文逗号分隔）")
    private String pics;

    @ApiModelProperty(value = "所属园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "所属入驻企业名")
    private String companyName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "状态")
    private Boolean enable;

}
