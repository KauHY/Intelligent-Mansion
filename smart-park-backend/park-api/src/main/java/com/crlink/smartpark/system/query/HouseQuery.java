package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 房屋表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "House对象", description = "房屋表")
public class HouseQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "楼层房号")
    private String name;

    @ApiModelProperty(value = "房屋编码")
    private String code;

    @ApiModelProperty(value = "所在楼层")
    private Integer floor;

    @ApiModelProperty(value = "套内面积")
    private Double coverArea;

    @ApiModelProperty(value = "建筑面积")
    private Double buildArea;

    @ApiModelProperty(value = "楼栋ID")
    private String BuildingId;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    //@ApiModelProperty(value = "租赁状态：false：未租 true：已租")
    //private Boolean rent;

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
