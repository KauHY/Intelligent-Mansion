package com.crlink.smartpark.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.crlink.smartpark.inspection.dto.TaskContentDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "ins_inspection_task",autoResultMap = true)
public class InspectionTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String parkOrgId;

    private String taskName;

    @ApiModelProperty(value = "巡检类型")
    private Integer type;


    @TableField(typeHandler = JacksonTypeHandler.class)
    public TaskContentDTO taskContent;

    /**
     * 执行日期
     */
    private LocalDate taskDate;
    /**
     * 执行时间0
     */
    private LocalTime executionTimeStart;

    /**
     * 执行时间1
     */
    private LocalTime executionTimeEnd;

    /**
     * 完成时间
     */
    private LocalDateTime completionTime;

    /**
     * 实际巡检人
     */
    private String actualEmployeeId;

    /**
     * 巡检状态
     */

    private Integer status;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
