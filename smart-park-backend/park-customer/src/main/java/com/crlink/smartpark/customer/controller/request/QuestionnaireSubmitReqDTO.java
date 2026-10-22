package com.crlink.smartpark.customer.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionnaireSubmitReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问卷id", required = true)
    private String questionnaireId;

    @ApiModelProperty(value = "问卷提交结果", required = true)
    private String submission;


}