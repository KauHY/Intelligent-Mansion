package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: ContractDownloadReqDTO
 * @Package com.crlink.smartpark.contract.model.request
 * @Description: 合同下载入参
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractDownloadReqDTO对象", description="合同下载")
public class ContractDownloadDTO {

    @ApiModelProperty(value = "合同表主键")
    private String contractId;

    @ApiModelProperty(value = "使用的模板KEY")
    private String key;

    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private String type;
}
