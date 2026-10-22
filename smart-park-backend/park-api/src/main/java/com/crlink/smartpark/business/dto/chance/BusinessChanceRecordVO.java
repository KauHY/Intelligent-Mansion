package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class BusinessChanceRecordVO {

    private String id;

    private String createBy;
    @ApiModelProperty(value = "创建人")
    @Id2objectField(idField = "createBy", code = Id2objCodeConstant.USER_NAME_CODE)
    private String createByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "记录类型：1创建、2分配、3领取、4跟进、5提醒、6转成客户、7转移、8释放、9删除")
    private Integer recordType;

    @ApiModelProperty(value = "记录名称")
    private String recordTypeName;
}
