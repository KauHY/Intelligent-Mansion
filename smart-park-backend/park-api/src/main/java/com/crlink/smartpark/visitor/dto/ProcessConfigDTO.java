package com.crlink.smartpark.visitor.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 访客流程配置
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProcessConfig对象", description = "访客流程配置")
public class ProcessConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "流程名")
    private String processName;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区组织名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

    @ApiModelProperty(value = "入驻企业名")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "流程类型，1：访客预约 2：访客邀约")
    private Integer processType;

    @ApiModelProperty(value = "访客类型ID")
    private String visitorTypeId;

    @ApiModelProperty(value = "访客类型名")
    @Id2objectField(code = Id2objCodeConstant.VISITOR_TYPE_NAME_CODE)
    private String visitorTypeName;

    @ApiModelProperty(value = "访客流程节点集合（英文逗号分隔），1：访客预约 2：员工审核 3：员工邀请 4：访客补充信息 5：前台核验")
    private String processNodes;

    @ApiModelProperty(value = "启用，0：否，1：是")
    private Integer enable;

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
