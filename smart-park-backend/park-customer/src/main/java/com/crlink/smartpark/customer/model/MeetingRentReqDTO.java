package com.crlink.smartpark.customer.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 会议室租赁管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingRent请求对象", description = "会议室租赁管理")
public class MeetingRentReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "所属园区", required = true)
    @NotBlank
    private String parkOrgId;

    @ApiModelProperty(value = "所属楼栋单元", required = true)
    @NotBlank
    private String buildingId;

    @ApiModelProperty(value = "楼层房号", required = true)
    @NotBlank
    private String houseId;

    @ApiModelProperty(value = "会议室名称", required = true)
    @NotBlank
    private String meetingName;

    @ApiModelProperty(value = "咨询电话", required = true)
    @NotBlank
    private String telephone;

    @ApiModelProperty(value = "容纳人数", required = true)
    @NotNull
    private Integer capacity;

    @ApiModelProperty(value = "会议室设备 1摄像机、2远程会议设备、3投影仪、4电子白板", required = true)
    private String meetingEquipment;

    @ApiModelProperty(value = "封面图片（单张）", required = true)
    @NotBlank
    private String coverImage;

    @ApiModelProperty(value = "详情图片（最多五张）", required = true)
    @NotBlank
    private String detailImage;

    @ApiModelProperty(value = "状态：1开启 0关闭", required = true)
    private Integer status;

    @ApiModelProperty(value = "会议室介绍", required = true)
    @NotBlank
    private String remark;

    @ApiModelProperty(value = "预定须知", required = true)
    @NotBlank
    private String reserveInstruct;

    @ApiModelProperty(value = "是否收费 1收费 0免费", required = true)
    @NotNull
    private Integer charge;

    @ApiModelProperty(value = "租赁价格")
    private BigDecimal rentPrice;

    @ApiModelProperty(value = "物资id 逗号分割")
    private String goodsId;

    @ApiModelProperty(value = "使用权限设置 0不限 1指定公司/人员", required = true)
    @NotNull
    private Integer usePermission;

    @ApiModelProperty(value = "指定公司集合")
    private List<MeetingPermissionReqDTO> companyIds;

    @ApiModelProperty(value = "指定人员集合")
    private List<MeetingPermissionReqDTO> employeeIds;

    @ApiModelProperty(value = "可预订时段集合")
    private List<MeetingScheduleTimeReqDTO> timeReqDTOList;



}
