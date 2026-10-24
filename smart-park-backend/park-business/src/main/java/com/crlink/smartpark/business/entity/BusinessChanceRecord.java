package com.crlink.smartpark.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("bus_business_chance_record")
public class BusinessChanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 商机id
     */
    private String businessChanceId;

    /**
     * 记录类型：1创建商机、2分配商机、3领取商机、4跟进、5提醒商机、6转成客户、7转移商机、8释放商机、9删除商机
     */
    private Integer recordType;

    /**
     * 商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭
     */
    private Integer chanceStage;

    /**
     * 跟进类型：1拜访，2接待，3参观
     */
    private Integer followType;

    /**
     * 实际跟进时间
     */
    private LocalDateTime followTime;

    /**
     * 跟进内容
     */
    private String followContent;

    /**
     * 跟进人Id
     */
    private String followEmployeeId;


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
