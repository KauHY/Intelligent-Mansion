package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车辆通行记录
 * * @date 2022-10-12
 */
@Data
@TableName("vis_car_access_record_rel")
@ApiModel(value="CarAccessRecordRel数据库对象", description="车辆通行记录")
public class CarAccessRecordRel implements Serializable {
    private static final long serialVersionUID = 8349677284645191481L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

}
