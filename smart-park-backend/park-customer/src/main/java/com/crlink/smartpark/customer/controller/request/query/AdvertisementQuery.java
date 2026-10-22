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
 * 广告
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Advertisement对象", description = "广告")
public class AdvertisementQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "广告名称")
    private String name;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "状态： 0 生效中，1 待生效，2 已过期")
    private Integer status;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeEnd;

}
