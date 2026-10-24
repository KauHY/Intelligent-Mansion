package com.crlink.smartpark.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "bus_business_chance", autoResultMap = true)
public class BusinessChance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String parkOrgId;

    /**
     * 商机名称
     */
    private String chanceName;

    /**
     * 商机来源：1商机录入，2入驻申请
     */
    private Integer businessSource;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    private String contactMail;

    /**
     * 需求数量
     */
    private Integer quantityNumber;

    /**
     * 期望价格（元/㎡/月）
     */
    private Double expectedPrice;

    /**
     * 商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭
     */
    private Integer chanceStage;

    /**
     * 线索来源：1老客户介绍，2政府资源，3公共资源，4第三方资源，5内部资源，6网络资源，7招商人员资源
     */
    private Integer threadSource;

    /**
     * 跟进人Id
     */
    private String followEmployeeId;

    /**
     * 意向房源:房源招商Id集合
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> houseIds;

    /**
     * 最近实际跟进时间
     */
    private LocalDateTime latestFollowTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提醒频次: 1 天、2 周
     */
    private Integer remindFrequency;

    /**
     * 按天提醒
     */
    private Integer remindByDay;

    /**
     * 按周提醒: 0 全选，1~~7 周一~~周日
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> remindByWeek;

    /**
     * 提醒时间
     */
    private String remindTime;

    /**
     * 提醒内容
     */
    private String remindContent;

    /**
     * 删除状态，0未删除，1已删除
     */
    private Boolean deleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
