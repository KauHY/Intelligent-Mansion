package com.crlink.smartpark.inspection.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡检路线
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InspectionCircuit对象", description = "巡检路线")
public class InspectionCircuitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "巡检路线名")
    private String circuitName;

    @ApiModelProperty(value = "巡检类型")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否有序")
    private Boolean orderly;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate updateTime;


    @ApiModelProperty(value = "巡检点")
    private List<InspectionPointDTO> points;


}
