package com.crlink.smartpark.customer.entity;

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
 * 广告
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_advertisement")
@ApiModel(value="Advertisement对象", description="广告")
public class Advertisement implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "广告名称")
    private String name;

    @ApiModelProperty(value = "排序：数字越小越靠前")
    private Integer sort;

    @ApiModelProperty(value = " 广告位置 ：1 首页轮播图")
    private Integer position;

    @ApiModelProperty(value = "图片")
    private String picture;

    @ApiModelProperty(value = "跳转类型：0 无，1 URL，2 小程序AppID")
    private Integer jumpType;

    @ApiModelProperty(value = "跳转地址/需跳转的小程序AppID")
    private String jumpContent;

    @ApiModelProperty(value = "状态： 0 待生效，1生效中，2 已过期")
    private Integer status;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeEnd;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
