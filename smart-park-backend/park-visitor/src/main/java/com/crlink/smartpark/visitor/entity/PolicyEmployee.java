package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.crlink.smartpark.core.mphandler.SetTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 策略员工表
 * </p>
 * */
@Data
@TableName(value = "vis_policy_employee", autoResultMap = true)
@ApiModel(value="PolicyEmployee数据库对象", description="策略员工表")
public class PolicyEmployee implements Serializable {
    private static final long serialVersionUID = 5580986571152588159L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "通行策略ID")
    private String policyId;

    @ApiModelProperty(value = "企业ID")
    private String companyId;

    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "设备人员ID")
    private Long devicePersonId;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "设备ID集合(逗号隔开)")
    @TableField(typeHandler = SetTypeHandler.class)
    private Set<String> deviceIds;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

}
