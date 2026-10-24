package com.crlink.smartpark.system.dto.wxuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 微信小程序用户
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "WxUser对象", description = "微信小程序用户")
public class WxUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户授权code")
    @NotNull(message = "用户授权code不能为空")
    private String code;

    @ApiModelProperty(value = "encryptedData")
    @NotNull(message = "encryptedData不能为空")
    private String encryptedData;

    @ApiModelProperty(value = "iv")
    @NotNull(message = "iv不能为空")
    private String iv;

    @NotEmpty(message = "appId不能未空")
    private String appId;

    //@ApiModelProperty(value = "用户头像url")
    //@NotNull(message = "头像url不能为空")
    //private String avatarUrl;

    //@ApiModelProperty(value = "用户昵称")
    //@NotNull(message = "用户昵称不能为空")
    //private String nickName;

}
