package com.crlink.smartpark.customer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 表单提交内容
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FormSubmission请求对象", description="表单提交内容")
public class FormSubmissionReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "信息统计表单id", required = true)
    private String formId;

    @ApiModelProperty(value = "信息统计表单提交结果", required = true)
    private String submission;

    @ApiModelProperty(value = "信息统计表单接收", required = true)
    private String receivedEmployeeIds;

    @ApiModelProperty(value = "提交人", required = true)
    private String employeeId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
