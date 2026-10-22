package com.crlink.smartpark.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作日志
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("com_operate_log")
@ApiModel(value="OperateLog数据库对象", description="操作日志")
public class OperateLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
