package com.crlink.smartpark.visitor.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 来访须知
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Notice响应对象", description = "来访须知")
public class NoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名", required = true)
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "须知名称")
    private String noticeName;

    @ApiModelProperty(value = "须知内容")
    private String noticeContent;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

}
