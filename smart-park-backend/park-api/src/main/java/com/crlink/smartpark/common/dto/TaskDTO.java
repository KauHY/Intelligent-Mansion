package com.crlink.smartpark.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 后台任务
 * </p>
 * */
@Data
@Accessors(chain = true)
@ApiModel(value = "Task对象", description = "后台任务")
public class TaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "任务名")
    private String taskName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "表达式")
    private String cron;

    @ApiModelProperty(value = "是否关闭，0开启，1关闭")
    private Boolean closed;

    @ApiModelProperty(value = "任务线程数")
    private Integer poolSize;

    @ApiModelProperty(value = "线程名前缀")
    private String threadNamePrefix;

    private Boolean removeOnCancelPolicy;

    private String applicationName;
    private String beanName;
    private String methodName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return id.equals(taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
