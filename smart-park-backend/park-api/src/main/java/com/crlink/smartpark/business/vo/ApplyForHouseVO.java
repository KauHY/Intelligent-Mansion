package com.crlink.smartpark.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ApplyForHouseVO {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "招商标题")
    private String title;

    @ApiModelProperty(value = "用户姓名")
    private String username;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "咨询顾问（员工）id")
    private String employeeId;
    private String employeeName;

    @ApiModelProperty(value = "受理状态,1待受理，2已完成,3已撤回")
    private Integer status;

    @ApiModelProperty(value = "意向房源:房源Id")
    private String houseInfoId;
    private BusinessHouseVO businessHouseVO;

    @ApiModelProperty(value = "房屋功能：0办公、1商铺、2住宿、3仓库、4其他")
    private Integer houseFunction;

    @ApiModelProperty(value = "企业/个人名称：：取值当前用户已关联的企业，如果关联多个公司，默认按拼音取值第一个关联企业")
    private String companyName;

    @ApiModelProperty(value = "地点：默认意向房源所属楼栋")
    private String buildingId;

    @ApiModelProperty(value = "地点：默认意向房源所属楼栋")
    private String buildingName;

    @ApiModelProperty(value = "地点：默认意向房源所属楼栋")
    private String address;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;
}
