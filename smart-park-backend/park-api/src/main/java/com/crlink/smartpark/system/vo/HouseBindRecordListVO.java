package com.crlink.smartpark.system.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HouseBindRecordListVO extends HouseBindListVO {

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "解绑时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDateTime unbindTime;

    @ApiModelProperty(value = "绑定状态：true：已绑定 false：已解绑")
    private Boolean bind;

}
