package com.crlink.smartpark.fee.controller.request;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 收费项
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "StatusReqDTO对象", description = "状态请求对象")
public class StatusReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "状态：0-关闭；1-开启")
    @CustomValid(value = {"0", "1"}, message = "状态值错误")
    private Integer status;

}
