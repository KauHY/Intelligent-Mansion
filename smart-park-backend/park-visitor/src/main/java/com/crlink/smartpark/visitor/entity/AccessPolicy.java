package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.crlink.smartpark.core.mphandler.SetTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人行通行策略表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "vis_access_policy", autoResultMap = true)
public class AccessPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 策略名称
     */
    private String policyName;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 企业ID
     */
    @TableField(typeHandler = SetTypeHandler.class)
    private Set<String> companyId;

    /**
     * 允许员工标签ID(逗号隔开)
     */
    @TableField(typeHandler = SetTypeHandler.class)
    private Set<String> tagIds;

    /**
     * 访客通行：0-不适应，1-适用
     */
    private Integer allowVisitor;

    /**
     * 设备id(逗号隔开)
     */
    @TableField(typeHandler = SetTypeHandler.class)
    private Set<String> deviceIds;

    /**
     * 策略生效时间
     */
    private LocalDate policyStartTime;

    /**
     * 策略过期时间
     */
    private LocalDate policyExpireTime;

    /**
     * 0-全时段，1-指定时段
     */
    private Integer allTime;

    /**
     * 指定时段具体信息：dayOfWeek-选中天（数组）;startTime-开始时间(时分秒);endTime-结束时间(时分秒)(JSON)
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Map<String, Object> specifiedTimePeriod;

    /**
     * 状态：0禁用1启用
     */
    private Integer status;

    /**
     * 已过期 0：否 1：是
     */
    private Integer expire;

    /**
     * 删除状态
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 修改者
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
