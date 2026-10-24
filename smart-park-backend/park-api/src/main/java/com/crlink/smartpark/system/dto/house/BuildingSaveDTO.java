package com.crlink.smartpark.system.dto.house;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class BuildingSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "楼栋名称")
    @NotBlank(message = "楼栋名不能为空")
    @Length(max = 30, message = "楼栋名称不能超过30字")
    private String name;

    @ApiModelProperty(value = "楼栋编码")
    @Length(max = 30, message = "楼栋编码30个字符以内")
    private String code;

    @ApiModelProperty(value = "总层数")
    @NotNull(message = "总层数不能为空！")
    private Integer totalFloor;

    @ApiModelProperty(value = "建成时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate buildTime;

    @ApiModelProperty(value = "电梯无:0,有 1")
    private Integer elevator;

    @ApiModelProperty(value = "占地面积")
    private Double coverArea;

    @ApiModelProperty(value = "建筑面积")
    private Double buildArea;

    @ApiModelProperty(value = "楼栋属性: 字典 BUILDING_PROPERTY 1办公、2食堂、3公寓、4其他", required = true)
    @NotNull(message = "楼栋属性不能为空！")
    private Integer property;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String remark;

    @ApiModelProperty(value = "园区ID")
    @NotBlank(message = "园区ID不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "创建人")
    @JsonIgnore
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonIgnore
    private String updateBy;

}
