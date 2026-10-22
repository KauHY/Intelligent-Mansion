package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FollowBusinessChanceDTO", description = "跟进商机")
public class FollowBusinessChanceDTO {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "跟进类型：1拜访，2接待，3参观")
    @NotNull(message = "跟进类型不能为空")
    private Integer followType;

    @ApiModelProperty("商机阶段：BUSINESS_CHANCE_STAGE :1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭")
    @NotNull
    private Integer chanceStage;


    @ApiModelProperty(value = "跟进时间")
    @NotNull(message = "跟进时间不能为空")
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime followTime;

    @ApiModelProperty(value = "跟进内容")
    @Length(max = 999, message = "跟进内容最多999字符")
    private String followContent;
}
