package com.crlink.smartpark.device.model;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 报警记录
 * </p>
 * */
@Data
public class FireWarningRecordHandleReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String id;

    @ApiModelProperty(value = "确认结果，1,真实火警，2误报，3消防演习")
    @CustomValid({"1", "2", "3"})
    private Integer realReasonType;

    @ApiModelProperty(value = "处理意见")
    @Length(max = 100)
    private String opinion;

}
