package com.crlink.smartpark.system.dto.menu;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class UpdateMenuStatusReqDTO implements Serializable {
    private String menuId;
    @Min(0)
    @Max(1)
    private Integer status;

}
