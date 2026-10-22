package com.crlink.smartpark.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 巡检计划巡检人关系表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ins_inspection_employee_relation")
public class InspectionEmployeeRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 巡检计划 1
     * 巡检任务 2
     */
    private Integer type;

    /**
     * 巡检计划Id或巡检任务Id
     */
    private String targetId;

    /**
     * 巡检人Id
     */
    private String employeeId;

    private Boolean deleted;


}
