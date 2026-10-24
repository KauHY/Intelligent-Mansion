package com.crlink.smartpark.customer.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 问卷
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuestionnaireVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")

    private String parkOrgId;
    @Id2objectField(idField = "parkOrgId", code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "问卷标题")
    private String title;

    @ApiModelProperty(value = "答卷数")
    private Integer answeredCount;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadlineTime;

    @ApiModelProperty(value = "状态，1设计中，2收集中，3已结束")
    private Integer status;

    private String createBy;
    @Id2objectField(idField = "createBy", code = Id2objCodeConstant.USER_NAME_CODE)
    private String createByName;

    private LocalDateTime createTime;

}
