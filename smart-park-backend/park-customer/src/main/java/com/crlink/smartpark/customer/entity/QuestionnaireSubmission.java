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

/**
 * <p>
 * 问卷提交内容
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cus_questionnaire_submission", autoResultMap = true)
@ApiModel(value = "QuestionnaireSubmission数据库对象", description = "问卷提交内容")
public class QuestionnaireSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "问卷id")
    private String questionnaireId;

    @ApiModelProperty(value = "问卷提交结果")
    @TableField(typeHandler = JacksonTypeHandler.class)
//    private Map<String, Object> submission;
    private Object submission;

    @ApiModelProperty(value = "提交人")
    private String employeeId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
