package com.crlink.smartpark.customer.controller.request.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 会议室租赁管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingRent请求对象", description = "会议室租赁管理")
public class MeetingRentStatusDTO implements Serializable {


    @ApiModelProperty(value = "主键id")
    @NotBlank
    private String id;

    @ApiModelProperty(value = "状态：1开启 0关闭", required = true)
    @NotNull
    private Integer status;


}
