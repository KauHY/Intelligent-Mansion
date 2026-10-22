package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 园区信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_park")
public class Park implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 园区组织ID
     */
    private String parkOrgId;

    /**
     * 地址
     */
    private String address;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 建成时间
     */
    private LocalDate buildTime;

    /**
     * 园区属性 1：社会大型园区 2：政府园区 3：社区集体园区 4：区属国企物业 5：社会零散物业
     */
    private Integer parkType;

    /**
     * 功能用途 1：研发办公 2：工业生产 3：物流仓储 4：商业配套 5：其他
     */
    private Integer parkFunction;

    /**
     * 产权属性 1：无 2：红本 3：绿本 4：其他 
     */
    private Integer property;

    /**
     * 权属单位
     */
    private String companyName;

    /**
     * 权属单位联系人
     */
    private String companyContacts;

    /**
     * 权属单位联系电话
     */
    private String companyTel;

    /**
     * 管理机构
     */
    private String managementName;

    /**
     * 管理机构联系人
     */
    private String managementContacts;

    /**
     * 管理机构联系电话
     */
    private String managementTel;

    /**
     * 占地面积
     */
    private Double coverArea;

    /**
     * 建筑面积
     */
    private Double buildingArea;

    /**
     * 消防审批状态 0：已通过 1：未通过 2：暂未审批
     */
    private Integer fireExamineStatus;

    /**
     * 消防责任管理人
     */
    private String firePerson;

    /**
     * 消防责任管理人联系电话
     */
    private String firePersonTel;

    /**
     * 图片
     */
    private String pics;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 删除状态
     */
    private Boolean deleted;


}
