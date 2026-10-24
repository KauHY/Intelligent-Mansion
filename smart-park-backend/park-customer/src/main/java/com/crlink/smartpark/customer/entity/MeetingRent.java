package com.crlink.smartpark.customer.entity;

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

/**
 * <p>
 * 会议室租赁管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_meeting_rent")
@ApiModel(value="MeetingRent数据库对象", description="会议室租赁管理")
public class MeetingRent implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属楼栋单元")
    private String buildingId;

    @ApiModelProperty(value = "楼层房号")
    private String houseId;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "咨询电话")
    private String telephone;

    @ApiModelProperty(value = "容纳人数")
    private Integer capacity;

    @ApiModelProperty(value = "会议室设备 1摄像机、2远程会议设备、3投影仪、4电子白板")
    private String meetingEquipment;

    @ApiModelProperty(value = "封面图片（单张）")
    private String coverImage;

    @ApiModelProperty(value = "详情图片（最多五张）")
    private String detailImage;

    @ApiModelProperty(value = "状态：1开启 0关闭")
    private Integer status;

    @ApiModelProperty(value = "会议室介绍")
    private String remark;

    @ApiModelProperty(value = "预定须知")
    private String reserveInstruct;

    @ApiModelProperty(value = "是否收费 1是 0否")
    private Integer charge;

    @ApiModelProperty(value = "租赁价格")
    private BigDecimal rentPrice;

    @ApiModelProperty(value = "物资id 逗号分割")
    private String goodsId;

    @ApiModelProperty(value = "使用权限设置 0不限 1指定公司/人员")
    private Integer usePermission;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;


}
