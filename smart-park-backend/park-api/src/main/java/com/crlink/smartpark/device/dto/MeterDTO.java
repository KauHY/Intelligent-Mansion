package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.*;

/**
 * <p>
 * 仪表（电表和水表）
 * </p>
 *
  */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Meter响应对象", description = "仪表（电表和水表）")
public class MeterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    //@ApiModelProperty(value = "仪表编号")
    //private String meterNo;

    @ApiModelProperty(value = "能耗类别（6电能耗，5水能耗）")
    private Integer deviceType;

    @ApiModelProperty(value = "表计属性")
    private Integer effectProperty;

    @ApiModelProperty(value = "表计类别")
    private Integer effectType;

    @ApiModelProperty(value = "表类型")
    private Integer meterType;

    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "表计号码")
    private String serialNumber;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    //@ApiModelProperty(value = "ip地址")
    //private String ip;
    //
    //@ApiModelProperty(value = "端口号")
    //private Integer port;
    //
    //@ApiModelProperty(value = "通信端口")
    //private String communicationPort;

    @ApiModelProperty(value = "最大量程")
    private Integer maxRange;

    @ApiModelProperty(value = "安装位置")
    private String location;


    @ApiModelProperty(value = "楼栋")
    @Id2objectField(code = BUILDING_NAME_CODE, targetFieldName = "buildingName")
    private String buildingId;
    @ApiModelProperty(value = "楼栋")
    private String buildingName;

    @ApiModelProperty(value = "所属楼层多选")
    private List<Integer> floors;

    @ApiModelProperty(value = "所属房屋多选")
    @Id2objectField(code = HOUSE_NAME_CODE, targetFieldName = "houseNames")
    private List<String> houseIds;
    private List<String> houseNames;

    @ApiModelProperty(value = "初始读数")
    private BigDecimal initialReading;

    @ApiModelProperty(value = "初始时间")
    private LocalDateTime initialTime;

    @ApiModelProperty(value = "在线状态")
    private Integer status;

    @ApiModelProperty(value = "使用状态")
    private Integer usingStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

}
