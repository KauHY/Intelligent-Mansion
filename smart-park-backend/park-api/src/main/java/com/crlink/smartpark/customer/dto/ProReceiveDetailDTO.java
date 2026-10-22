package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.EMPLOYEE_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 物品领用流程
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProReceiveFlow响应对象", description = "物品领用流程")
public class ProReceiveDetailDTO implements Serializable {

    @ApiModelProperty(value = "审核人")
    private String auditUserId;

    @ApiModelProperty(value = "审核人")
    @Id2objectField(code = USER_NAME_CODE, idField = "auditUserId")
    private String auditUserName;

    @ApiModelProperty(value = "审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消")
    private Integer auditState;

    @ApiModelProperty(value = "审核意见")
    private String auditMsg;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "出入库单号")
    private String stockNo;

    @ApiModelProperty(value = "是否需归还")
    private Integer needReturn;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "申请备注")
    private String applyRemark;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "本次归还数量")
    private Integer returnNumCurrent;


}
