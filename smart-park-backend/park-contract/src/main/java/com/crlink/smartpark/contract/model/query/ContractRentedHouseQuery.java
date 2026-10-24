package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * <p>
 * 合同
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractRentedHouseQuery对象", description="合同")
public class ContractRentedHouseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 园区Id
     */
    @ApiModelProperty(value = "园区Id", required = true)
    @NotNull(message = "园区不能为空")
    private String parkOrgId;

    /**
     * 合同开始日期
     */
    @ApiModelProperty(value = "合同开始日期", required = true)
    @NotNull(message = "合同开始日期不能为空")
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    @ApiModelProperty(value = "合同结束日期", required = true)
    @NotNull(message = "合同结束日期不能为空")
    private LocalDate endDate;

    /**
     * 不在ids范围内
     */
    @ApiModelProperty(value = "不在ids范围内")
    private Set<String> noIds;

    /**
     * 合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；
     */
    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；", required = true)
    private Integer type;
}
