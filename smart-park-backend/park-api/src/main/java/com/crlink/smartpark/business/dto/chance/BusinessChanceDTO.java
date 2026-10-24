package com.crlink.smartpark.business.dto.chance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商机表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BusinessChance对象", description = "商机表")
public class BusinessChanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    private String parkOrgId;

    /**
     * 商机名称
     */
    @ApiModelProperty(value = "商机名称")
    private String chanceName;

    /**
     * 商机来源：1商机录入，2入驻申请
     */
    @ApiModelProperty(value = "商机来源：1商机录入，2入驻申请")
    private Integer businessSource;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    @ApiModelProperty(value = "联系人邮箱")
    private String contactMail;

    /**
     * 需求数量
     */
    @ApiModelProperty(value = "需求数量")
    private Integer quantityNumber;

    /**
     * 期望价格（元/㎡/月）
     */
    @ApiModelProperty(value = "期望价格（元/㎡/月）")
    private Double expectedPrice;

    /**
     * 商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭
     */
    @ApiModelProperty(value = "商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭")
    private Integer chanceStage;

    /**
     * 线索来源：1老客户介绍，2政府资源，3公共资源，4第三方资源，5内部资源，6网络资源，7招商人员资源
     */
    @ApiModelProperty(value = "线索来源：1老客户介绍，2政府资源，3公共资源，4第三方资源，5内部资源，6网络资源，7招商人员资源")
    private Integer threadSource;

    /**
     * 跟进人Id
     */
    @ApiModelProperty(value = "跟进人Id")
    private String followEmployeeId;

    /**
     * 意向房源:房源招商Id集合
     */
    @ApiModelProperty(value = "意向房源:房屋Id集合")
    private List<String> houseIds;

    /**
     * 最近实际跟进时间
     */
    @ApiModelProperty(value = "最近实际跟进时间")
    private LocalDateTime latestFollowTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 提醒频次: 1 天、2 周
     */
    @ApiModelProperty(value = "提醒频次: 1 天、2 周")
    private Integer remindFrequency;

    /**
     * 按天提醒
     */
    @ApiModelProperty(value = "按天提醒")
    private Integer remindByDay;

    /**
     * 按周提醒
     */
    @ApiModelProperty(value = "按周提醒:0 全选，1~~7 周一~~周日")
    private String remindByWeek;

    /**
     * 提醒时间
     */
    @ApiModelProperty(value = "提醒时间")
    private String remindTime;

    /**
     * 提醒内容
     */
    @ApiModelProperty(value = "提醒内容")
    private String remindContent;


    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
