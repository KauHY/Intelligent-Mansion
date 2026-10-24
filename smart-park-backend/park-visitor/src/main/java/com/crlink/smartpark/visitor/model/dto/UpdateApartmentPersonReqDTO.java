package com.crlink.smartpark.visitor.model.dto;

import com.crlink.smartpark.core.validation.CustomValid;
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
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UpdateApartmentPersonReqDTO请求对象", description = "")
public class UpdateApartmentPersonReqDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "状态，0：禁用 1：启用", required = true)
    @NotNull(message = "状态不能为空")
    @CustomValid(value = {"0", "1"}, message = "操作类型值错误")
    private Integer status;

}
