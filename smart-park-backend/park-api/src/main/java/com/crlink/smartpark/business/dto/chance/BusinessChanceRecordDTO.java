package com.crlink.smartpark.business.dto.chance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商机操作记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BusinessChanceRecord对象", description = "商机操作记录")
public class BusinessChanceRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商机id")
    private String businessChanceId;

    @ApiModelProperty(value = "记录类型：1创建商机、2分配商机、3领取商机、4跟进、5提醒商机、6转成客户、7转移商机、8释放商机、9删除商机")
    private Integer recordType;

    @ApiModelProperty(value = "商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭")
    private Integer chanceStage;

    @ApiModelProperty(value = "跟进类型：1拜访，2接待，3参观")
    private Integer followUpType;

    @ApiModelProperty(value = "实际跟进时间")
    private LocalDateTime actualFollowTime;

    @ApiModelProperty(value = "跟进内容")
    private String followUpContent;

    @ApiModelProperty(value = "跟进人Id")
    private String followEmployeeId;

   
}
