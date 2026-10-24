package com.crlink.smartpark.inspection.controller.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
public class InspectionPlanReqDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区组织Id")
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "计划名称")
    @NotNull(message = "计划名称不能为空")
    @Length(max = 30, min = 1)
    private String planName;

    @ApiModelProperty(value = "巡检类型")
    private Integer type;

    @ApiModelProperty(value = "巡检路线Id")
    @NotEmpty
    private String circuitId;

    @ApiModelProperty(value = "执行周期类型,0天，1周")
    @NotNull(message = "执行周期类型不能为空")
    private Integer cycleType;

    @ApiModelProperty(value = "执行规则")
    @NotEmpty
    private String executionRule;

//    @ApiModelProperty(value = "节假日顺延")
//    private boolean canDelay;

    @ApiModelProperty(value = "执行时间0")
    @NotNull
    @JsonDeserialize(converter = StringToLocalTimeConverter.class)
    private LocalTime executionTimeStart;

    @ApiModelProperty(value = "执行时间1")
    @JsonDeserialize(converter = StringToLocalTimeConverter.class)
    @NotNull
    private LocalTime executionTimeEnd;

    @ApiModelProperty(value = "生效日期0")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate effectDateStart;

    @ApiModelProperty(value = "生效日期1")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate effectDateEnd;

    @ApiModelProperty(value = "备注")
    private String remark;

    public static class StringToLocalTimeConverter extends StdConverter<String, LocalTime> {
        @Override
        public LocalTime convert(String value) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(value, timeFormatter);
        }
    }


}
