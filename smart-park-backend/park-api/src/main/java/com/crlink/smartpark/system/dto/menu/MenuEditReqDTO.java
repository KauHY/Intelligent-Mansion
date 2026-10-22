package com.crlink.smartpark.system.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class MenuEditReqDTO implements Serializable {
    @NotEmpty
    private String id;

    @ApiModelProperty(value = "名称")
    @Length(max = 30, min = 1)
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Double sort;

    @ApiModelProperty(value = "父节点id")
    @NotEmpty
    private String parentId;

//    @ApiModelProperty(value = "状态：0：不显示，1：显示")
//    @Min(0)
//    @Max(1)
//    private Integer status;

}
