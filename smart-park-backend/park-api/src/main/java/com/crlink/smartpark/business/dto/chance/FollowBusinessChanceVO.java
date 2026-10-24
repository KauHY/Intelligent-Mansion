package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class FollowBusinessChanceVO {

    private String id;

    private String followEmployee;

    private String createBy;

    @ApiModelProperty(value = "跟进人姓名")
    @Id2objectField(idField = "createBy", code = Id2objCodeConstant.USER_NAME_CODE)
    private String followEmployeeName;

    @ApiModelProperty(value = "跟进类型：1拜访，2接待，3参观")
    private Integer followType;

    @ApiModelProperty("商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭")
    private Integer chanceStage;

    @ApiModelProperty(value = "跟进时间")
    private LocalDateTime followTime;

    @ApiModelProperty(value = "跟进内容")
    private String followContent;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
