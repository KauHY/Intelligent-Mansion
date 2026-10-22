package com.crlink.smartpark.inspection.dto;

import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 巡检计划
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InspectionPlan对象", description = "巡检计划")
public class InspectionPlanDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private String id;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "巡检类型")
    private Integer type;

    @ApiModelProperty(value = "巡检路线Id")
    private String circuitId;
    private String circuitName;

    @ApiModelProperty(value = "执行周期类型,0天，1周")
    private Integer cycleType;

    @ApiModelProperty(value = "执行规则")
    private String executionRule;

//    @ApiModelProperty(value = "节假日是否顺延")
//    private Boolean canDelay;

    @ApiModelProperty(value = "执行时间0")
    private String executionTimeStart;

    @ApiModelProperty(value = "执行时间1")
    private String executionTimeEnd;

    @ApiModelProperty(value = "生效日期0")
    private LocalDate effectDateStart;

    @ApiModelProperty(value = "生效日期1")
    private LocalDate effectDateEnd;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "计划状态，0关闭，1开启，2删除")
    private Integer status;


    private LocalDate updateTime;


    private List<DictVO> employees;

    public static LocalTime parse(String localTime) {
        return LocalTime.parse(localTime, formatter);
    }

    public static String format(LocalTime localTime) {
        return localTime.format(formatter);
    }


}
