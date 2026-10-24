package com.crlink.smartpark.inspection.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.EMPLOYEE_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InspectionTask对象", description = "")
public class InspectionTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "巡检计划ID")
    private String planId;
    
    private String parkOrgId;

    @Id2objectField(code = PARK_ORG_NAME_CODE, idField = "parkOrgId")
    private String parkOrgName;

    private String taskName;

    @ApiModelProperty(value = "巡检类型")
    private Integer type;

    @ApiModelProperty(value = "是否有序:true 有序，false 无序")
    private Boolean orderly;

    @ApiModelProperty(value = "完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime completionTime;

    @ApiModelProperty(value = "任务时间")
    private String taskTime;

    @ApiModelProperty(value = "任务开始时间")
    private String taskStartTime;

    @ApiModelProperty(value = "任务结束时间")
    private String taskEndTime;

    @Id2objectField(code = EMPLOYEE_NAME_CODE, targetFieldName = "employees")
    @JsonIgnore
    private Set<String> employeeIds;
    @ApiModelProperty(value = "巡检人")
    private Set<String> employees;

    @ApiModelProperty(value = "实际巡检人")
    @JsonIgnore
    private String actualEmployeeId;

    @Id2objectField(code = EMPLOYEE_NAME_CODE, idField = "actualEmployeeId")
    private String actualEmployeeName;

    @ApiModelProperty(value = "巡检状态")
    private Integer status;

    private List<InspectionPointDetailDTO> pointDetails;


}
