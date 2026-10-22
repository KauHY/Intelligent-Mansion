package com.crlink.smartpark.common.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作日志
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "OperateLog查询对象", description = "操作日志")
public class OperateLogQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "账号")
    private String userId;

    private String requestUri;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "菜单")
    private String operateModule;

    @ApiModelProperty(value = "操作名称")
    private String operateName;

    @ApiModelProperty(value = "耗时")
    private Integer timeConsuming;

    private LocalDateTime createTime;


}
