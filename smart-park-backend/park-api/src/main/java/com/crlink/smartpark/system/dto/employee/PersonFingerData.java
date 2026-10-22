package com.crlink.smartpark.system.dto.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PersonFingerData对象", description = "")
public class PersonFingerData implements Serializable {


    @ApiModelProperty(value = "指纹数据索引", required = true)
    private Integer fingerIndex;

    @ApiModelProperty(value = "指纹数据", required = true)
    private String finger;


}
