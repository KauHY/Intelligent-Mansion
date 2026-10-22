package com.crlink.smartpark.system.dto.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Tag对象", description = "标签表")
public class TagReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签Id")
    private String id;

    @ApiModelProperty(value = "标签名")
    @Length(max = 30)
    private String tagName;

    @ApiModelProperty(value = "园区组织Id")
    @NotNull
    private String parkOrgId;

    @ApiModelProperty(value = "公司Id")
    @NotNull
    private String companyId;

}