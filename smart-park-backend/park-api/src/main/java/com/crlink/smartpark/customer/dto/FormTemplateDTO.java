package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
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
 * 表单模板
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FormTemplate响应对象", description = "表单模板")
public class FormTemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "模板标题,模板名称")
    private String title;

    private String description;

    @ApiModelProperty(value = "编辑器")
    private List<EditorDTO> editors;

    private LocalDateTime updateTime;


}
