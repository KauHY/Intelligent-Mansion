package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.BUILDING_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 消防设备
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FireControlDevice响应对象", description = "消防设备")
public class FireControlDeviceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "所属系统")
    private Integer systemType;

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "SN号码")
    private String serialNumber;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "端口号")
    private Integer port;

    @ApiModelProperty(value = "通信端口")
    private String communicationPort;

    @ApiModelProperty(value = "安装楼栋")
    private String buildingId;
    @Id2objectField(idField = "buildingId", code = BUILDING_NAME_CODE)
    private String buildingName;

    @ApiModelProperty(value = "所属楼层")
    private Integer floor;

    @ApiModelProperty(value = "房屋")
    private String houseId;

    @ApiModelProperty(value = "安装位置")
    private String installationLocation;

    @ApiModelProperty(value = "状态")
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
