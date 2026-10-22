package com.crlink.smartpark.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 客户标签
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomerTag对象", description = "客户标签")
public class CustomerTagDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "客户标签名称", required = true)
    @Length(max = 30, message = "客户标签名称不能超过30字")
    private String name;


}
