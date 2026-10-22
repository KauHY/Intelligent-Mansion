package com.crlink.smartpark.system.dto.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PersonFingerReqDTO请求对象", description = "")
public class PersonFingerReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fingerId;

    @ApiModelProperty(value = "员工id", required = true)
    private String employeeId;

    @ApiModelProperty(value = "指纹数据", required = true)
    private List<PersonFingerData> fingers;


}
