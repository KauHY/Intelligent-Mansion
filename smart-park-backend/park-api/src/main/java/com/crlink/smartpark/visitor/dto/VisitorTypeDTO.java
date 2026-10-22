package com.crlink.smartpark.visitor.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 访客类型
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "VisitorType对象", description = "访客类型")
public class VisitorTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "访客类型名称")
    private String visitorTypeName;

    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "入驻企业名")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "园区组织名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
