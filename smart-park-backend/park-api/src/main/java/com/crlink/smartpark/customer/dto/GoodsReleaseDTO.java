package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GoodsRelease响应对象", description = "")
public class GoodsReleaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区")
    @JsonIgnore
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;


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

//    @ApiModelProperty(value = "放行时间0")
//    private LocalDateTime releaseDatetimeStart;
//
//    @ApiModelProperty(value = "放行时间1")
//    private LocalDateTime releaseDatetimeEnd;

    @ApiModelProperty(value = "放行时间0")
    private String releaseDatetimeStart;

    @ApiModelProperty(value = "放行时间1")
    private String releaseDatetimeEnd;

    @ApiModelProperty(value = "放行物品")
    private List<Goods> goodsList;

    @ApiModelProperty(value = "车牌号")
    private String carNumber;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审核意见")
    private String checkOpinion;

    @ApiModelProperty(value = "状态")
    private Integer status;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    @Id2objectField(code = USER_NAME_CODE, idField = "updateBy")
    private String updateByName;

    @ApiModelProperty(value = "是否自己申请")
    private Boolean selfCreate;


}
