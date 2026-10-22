package com.crlink.smartpark.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 多级分类表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MultistageCategoryUpdate请求对象", description="多级分类表")
public class MultistageCategoryUpdateReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @NotBlank
    private String id;

    @ApiModelProperty(value = "是否能扩展：false-否；true-是")
    private Boolean expand;

    @ApiModelProperty(value = "是否可选：false-否；true-是")
    private Boolean selectable;

    @ApiModelProperty(value = "禁用：false-否；true-是")
    private Boolean disabled;

    @ApiModelProperty(value = "名称")
    private String label;

}
