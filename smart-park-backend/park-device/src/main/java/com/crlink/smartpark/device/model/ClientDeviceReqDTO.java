package com.crlink.smartpark.device.model;

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
import java.util.List;

/**
 * <p>
 * 设备管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ClientDevice请求对象", description="设备管理")
public class ClientDeviceReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id", required = true)
      private String id;

    @ApiModelProperty(value = "设备名称", required = true)
    private String deviceName;

    @ApiModelProperty(value = "设备序列号", required = true)
    private String deviceSn;

    @ApiModelProperty(value = "网关主键id", required = true)
    private String gatewaySn;

    @ApiModelProperty(value = "设备类型", required = true)
    private Integer deviceType;

    @ApiModelProperty(value = "设备位置", required = true)
    private String address;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    @ApiModelProperty(value = "状态；1在线 0离线", required = true)
    private Integer status;

    @ApiModelProperty(value = "区域id", required = true)
    private Long areaId;

    @ApiModelProperty(value = "设备厂家编码	", required = true)
    private Integer factoryCode;

    @ApiModelProperty(value = "设备子类型", required = true)
    private Integer factorySubtype;

    @ApiModelProperty(value = "位置（设备属性）", required = true)
    private String roomId;

    @ApiModelProperty(value = "认证状态 0未认证 1已认证 2失效", required = true)
    private Integer authState;

    @ApiModelProperty(value = "租户id", required = true)
    private String tenantId;

    @ApiModelProperty(value = "0 否，1 是", required = true)
    private Integer deleted;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;


}
