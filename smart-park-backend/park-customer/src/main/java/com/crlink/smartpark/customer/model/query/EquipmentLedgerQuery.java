package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 设备台账
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EquipmentLedger查询对象", description="设备台账")
public class EquipmentLedgerQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "园区id")
    @NotBlank
    private String parkOrgId;

    @ApiModelProperty(value = "设备名，同一园区下不重复")
    private String name;

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "状态：0-已报废；1-正常")
    private Integer state;


}
