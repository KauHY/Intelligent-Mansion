package com.crlink.smartpark.customer.controller.request;

import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.customer.dto.Goods;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GoodsRelease请求对象", description = "")
public class GoodsReleaseReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //private String id;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "申请人姓名")
    private String submitterName;

    @ApiModelProperty(value = "申请人手机号")
    private String submitterMobile;

    @ApiModelProperty(value = "所在公司")
    private String company;

    @ApiModelProperty(value = "放行时间0")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS, timezone = Constants.TIMEZONE)
    private LocalDateTime releaseDatetimeStart;

    @ApiModelProperty(value = "放行时间1")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS, timezone = Constants.TIMEZONE)
    private LocalDateTime releaseDatetimeEnd;

    @ApiModelProperty(value = "放行物品")
    @NotNull(message = "放行物品不能为空")
    @Size(max = 20)
    private List<Goods> goodsList;

    @ApiModelProperty(value = "车牌号")
    private String carNumber;

    @ApiModelProperty(value = "备注")
    private String remark;

}
