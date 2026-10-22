package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 访客类型
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "VisitorType对象", description = "访客类型")
public class VisitorTypeReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "访客类型名称", required = true)
    @NotBlank(message = "访客类型名称不能为空")
    @Length(max = 30, message = "访客类型名称长度最大不能超过30")
    private String visitorTypeName;

    @ApiModelProperty(value = "入驻企业ID", required = true)
    @NotBlank(message = "入驻企业ID不能为空")
    private String companyId;

}
