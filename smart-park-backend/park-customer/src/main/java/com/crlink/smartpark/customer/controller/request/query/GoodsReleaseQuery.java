package com.crlink.smartpark.customer.controller.request.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GoodsRelease查询对象", description = "")
public class GoodsReleaseQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "申请人姓名")
    private String submitterName;

    @ApiModelProperty(value = "申请人手机号")
    private String submitterMobile;

    @ApiModelProperty(value = "申请时间0")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "申请时间1")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "状态")
    private Integer status;


}
