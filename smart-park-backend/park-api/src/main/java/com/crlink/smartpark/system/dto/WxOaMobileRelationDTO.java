package com.crlink.smartpark.system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 公众号用户openId与手机号关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WxOaMobileRelation响应对象", description="公众号用户openId与手机号关联表")
public class WxOaMobileRelationDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "公众号用户openId")
    private String openId;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "公众号appId")
    private String appId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
