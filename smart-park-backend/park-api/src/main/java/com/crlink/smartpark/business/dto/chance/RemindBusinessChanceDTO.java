package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RemindBusinessChanceDTO", description = "提醒商机")
public class RemindBusinessChanceDTO {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "提醒频次: 1 天、2 周")
    @NotNull(message = "提醒频次不能为空")
    @Min(1)
    @Max(2)
    private Integer remindFrequency;

    @ApiModelProperty(value = "按天提醒:天数")
    private Integer remindByDay;

    @ApiModelProperty(value = "按周提醒: 0 全选，1~~7 周一~~周日")
    private List<Integer> remindByWeek;

    @ApiModelProperty(value = "提醒时间: HH:mm")
    @NotBlank(message = "提醒时间不能为空")
    @Pattern(regexp = Constants.REMIND_TIME_PATTERN)
    private String remindTime;

    @ApiModelProperty(value = "提醒内容")
    @NotBlank(message = "提醒内容不能为空")
    @Length(max = 999, message = "提醒内容最多999字")
    private String remindContent;
}
