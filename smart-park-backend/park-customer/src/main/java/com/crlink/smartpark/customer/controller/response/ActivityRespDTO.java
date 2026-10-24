package com.crlink.smartpark.customer.controller.response;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.customer.dto.ActivityDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

@Data
public class ActivityRespDTO extends ActivityDTO {
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonIgnore
    @Id2objectField(code = USER_NAME_CODE, targetFieldName = "createByName")
    private String createBy;
    
    @ApiModelProperty(value = "发布人")
    private String createByName;

    @ApiModelProperty(value = "推送范围")
    private List<String> parkOrgNames;
}
