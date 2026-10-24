package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报警记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dev_fire_warning_record")
@ApiModel(value = "FireWarningRecord数据库对象", description = "报警记录")
public class FireWarningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "消防设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(value = "报警类型")
    private Integer warnType;

    @ApiModelProperty(value = "报警时间")
    private LocalDateTime warnTime;

    @ApiModelProperty(value = "确认结果，1,真实火警，2误报，3消防演习")
    private Integer realReasonType;

    @ApiModelProperty(value = "处理意见")
    private String opinion;

    @ApiModelProperty(value = "状态,0待处理，1已处理")
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;

}
