package com.crlink.smartpark.system.dto.job;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class JobUserRespDTO implements Serializable {
    private String userId;
    private String username;

    @ApiModelProperty(value = "是否有工作任务")
    private boolean hasJob;

    public JobUserRespDTO(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
