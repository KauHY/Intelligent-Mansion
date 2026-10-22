package com.crlink.smartpark.system.dto.house;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 房屋入驻记录表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HouseRentRenewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "租赁ID")
    @NotBlank(message = "租赁ID不能为空")
    private String id;

    @ApiModelProperty(value = "续租到期时间")
    @NotNull(message = "续租到期时间不能为空")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate rentRenewEndTime;

}
