package com.crlink.smartpark.visitor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 园区运营平台-车行管理-车辆管理VO
 * */
@Data
@ApiModel(value="车辆信息VO对象", description="")
public class CarVO implements Serializable {
    private static final long serialVersionUID = -6234632850850960710L;

    private String id;

    private String carCode;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String carNum;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    private Integer parkType;

    /**
     * 车辆品牌:0未识别
     */
    private Integer carBrand;
    /**
     * 车辆类型:0未识别
     */
    private Integer carType;

    /**
     * 授权开始时间
     */
    @ApiModelProperty(value = "授权开始时间")
    private LocalDateTime authStartTime;

    /**
     * 授权结束时间
     */
    @ApiModelProperty(value = "授权结束时间")
    private LocalDateTime authEndTime;

    /**
     * 园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private String companyId;

    /**
     *园区名
     */
    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    /**
     *企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "车主员工ID")
    private String employeeId;

    /**
     * 车主姓名
     */
    @ApiModelProperty(value = "车主姓名")
    private String carOwnerName;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "车主手机号")
    private String mobile;

    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private Set<String> parkingLotIds;

    @ApiModelProperty(value = "停车场名称")
    private String parkingLots;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态：0-未生效；1-已生效；2-已失效
     */
    @ApiModelProperty(value = "状态：0-未生效；1-已生效；2-已失效")
    private Integer status;

}
