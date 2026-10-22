package com.crlink.smartpark.enterprise.model;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 企业产业链
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "StateReqDTO请求对象", description = "企业服务")
public class StateReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "状态：1-发布；2-停用/下架", required = true)
    @CustomValid({"1", "2"})
    private Integer state;


}
