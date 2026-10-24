package com.crlink.smartpark.customer.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 报名记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ActivityRegisterRecord对象", description = "报名记录")
public class ActivityRegisterRecordReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    @ApiModelProperty(value = "活动ID")
    private String activityId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;


}
