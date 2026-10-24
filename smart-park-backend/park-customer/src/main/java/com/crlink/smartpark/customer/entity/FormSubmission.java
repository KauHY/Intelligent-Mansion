package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "cus_form_submission", autoResultMap = true)
@ApiModel(value = "FormSubmission数据库对象", description = "表单提交内容")
public class FormSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "信息统计表单id")
    private String formId;

    @ApiModelProperty(value = "信息统计表单提交结果")
    @TableField(typeHandler = JacksonTypeHandler.class)
//    private Map<String, Object> submission;
    private Object submission;

    @ApiModelProperty(value = "信息统计表单接收")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> receivedEmployeeIds;

    @ApiModelProperty(value = "提交人")
    private String employeeId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
