package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 房屋表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 楼层房号
     */
    private String name;

    /**
     * 房屋编码
     */
    private String code;

    /**
     * 所在楼层
     */
    private Integer floor;

    /**
     * 套内面积
     */
    private Double coverArea;

    /**
     * 建筑面积
     */
    private Double buildArea;

    /**
     * 楼栋ID
     */
    private String buildingId;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 楼层属性：平层，底层，中层，高层
     */
    private Integer floorType;

    /**
     * 房屋状态：自用，待租， 已租，代售，已售
     */
    private Integer houseStatus;

    /**
     * 房屋属性：0自用，1租赁，2销售
     */
    private Integer houseProperty;

    /**
     * 房屋功能；办公 商铺 公寓 仓库 其他
     */
    private Integer houseFunction;

    /**
     * 朝向
     */
    private Integer orientation;

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
