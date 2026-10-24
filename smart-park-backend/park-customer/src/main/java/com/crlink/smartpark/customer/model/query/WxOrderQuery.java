package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 报事投诉、报修服务表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "WxOrder对象", description = "报事投诉、报修服务列表")
public class WxOrderQuery extends BaseQuery implements Serializable {

    //@ApiModelProperty(value = " true运营端，false C端")
    //@NotNull
    //private Boolean queryFromOperationClient;

    @ApiModelProperty(value = "园区编码")
    private String parkOrgId;

    @ApiModelProperty(value = "报事报修类型：1-报事投诉、2-报修服务", required = true)
    @Min(value = 1, message = "参数错误")
    @Max(value = 2, message = "参数错误")
    @NotNull(message = "报事报修类型不能为空")
    private Integer orderType;

    @ApiModelProperty(value = "公司Id")
    private String companyId;

    @ApiModelProperty(value = "处理状态：1待派单,2待处理,8已完成，10转办中")
    private Integer orderState;

}
