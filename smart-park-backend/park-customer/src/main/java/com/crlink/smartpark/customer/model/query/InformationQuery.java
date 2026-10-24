package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.Constants.TIME_PATTERN;

/**
 * <p>
 * 公告、资讯信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InformationQuery对象", description = "公告、资讯信息表")
public class InformationQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "资讯类型：INFOMATION_TYPE")
    private Integer type;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "投放的小程序类型:1 C端，2 运营端")
    private Integer putIntoType;

    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime effectTimeStart;
    private LocalDateTime effectTimeEnd;

    @ApiModelProperty(value = "发布状态，0待生效，1已生效，2已过期")
    private Integer effectStatus;


}
