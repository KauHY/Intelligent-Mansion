package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dev_client_device")
@ApiModel(value="ClientDevice数据库对象", description="设备管理")
public class ClientDevice implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备序列号")
    private String deviceSn;

    @ApiModelProperty(value = "网关主键id")
    private String gatewaySn;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(value = "设备位置")
    private String address;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态；1在线 0离线")
    private Integer status;

    @ApiModelProperty(value = "区域id")
    private Long areaId;

    @ApiModelProperty(value = "设备厂家编码	")
    private Integer factoryCode;

    @ApiModelProperty(value = "设备子类型")
    private Integer factorySubtype;

    @ApiModelProperty(value = "位置（设备属性）")
    private String roomId;

    @ApiModelProperty(value = "认证状态 0未认证 1已认证 2失效")
    private Integer authState;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "0 否，1 是")
    private Integer deleted;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
