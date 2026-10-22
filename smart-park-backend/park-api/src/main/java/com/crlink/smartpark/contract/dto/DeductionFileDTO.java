package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DeductionFileDTO", description = "DeductionFileDTO")
public class DeductionFileDTO implements Serializable {

    @ApiModelProperty(value = "附件key")
    private String key;

    @ApiModelProperty(value = "附件名")
    private String name;

    public DeductionFileDTO(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
