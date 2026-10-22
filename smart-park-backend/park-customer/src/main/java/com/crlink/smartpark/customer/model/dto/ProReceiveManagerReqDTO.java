package com.crlink.smartpark.customer.model.dto;

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
import java.util.List;

/**
 * <p>
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceiveManager请求对象", description="物品领用管理")
public class ProReceiveManagerReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id", required = true)
      private String id;

    @ApiModelProperty(value = "物品id", required = true)
    private String goodsId;

    @ApiModelProperty(value = "所属园区", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "申请单号", required = true)
    private String applyNumber;

    @ApiModelProperty(value = "所在公司", required = true)
    private String companyId;

    @ApiModelProperty(value = "领用人", required = true)
    private String employeeId;

    @ApiModelProperty(value = "领用数量", required = true)
    private String receiveNum;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    @ApiModelProperty(value = "是否需要归还 1是 0否", required = true)
    private Integer needReturn;

    @ApiModelProperty(value = "已归还数量", required = true)
    private Integer returnNumTotal;

    @ApiModelProperty(value = "申请时间", required = true)
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否", required = true)
    private Boolean deleted;


}
