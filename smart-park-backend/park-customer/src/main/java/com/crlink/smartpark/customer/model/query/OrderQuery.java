package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 报事投诉、报修服务表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Order对象", description = "报事投诉、报修服务表")
public class OrderQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报事报修工单编号")
    private String orderNo;

    @ApiModelProperty(value = "园区编码")
    private String parkOrgId;

    @ApiModelProperty(value = "报事报修类型：1-报事投诉、2-报修服务", required = true)
    @Min(value = 1, message = "参数错误")
    @Max(value = 2, message = "参数错误")
    @NotNull(message = "报事报修类型不能为空")
    private Integer orderType;

    @ApiModelProperty(value = "报事投诉类型：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务、20-其他	报修服务类型：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备、26-其他")
    private Integer orderTypeItem;

    @ApiModelProperty(value = "报事报修内容/反馈人/手机号")
    private String keyword;

    @ApiModelProperty(value = "处理状态：1待派单,2待处理,8已完成,9已撤单，10转办中")
    private Integer orderState;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createStartTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createEndTime;

    @JsonIgnore
    private Set<String> parkOrgIds;

}
