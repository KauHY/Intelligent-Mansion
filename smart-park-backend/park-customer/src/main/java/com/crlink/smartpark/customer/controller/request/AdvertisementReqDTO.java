package com.crlink.smartpark.customer.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 广告
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Advertisement对象", description = "广告")
public class AdvertisementReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "广告名称", required = true)
    @Length(max = 30, message = "名称不能超过30字")
    private String name;

    @ApiModelProperty(value = "推送范围", required = true)
    private List<String> parkOrgIds;

    @ApiModelProperty(value = "排序：数字越小越靠前,最多2位数字", required = true)
    private Integer sort;

    @ApiModelProperty(value = " 广告位置 ：1 首页轮播图")
    private Integer position;

    @ApiModelProperty(value = "图片", required = true)
    private String picture;

    @ApiModelProperty(value = "跳转类型：0 无，1 URL，2 小程序AppID")
    private Integer jumpType;

    @ApiModelProperty(value = "跳转地址/需跳转的小程序AppID")
    private String jumpContent;

    @ApiModelProperty(value = "生效开始时间", required = true)
    @NotNull(message = "生效开始时间不能为空！")
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "生效结束时间", required = true)
    @NotNull(message = "生效结束时间不能为空！")
    private LocalDateTime effectTimeEnd;

}
