package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 报事投诉、报修服务表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AssignOrder对象", description = "报事投诉、报修服务分配人员")
public class AssignOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单Id")
    @NotBlank(message = "工单Id不能为空")
    private String orderId;

    @ApiModelProperty(value = "报事报修工单编号")
    private String orderNo;

    @ApiModelProperty(value = "要求完成时间")
    @NotNull(message = "要求完成时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate askFinishTime;

    @ApiModelProperty(value = "处理人")
    @NotBlank(message = "处理人不能为空")
    private String handleUserId;

    @ApiModelProperty(value = "处理人姓名")
    private String handleUserName;

    @ApiModelProperty(value = "物业公司Id")
    @NotBlank(message = "物业公司Id不能为空")
    private String companyId;
}
