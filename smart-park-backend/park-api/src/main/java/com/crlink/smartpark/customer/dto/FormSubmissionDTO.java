package com.crlink.smartpark.customer.dto;

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
 * 表单提交内容
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FormSubmission响应对象", description = "表单提交内容")
public class FormSubmissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "信息统计表单id")
    private String formId;

    @ApiModelProperty(value = "信息统计表单提交结果")
    private String submission;

    @ApiModelProperty(value = "信息统计表单接收")
    private List<String> receivedEmployeeIds;

    @ApiModelProperty(value = "提交人")
    private String employeeId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
