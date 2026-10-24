package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 卡片管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Card查询对象", description = "卡片管理")
public class CardQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "关联园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "所属企业ID")
    private String companyId;

    @ApiModelProperty(value = "持卡人")
    private String name;

    @ApiModelProperty(value = "有效期开始日期")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDate effectStartDate;

    @ApiModelProperty(value = "有效期结束日期")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDate effectEndDate;

    @ApiModelProperty(value = "状态：CARD_STATUS")
    private Integer status;

}
