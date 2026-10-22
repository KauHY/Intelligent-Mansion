package com.crlink.smartpark.customer.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectNestingField;
import com.crlink.smartpark.customer.dto.ProReceiveDetailDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 领用权限设置
 * </p>
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveDetailVo implements Serializable {

    @ApiModelProperty(value = "领用人电话")
    private String mobile;

    @ApiModelProperty(value = "领用流程详情")
    @Id2objectNestingField
    List<ProReceiveDetailDTO> receiveDetailList;

}
