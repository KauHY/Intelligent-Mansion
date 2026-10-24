package com.crlink.smartpark.business.dto.chance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusinessChanceDetailVO extends BusinessChanceVO {


    @ApiModelProperty(value = "跟进记录")
    private List<FollowBusinessChanceVO> followRecord;

    @ApiModelProperty(value = "阶段流程")
    private List<BusinessChanceRecordVO> recordVOList;
}
