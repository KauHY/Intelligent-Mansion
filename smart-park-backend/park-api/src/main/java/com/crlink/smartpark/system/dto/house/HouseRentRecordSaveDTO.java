package com.crlink.smartpark.system.dto.house;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class HouseRentRecordSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "房屋ID")
    @NotBlank(message = "房屋ID不能为空")
    private String houseId;

    @ApiModelProperty(value = "楼栋ID")
    @NotBlank(message = "楼栋id不能为空")
    private String buildingId;

    @ApiModelProperty(value = "起租时间")
    @NotNull(message = "起租时间不能为空")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate rentStartTime;

    @ApiModelProperty(value = "到期时间")
    @NotNull(message = "到期时间不能为空")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate rentEndTime;

    @ApiModelProperty(value = "创建人")
    @JsonIgnore
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonIgnore
    private String updateBy;

}
