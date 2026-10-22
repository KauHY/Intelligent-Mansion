package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WithdrawReqDto对象", description="退约请求对象")
public class WithdrawReqDto {


    @ApiModelProperty(value = "合同Id")
    @NotBlank(message = "合同Id不能为空")
    private String id;
    @ApiModelProperty(value = "退约原因")
    @NotBlank
    private String withdrawReason;
    @ApiModelProperty(value = "退约日期")
    @NotNull
    private LocalDate withdrawDate;
}
