package com.crlink.smartpark.device.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 消防设备
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FireControlDevice请求对象", description = "消防设备")
public class FireControlDeviceReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id", required = true)
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "所属系统", required = true)
    @NotNull
    private Integer systemType;

    @ApiModelProperty(value = "设备名称", required = true)
    @Length(min = 1, max = 30)
    private String name;

    @ApiModelProperty(value = "SN号码", required = true)
    @Length(min = 1, max = 30)
    private String serialNumber;

    @ApiModelProperty(value = "设备id", required = true)
    @Length(min = 1, max = 30)
    private String deviceId;

    @ApiModelProperty(value = "ip地址", required = true)
    @Length(min = 1, max = 30)
    private String ip;

    @ApiModelProperty(value = "端口号", required = true)
    @NotNull
    private Integer port;

    @ApiModelProperty(value = "通信端口", required = true)
    @Length(min = 1, max = 30)
    private String communicationPort;

    @ApiModelProperty(value = "安装楼栋", required = true)
    @NotEmpty
    private String buildingId;

    @ApiModelProperty(value = "所属楼层", required = true)
    @NotNull
    private Integer floor;

    @ApiModelProperty(value = "房屋")
    private String houseId;

    @ApiModelProperty(value = "安装位置")
    @Length(max = 30)
    private String installationLocation;

}
