package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.device.vo.DeviceChannelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 设备信息保存DTO
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DeviceInfo新增对象", description = "设备信息")
public class DeviceInfoReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", required = true)
    private String id;

    @ApiModelProperty(value = "园区ID", required = true)
    @NotBlank(message = "关联园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "设备名称", required = true)
    @NotBlank(message = "设备名称不能为空")
    @Length(max = 30, message = "设备名称不允许超过30个字符")
    private String deviceName;

    @ApiModelProperty(value = "设备ID", required = true)
    @NotBlank(message = "设备ID不能为空")
    @Length(max = 30, message = "设备ID不允许超过30个字符")
    private String deviceId;

    @ApiModelProperty("设备类型：1-公共门禁；2-公寓门禁；3-道闸一体机；4-视频监控；5-水表；6-电表；7-会议信息屏；8-消防设备-烟感设备 9消防设备-液位设备 10 智能锁")
    @NotNull(message = "设备类型不能为空")
    private Integer deviceType;


    @ApiModelProperty(value = "IP地址")
    @Length(max = 30, message = "IP地址不允许超过30个字符")
    private String ipAddress;

    @ApiModelProperty(value = "拓展字段")
    private Map<String, Object> extension;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200个字符")
    private String remark;

    @ApiModelProperty(value = "租户ID", required = true)
    private String tenantId;

    @ApiModelProperty(value = "设备位置")
    private String location;

    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "所在楼层")
    private Integer floor;

//    @ApiModelProperty(value = "房屋ID")
//    private String houseId;

//    @ApiModelProperty(value = "关联房屋:key 通道编码（锁），value 房屋id")
//    private Map<String, String> houseMap;

    @ApiModelProperty(value = "智能锁通道数据")
    private List<DeviceChannelVO> lockChanelList;

}
