package com.crlink.smartpark.system.dto.house;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class HouseSaveDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "楼层房号")
    @NotBlank(message = "楼层房号不能为空")
    private String name;

    @ApiModelProperty(value = "房屋编码")
    @Length(max = 30, message = "房屋编码30个字符以内")
    private String code;

    @ApiModelProperty(value = "所在楼层")
    @NotNull(message = "所在楼层不能为空")
    private Integer floor;

    @ApiModelProperty(value = "套内面积")
    private Double coverArea;

    @ApiModelProperty(value = "建筑面积")
    @NotNull(message = "建筑面积不能为空")
    private Double buildArea;

    @ApiModelProperty(value = "楼栋ID")
    @NotBlank(message = "楼栋ID不能为空")
    private String buildingId;

    @ApiModelProperty(value = "园区ID")
    @NotBlank(message = "园区ID不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "楼层属性")
    private Integer floorType;

//    @ApiModelProperty(value = "房屋状态")
//    private Integer houseStatus;

    @ApiModelProperty(value = "房屋属性")
    private Integer houseProperty;

    @ApiModelProperty(value = "房屋功能")
    @NotNull(message = "房屋功能不能为空")
    private Integer houseFunction;

    @ApiModelProperty(value = "朝向")
    private Integer orientation;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String remark;

    @ApiModelProperty(value = "创建人")
    @JsonIgnore
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonIgnore
    private String updateBy;

}
