package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 表单模板
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FormTemplate请求对象", description = "表单模板")
public class FormTemplateReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "模板标题,模板名称", required = true)
    private String title;

    //@ApiModelProperty(value = "编辑器ids", required = true)
    //private String editorIds;

    //@ApiModelProperty(value = "状态", required = true)
    //private Integer status;
    //
    //private String createBy;
    //
    //private LocalDateTime createTime;
    //
    //private String updateBy;
    //
    //private LocalDateTime updateTime;
    //
    //private Boolean deleted;


}
