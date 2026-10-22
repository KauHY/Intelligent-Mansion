package com.crlink.smartpark.contract.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 合同模板管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Template响应对象", description = "合同模板管理")
public class TemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;
    @Id2objectField(code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer type;

    @ApiModelProperty(value = "word模板key")
    private String templateKey;

    @ApiModelProperty(value = "word模板名字")
    private String templateName;

    @ApiModelProperty(value = "生成的合同key")
    private String contractKey;

    @ApiModelProperty(value = "生成的合同时间")
    private LocalDateTime contractKeyTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建人名字")
    @Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    private String createByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
