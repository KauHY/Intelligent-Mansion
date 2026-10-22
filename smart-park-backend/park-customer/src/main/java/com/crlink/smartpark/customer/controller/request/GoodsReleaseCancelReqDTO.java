package com.crlink.smartpark.customer.controller.request;

import com.crlink.smartpark.core.validation.CustomValid;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class GoodsReleaseCancelReqDTO implements Serializable {
    @NotEmpty
    private String goodsReleaseId;
    @CustomValid({"4"})
    private Integer status;
}
