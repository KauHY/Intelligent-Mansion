package com.crlink.smartpark.customer.model;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 公告信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Notice请求对象", description = "公告信息表")
public class NoticeReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    @Length(max = 100, message = "标题不能超过100字")
    private String title;

    @ApiModelProperty(value = "公告类型：字典 NOTICE_TYPE", required = true)
    @NotNull(message = "公告类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "园区组织Id", required = true)
    @NotBlank(message = "园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "推送范围是否不限：0 指定公司/人员, 1 不限", required = true)
    @NotNull(message = "推送范围不能为空")
    private Boolean withoutLimit;

    @ApiModelProperty(value = "指定公司集合")
    private List<String> companyIds;

    @ApiModelProperty(value = "指定人员集合")
    private List<String> employeeIds;

    @ApiModelProperty(value = "投放类型：1 C端，2 运营端", required = true)
    @NotEmpty
    @Size(min = 1, max = 2)
    private List<Integer> putIntoType;

    @ApiModelProperty(value = "是否弹窗提醒")
    private Boolean popuped;

    @ApiModelProperty(value = "生效开始时间", required = true)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "生效开始时间", required = true)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime effectTimeEnd;

    @ApiModelProperty(value = "摘要", required = true)
    @NotBlank(message = "摘要不能为空")
    @Length(max = 100, message = "摘要不能超过100字")
    private String summary;

    @ApiModelProperty(value = "公告内容", required = true)
//    @Length(max = 3000, message = "公告内容不能超过3000字")
    @NotBlank(message = "公告内容不能为空")
    private String content;

    @ApiModelProperty(value = "附件")
    @Length(max = 600)
    private String files;
}
