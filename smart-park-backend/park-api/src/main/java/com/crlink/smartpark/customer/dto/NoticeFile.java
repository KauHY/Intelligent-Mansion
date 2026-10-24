package com.crlink.smartpark.customer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NoticeFile implements Serializable {

    @ApiModelProperty(value = "文件类型")
    @Length(max = 30)
    private String type;

    @ApiModelProperty(required = true)
    @Length(max = 255)
    private String key;

}
