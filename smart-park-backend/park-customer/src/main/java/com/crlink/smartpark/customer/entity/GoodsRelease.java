package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.crlink.smartpark.core.mphandler.MyJacksonTypeHandler;
import com.crlink.smartpark.customer.dto.Goods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cus_goods_release", autoResultMap = true)
@ApiModel(value = "GoodsRelease数据库对象", description = "")
public class GoodsRelease implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "编号")
    private String goodsNo;

    @ApiModelProperty(value = "申请人姓名")
    private String submitterName;

    @ApiModelProperty(value = "申请人手机号")
    private String submitterMobile;

    @ApiModelProperty(value = "放行码")
    private String releaseCode;

    @ApiModelProperty(value = "所在公司")
    private String company;

    @ApiModelProperty(value = "放行时间0")
    private LocalDateTime releaseDatetimeStart;

    @ApiModelProperty(value = "放行时间1")
    private LocalDateTime releaseDatetimeEnd;

    @ApiModelProperty(value = "放行物品")
    //@TableField(typeHandler = JacksonTypeHandler.class)
    @TableField(typeHandler = MyJacksonTypeHandler.class)
    private List<Goods> goodsList;

    @ApiModelProperty(value = "车牌号")
    private String carNumber;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审核人")
    private String employeeId;

    @ApiModelProperty(value = "审核意见")
    private String checkOpinion;

    @ApiModelProperty(value = "状态")
    private Integer status;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
