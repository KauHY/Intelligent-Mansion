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
 * 巡检路线巡检点关系表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ins_circuit_point_relation")
public class CircuitPointRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 巡检路线
     */
    private String circuitId;

    /**
     * 巡检点
     */
    private String pointId;

    /**
     * 排序
     */
    private Integer sort;

    private Boolean deleted;


}
