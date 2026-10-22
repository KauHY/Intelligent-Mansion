package com.crlink.smartpark.system.dto.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 标签表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Tag对象", description = "标签表")
public class TagDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签Id")
    private String id;

    @ApiModelProperty(value = "标签名")
    private String tagName;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "公司Id")
    private String companyId;
    private String companyName;


    private LocalDateTime updateTime;
    

}
