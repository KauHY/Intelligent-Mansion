package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 车辆通行记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CarAccessRecordRel响应对象", description="车辆通行记录")
public class CarAccessRecordRelDTO implements Serializable {

    private static final long serialVersionUID = -7247097921490784215L;

    @ApiModelProperty(value = "车牌号")
    private String carNum;

    @ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
    private Integer parkType;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "停车场ID")
    private String parkingLotId;

    @ApiModelProperty(value = "停车场名")
    private String parkingLot;

    @ApiModelProperty(value = "驶入抓拍照片")
    private String enterImgUrl;

    @ApiModelProperty(value = "驶入车道ID")
    private String enterCarLaneId;

    @ApiModelProperty(value = "驶入车道名")
    private String enterCarLaneName;

    @ApiModelProperty(value = "驶入时间")
    private LocalDateTime enterRecognizeTime;

    @ApiModelProperty(value = "驶出抓拍照片")
    private String exitImgUrl;

    @ApiModelProperty(value = "驶出车道ID")
    private String exitCarLaneId;

    @ApiModelProperty(value = "驶出车道名")
    private String exitCarLaneName;

    @ApiModelProperty(value = "驶出时间")
    private LocalDateTime exitRecognizeTime;

    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;


}
