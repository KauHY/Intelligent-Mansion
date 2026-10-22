package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 公寓人员指纹表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_employee_finger")
@ApiModel(value = "EmployeeFinger数据库对象", description = "员工指纹表")
public class EmployeeFinger implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

//    @ApiModelProperty(value = "公寓人员Id")
//    private String apartmentPersonId;

    @ApiModelProperty(value = "员工Id")
    private String employeeId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "指纹数据")
    private String fingerData;

    @ApiModelProperty(value = "指纹数据索引（用作区分多个指纹）")
    private Integer fingerIndex;

    @ApiModelProperty(value = "删除状态，0未删除，1已删除")
    private Boolean deleted;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
