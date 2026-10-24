package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 来访须知
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Notice请求对象", description = "来访须知")
public class NoticeReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "园区id", required = true)
    @NotBlank(message = "园区id不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "须知名称", required = true)
    @NotBlank(message = "须知名称不能为空")
    private String noticeName;

    @ApiModelProperty(value = "须知内容")
    @NotBlank(message = "须知内容不能为空")
    private String noticeContent;

}
