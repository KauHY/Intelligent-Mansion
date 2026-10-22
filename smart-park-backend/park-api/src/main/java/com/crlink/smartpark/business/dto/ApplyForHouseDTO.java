package com.crlink.smartpark.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 看房申请表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApplyForHouse对象", description = "看房申请")
public class ApplyForHouseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户姓名")
    private String username;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "咨询顾问（员工）id")
    private String employeeId;

    @ApiModelProperty(value = "受理状态,1待受理，2已完成,,3已撤回")
    private Integer status;

    @ApiModelProperty(value = "意向房源:房源招商Id")
    private String houseInfoId;

    @ApiModelProperty(value = "房屋功能：1办公、2商铺、3住宿、4仓库、5其他")
    private Integer houseFunction;

    @ApiModelProperty(value = "企业/个人名称：：取值当前用户已关联的企业，如果关联多个公司，默认按拼音取值第一个关联企业")
    private String companyName;

    @ApiModelProperty(value = "地点：默认意向房源所属楼栋")
    private String address;

    @ApiModelProperty(value = "删除状态，0未删除，1已删除")
    private Boolean deleted;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
