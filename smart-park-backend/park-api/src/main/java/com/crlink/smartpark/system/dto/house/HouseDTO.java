package com.crlink.smartpark.system.dto.house;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class HouseDTO implements Serializable {
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
     * 楼层属性：平层，底层，中层，高层
     */
    private Integer floorType;

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

    private String companyName;


}
