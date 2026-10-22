package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 房屋入驻记录表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HouseRentRecord对象", description="房屋入驻记录表")
public class HouseRentRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    @ApiModelProperty(value = "企业ID")
    private String companyId;

    @ApiModelProperty(value = "起租时间")
    private LocalDate rentStartTime;

    @ApiModelProperty(value = "到期时间")
    private LocalDate rentEndTime;

    @ApiModelProperty(value = "绑定状态：true：已绑定 false：已解绑")
    private Boolean bind;

    @ApiModelProperty(value = "解绑时间")
    private LocalDateTime unbindTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;

    @ApiModelProperty(value = "ids")
    private List<String> ids;


}
