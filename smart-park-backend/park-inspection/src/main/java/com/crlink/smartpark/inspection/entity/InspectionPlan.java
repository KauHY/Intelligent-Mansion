package com.crlink.smartpark.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 巡检计划
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ins_inspection_plan")
public class InspectionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 园区组织Id
     */
    private String parkOrgId;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 巡检类型
     */
    private Integer type;

    /**
     * 巡检路线Id
     */
    private String circuitId;

    /**
     * 执行周期类型,0天，1周
     */
    private Integer cycleType;

    /**
     * 执行规则
     */
    private String executionRule;

    /**
     * 节假日是否顺延
     */
    private Boolean canDelay;

    /**
     * 执行时间0
     */
    private LocalTime executionTimeStart;

    /**
     * 执行时间1
     */
    private LocalTime executionTimeEnd;

    /**
     * 生效日期0
     */
    private LocalDate effectDateStart;

    /**
     * 生效日期1
     */
    private LocalDate effectDateEnd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 计划状态，0关闭，1开启，2删除
     */
    private Integer status;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;


}
