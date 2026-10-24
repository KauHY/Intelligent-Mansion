package com.crlink.smartpark.business.dto;

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
@ApiModel(value = "BusinessInfo对象", description = "")
public class BusinessInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String parkOrgId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "招商时效类型,1:长期有效")
    private Integer effectType;

    private LocalDateTime deadline;

    @ApiModelProperty(value = "租售属性，1出租，2出售，3出租出售")
    private List<Integer> rentOrSale;

    //    @ApiModelProperty(value = "亮点描述")
//    private String highlight;
    @ApiModelProperty(value = "亮点描述:字典HIGHLIGHT ,0智慧办公,1限时入住,2配套齐全,3交通便利,4临近商圈")
    private List<Integer> highlight;


    @ApiModelProperty(value = "咨询顾问（员工）id")
    private String employeeId;

    @ApiModelProperty(value = "咨询电话")
    private String employeePhone;

    @ApiModelProperty(value = "标题图片")
    private String titlePicture;

    @ApiModelProperty(value = "内容详情")
    private String content;

    @ApiModelProperty(value = "地址")
    private AddressDTO address;

    @ApiModelProperty(value = "发布状态,0未上架，1招商中，2已下架")
    private Integer status;

    private LocalDateTime createTime;


}
