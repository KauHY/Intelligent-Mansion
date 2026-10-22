package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;

import java.time.LocalDate;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 合同
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractListQuery对象", description="合同")
public class ContractListQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 园区Id
     */
    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "房屋Id")
    private String houseId;

    @ApiModelProperty(value = "楼栋名")
    private String buildingName;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String name;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String no;

    @ApiModelProperty(value = "乙方名称")
    private String nameB;

    /**
     * 合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；
     */
    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    @NotNull(message = "合同类型不能为空")
    private Integer type;

    /**
     * 合同开始日期
     */
    @ApiModelProperty(value = "合同开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    @ApiModelProperty(value = "合同结束日期")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate endDate;


    /**
     * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
     */
    @ApiModelProperty(value = "合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中")
    private Integer state;

    @ApiModelProperty(value = "状态：0-关闭；1-开启")
    private Integer status;
}
