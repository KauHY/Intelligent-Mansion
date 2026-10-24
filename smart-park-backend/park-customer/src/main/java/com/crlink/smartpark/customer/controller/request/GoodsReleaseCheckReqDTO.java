package com.crlink.smartpark.customer.controller.request;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class GoodsReleaseCheckReqDTO implements Serializable {

    @NotEmpty
    private String goodsReleaseId;

    @ApiModelProperty(value = "2通过，3拒绝")
    @CustomValid(value = {"2", "3"})
    private Integer checkStatus;

    @ApiModelProperty(value = "审核意见")
    @NotEmpty(message = "审核意见不能为空！")
    @Length(max = 200, min = 1)
    private String checkOpinion;
}
