package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备台账
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cus_equipment_ledger", autoResultMap = true)
@ApiModel(value="EquipmentLedger数据库对象", description="设备台账")
public class EquipmentLedger implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "设备名，同一园区下不重复")
    private String name;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "楼栋id")
    private String buildingId;

    @ApiModelProperty(value = "楼层数")
    private String floor;

    @ApiModelProperty(value = "详细位置")
    private String installationLocation;

    @ApiModelProperty(value = "管理员")
    private String adminId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "图片,逗号隔开")
    private String pic;

    @ApiModelProperty(value = "规格型号")
    private String specifications;

    @ApiModelProperty(value = "安装日期")
    private LocalDate installationDate;

    @ApiModelProperty(value = "原厂编号")
    private String originalNumber;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "维保单位")
    private String manufacturerUnit;

    @ApiModelProperty(value = "维保电话")
    private String manufacturerMobile;

    @ApiModelProperty(value = "状态：0-已报废；1-正常")
    private Integer state;

    @ApiModelProperty(value = "报废日期")
    private LocalDate scrapDate;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除：0否1是")
    private Integer deleted;


}
