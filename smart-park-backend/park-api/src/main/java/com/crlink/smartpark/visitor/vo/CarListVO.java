package com.crlink.smartpark.visitor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 园区运营平台-车行管理-车辆管理ListVO
 * */
@Data
@ApiModel(value="车辆信息ListVO对象", description="车辆信息")
public class CarListVO implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "车辆编码")
    private String carCode;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String carNum;

    private String employeeId;

    /**
     *车主姓名
     */
    @ApiModelProperty(value = "车主姓名")
    private String carOwnerName;

    /**
     *电话号码
     */
    @ApiModelProperty(value = "电话号码")
    private String mobile;

    /**
     *停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    @ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
    private Integer parkType;

    /**
     *授权开始时间
     */
    @ApiModelProperty(value = "授权开始时间")
    private LocalDateTime authStartTime;

    /**
     *授权结束时间
     */
    @ApiModelProperty(value = "授权结束时间")
    private LocalDateTime authEndTime;

    /**
     *修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

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

    /**
     *状态：未生效；已生效；已失效
     */
    @ApiModelProperty(value = "车辆状态 0-未生效,1-已生效，2-已失效")
    private Integer status;

    @ApiModelProperty(value = "停车场ID")
    private Set<String> parkingLotIds;

    @ApiModelProperty(value = "停车场名称")
    private String parkingLots;

}
