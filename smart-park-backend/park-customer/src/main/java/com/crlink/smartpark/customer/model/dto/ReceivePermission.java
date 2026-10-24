package com.crlink.smartpark.customer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 领用权限设置
 * </p>
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivePermission implements Serializable {

    @ApiModelProperty(value = "公司id,逗号分隔")
    private String companyId;

    @ApiModelProperty(value = "员工id,逗号分隔")
    private String employeeId;

}
