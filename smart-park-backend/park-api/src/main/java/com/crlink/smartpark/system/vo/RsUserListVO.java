package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统用户表
 * </p>
 * */
@Data
public class RsUserListVO implements Serializable {

    @ApiModelProperty(value = "用户名id")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String realName;

}
