package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * 园区运营平台-车行管理-车辆管理查询对象
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Car对象", description="车辆信息")
public class CarQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String carNum;

    @ApiModelProperty(value = "车主姓名")
    private String carOwnerName;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    @ApiModelProperty(value = "停车类型：1员工车辆,2公务车辆,3外部车辆")
    private Integer parkType;

    /**
     *园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     *企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private String companyId;

    /**
     *状态：0-未生效；1-已生效；2-已失效
     */
    @ApiModelProperty(value = "状态：0-未生效；1-已生效；2-已失效")
    private Integer status;

    @ApiModelProperty(value = "停车场ID")
    private String parkingLotId;

}
