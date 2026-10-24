package com.crlink.smartpark.device.model;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "CardReqDTO请求对象", description = "卡片管理")
public class CardReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "卡号", required = true)
    @NotBlank(message = "卡号不能为空")
    private String cardNo;

    @ApiModelProperty(value = "卡片介质:CARD_MEDIUM  1,IC/ID卡", required = true)
    @NotNull(message = "卡片介质不能为空")
    private Integer cardMedium;

    @ApiModelProperty(value = "卡片类型:CARD_TYPE：1普通卡、2管理员卡、3其他")
    private Integer cardType;

    @ApiModelProperty(value = "关联园区ID", required = true)
    @NotBlank(message = "关联园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "所属企业ID", required = true)
    @NotBlank(message = "所属企业不能为空")
    private String companyId;

    @ApiModelProperty(value = "关联员工ID", required = true)
    @NotBlank(message = "关联员工不能为空")
    private String employeeId;

    @ApiModelProperty(value = "有效期开始日期", required = true)
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    @NotNull(message = "生效开始日期不能为空")
    private LocalDate effectStartDate;

    @ApiModelProperty(value = "有效期结束日期", required = true)
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    @NotNull(message = "生效结束日期不能为空")
    private LocalDate effectEndDate;

    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;
}
