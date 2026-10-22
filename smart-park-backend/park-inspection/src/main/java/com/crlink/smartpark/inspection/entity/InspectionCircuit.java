package com.crlink.smartpark.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 巡检路线
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ins_inspection_circuit")
public class InspectionCircuit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 园区Id
     */
    @TableField
    private String parkOrgId;

    /**
     * 巡检路线名
     */
    private String circuitName;

    /**
     * 巡检类型
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否有序
     */
    private Boolean orderly;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
