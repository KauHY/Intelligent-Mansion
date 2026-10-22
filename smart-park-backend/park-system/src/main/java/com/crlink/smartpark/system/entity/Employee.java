package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.crlink.smartpark.system.dto.employee.PersonFingerData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 入住企业员工信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sys_employee", autoResultMap = true)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 照片（英文逗号分隔）
     */
    private String pics;

    /**
     * 所属入驻企业ID
     */
    private String companyId;

    @TableField(exist = false)
    private String companyName;

    /**
     * 所属园区组织ID
     */
    private String parkOrgId;

    /**
     * 2022.03.23 v12 新增字段
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 员工标签
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tagIds;

    /**
     * 状态，0：禁用 1：启用
     */
    private Integer status;

    /**
     * 人员卡号（用于大华设备推送）
     */
    private String cardNo;

    /**
     * 设备人员ID
     */
    private Long devicePersonId;

    /**
     * 设备人员ID
     */
    @TableField(exist = false)
    private List<PersonFingerData> fingers;

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


    @TableField(exist = false)
    private String uploadPic;
}
