package com.crlink.smartpark.business.controller.request;

import com.crlink.smartpark.core.validation.CustomValid;
import com.crlink.smartpark.core.validation.DefaultValidHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BusinessChanceOperationDTO", description = "操作商机")
public class BusinessChanceOperationDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "公司Id")
    private String companyId;

    @ApiModelProperty(value = "园区Id：后台领取商机时必传")
    private String parkOrgId;

    @ApiModelProperty(value = "跟进人Id")
    private String followEmployeeId;

    @ApiModelProperty(value = "操作类型：2分配商机、3领取商机、7转移商机、8释放商机'", required = true)
    @NotNull(message = "操作类型不能为空")
    @CustomValid(value = {"2", "3", "7", "8"}, handler = DefaultValidHandler.class)
    private Integer recordType;
}
