package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_TIME_FORMAT;

@Data
public class BusinessChanceQuery extends BaseQuery {
    @ApiModelProperty("组织ID")
    private String OrgId;
    @ApiModelProperty("关键字")
    private String keyword;
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime followStartTime;
    @ApiModelProperty("跟进时间")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime followEndTime;
    @ApiModelProperty("商机来源:1商机录入，2入驻申请")
    private Integer businessSource;

    @ApiModelProperty("商机阶段")
    private Integer chanceStage;
}
