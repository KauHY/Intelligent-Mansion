package com.crlink.smartpark.customer.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EditorResultQuery extends BaseQuery {
    @ApiModelProperty("问卷id")
    @NotEmpty
    private String questionnaireId;
    @ApiModelProperty("题目id")
    @NotEmpty
    private String editorId;
    private String keyword;
}
