package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 信息统计表单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Form查询对象", description = "信息统计表单")
public class FormQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "表单名称")
    private String title;

    //@ApiModelProperty(value = "收集对象,公司")
    //private Set<String> companyIds;
    //
    //@ApiModelProperty(value = "收集对象,员工")
    //private String employeeIds;
    //
    //@ApiModelProperty(value = "周期，1每日、2每周、3每月、4自定义")
    //private Integer cycle;
    //
    //@ApiModelProperty(value = "截止时间")
    //private String deadlineTime;
    //
    //@ApiModelProperty(value = "截止时间的周几或第几天")
    //private Integer deadlineNum;
    //
    //@ApiModelProperty(value = "是否提醒，0不提醒，1提醒")
    //private Boolean reminded;
    //
    //@ApiModelProperty(value = "模板id")
    //private String templateId;
    //
    //@ApiModelProperty(value = "状态，1设计中，2收集中，3已结束")
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
