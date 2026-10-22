package com.crlink.smartpark.customer.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QuestionnaireDataVO implements Serializable {
    private String questionnaireId;
    @ApiModelProperty(value = "问卷标题")
    private String title;

    private String mobile;

    private LocalDateTime createTime;
    @ApiModelProperty(value = "提交结果id")
    private String questionnaireSubmissionId;
}
