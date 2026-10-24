package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 广告
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Advertisement对象", description = "广告")
public class AdvertisementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "状态： 0 生效中, 1 待生效，2 已过期")
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

    @ApiModelProperty(value = "推送范围")
    @Id2objectField(code = PARK_ORG_NAME_CODE)
    private List<DictVO> parkOrgNames;

    @ApiModelProperty(value = "发布人")
    @Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    private String createByName;


}
