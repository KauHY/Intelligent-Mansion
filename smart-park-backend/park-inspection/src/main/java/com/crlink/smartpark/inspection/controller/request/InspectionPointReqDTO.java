package com.crlink.smartpark.inspection.controller.request;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InspectionPointReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "巡检点名称")
    @Length(max = 30, min = 1)
    private String pointName;

    @ApiModelProperty(value = "园区id")
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "巡检点类型")
    @CustomValid({"1", "2"})
    private Integer type;

    @ApiModelProperty(value = "巡检位置")
    @Length(max = 30, min = 1)
    private String location;

    @ApiModelProperty(value = "备注")
    @Length(max = 100)
    private String remark;


}
