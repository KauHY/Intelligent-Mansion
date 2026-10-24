package com.crlink.smartpark.system.vo.builiding;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 楼栋表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BuildingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "楼栋名称")
    private String name;

    @ApiModelProperty(value = "楼栋编码")
    private String code;

    @ApiModelProperty(value = "总层数")
    private Integer totalFloor;

    @ApiModelProperty(value = "建成时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate buildTime;

    @ApiModelProperty(value = "电梯：0：无，1：有")
    private Integer elevator;

    @ApiModelProperty(value = "占地面积")
    private Double coverArea;

    @ApiModelProperty(value = "建筑面积")
    private Double buildArea;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "楼栋属性: 字典 BUILDING_PROPERTY 1办公、2食堂、3公寓、4其他", required = true)
    private Integer property;

    @ApiModelProperty(value = "备注")
    private String remark;

}
