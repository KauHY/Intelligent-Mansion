package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_TIME_FORMAT;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ApplyForHouseQuery extends BaseQuery {

    @ApiModelProperty(value = "受理状态,1待受理，2已完成,3已撤回")
    private Integer status;

    @ApiModelProperty(value = "关键字：包括招商标题、用户姓名、咨询顾问")
    @Length(max = 10)
    private String keyword;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createStartTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createEndTime;

    private String orgId;

    @JsonIgnore
    private Set<String> parkOrgIds;

    @JsonIgnore
    private Set<String> employeeIds;

}
