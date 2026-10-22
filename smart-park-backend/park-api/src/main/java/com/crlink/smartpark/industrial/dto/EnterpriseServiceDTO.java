package com.crlink.smartpark.industrial.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业服务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EnterpriseService响应对象", description="企业服务")
public class EnterpriseServiceDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "服务名")
    private String name;

    @ApiModelProperty(value = "推送范围")
    private List<String> pushRange;

    private String pushRangeName;

    @ApiModelProperty(value = "所在分组：0-服务指南；1-政策申报；2-其他")
    private Integer groupType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "banner")
    private String banner;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "咨询电话")
    private String tel;

    @ApiModelProperty(value = "附件")
    private String att;

    @ApiModelProperty(value = "状态：0-待发布；1-已发布；2-停用")
    private Integer state;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
