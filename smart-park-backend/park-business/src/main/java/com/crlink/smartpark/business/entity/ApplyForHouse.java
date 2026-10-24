package com.crlink.smartpark.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 看房申请表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_apply_for_house")
public class ApplyForHouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 园区id
     */
    private String parkOrgId;

    /**
     * 标题
     */
    private String title;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 咨询顾问（员工）id
     */
    private String employeeId;

    /**
     * 受理状态,1待受理，2已完成,3已撤回
     */
    private Integer status;

    /**
     * 意向房源:招商房屋Id
     */
    private String houseInfoId;

    /**
     * 房屋功能：1办公、2商铺、3住宿、4仓库、5其他
     */
    private Integer houseFunction;

    /**
     * 企业/个人名称：：取值当前用户已关联的企业，如果关联多个公司，默认按拼音取值第一个关联企业
     */
    private String companyName;

    /**
     * 地点：默认意向房源所属楼栋
     */
    private String buildingId;

    /**
     * 删除状态，0未删除，1已删除
     */
    private Boolean deleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
