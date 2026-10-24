package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_TIME_FORMAT;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.EMPLOYEE_NAME_CODE;

@Data
public class FormEditorsDTO extends EditorsDTO {
    private String submissionId;
    @ApiModelProperty(value = "提交人", required = true)
    private String employeeId;
    @Id2objectField(idField = "employeeId", code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    private String employeeName;
    @ApiModelProperty(value = "接收人", required = true)
    @Id2objectField(code = EMPLOYEE_NAME_CODE, targetFieldName = "receivedEmployeeNames")
    private List<String> receivedEmployeeIds;
    private List<String> receivedEmployeeNames;

    @ApiModelProperty(value = "创建时间或截止时间")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createTime;
    @ApiModelProperty(value = "状态，1设计中，2收集中，3已结束")
    private Integer status;


    private Integer cycle;
}
