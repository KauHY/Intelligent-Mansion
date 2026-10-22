package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.crlink.smartpark.core.mphandler.SetTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 公寓人员管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "vis_apartment_person", autoResultMap = true)
@ApiModel(value = "ApartmentPerson数据库对象", description = "公寓人员管理")
public class ApartmentPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;

    @ApiModelProperty(value = "所属企业Id")
    private String companyId;

    @ApiModelProperty(value = "关联员工Id")
    private String employeeId;

    @ApiModelProperty(value = "身份证号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String idCardNumber;

//    @ApiModelProperty(value = "关联房屋id集合")
//    @TableField(typeHandler = JacksonTypeHandler.class)
//    private List<String> houseIds;

    @ApiModelProperty(value = "公寓门禁设备id集合")
    @TableField(typeHandler = SetTypeHandler.class)
    private Set<String> deviceIds;

    @ApiModelProperty(value = "生效开始日期")
    private LocalDate effectStartDate;

    @ApiModelProperty(value = "生效结束日期")
    private LocalDate effectEndDate;

    @ApiModelProperty(value = "备注")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    private Integer status;

    @ApiModelProperty(value = "已过期，0：否 1：是")
    private Integer expire;

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
