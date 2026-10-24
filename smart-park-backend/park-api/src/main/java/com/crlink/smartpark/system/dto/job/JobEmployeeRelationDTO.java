package com.crlink.smartpark.system.dto.job;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 岗位用户关系
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "JobEmployeeRelation对象", description = "岗位用户关系")
public class JobEmployeeRelationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String jobId;

    private String employeeId;

    private Boolean deleted;


}
