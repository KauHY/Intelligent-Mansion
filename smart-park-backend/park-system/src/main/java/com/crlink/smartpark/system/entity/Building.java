package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 楼栋表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_building")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 楼栋名称
     */
    private String name;

    /**
     * 楼栋编码
     */
    private String code;

    /**
     * 总层数
     */
    private Integer totalFloor;

    /**
     * 建成时间
     */
    private LocalDate buildTime;

    /**
     * 电梯：0：无，1：有
     */
    private Integer elevator;

    /**
     * 占地面积
     */
    private Double coverArea;

    /**
     * 建筑面积
     */
    private Double buildArea;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 属性: 字典 BUILDING_PROPERTY 1办公、2食堂、3公寓、4其他
     */
    private Integer property;

    /**
     * 备注
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除状态
     */
    private Boolean deleted;


}
