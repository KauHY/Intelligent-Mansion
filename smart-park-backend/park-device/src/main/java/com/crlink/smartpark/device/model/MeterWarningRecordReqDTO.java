package com.crlink.smartpark.device.model;

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
import java.util.List;

/**
 * <p>
 * 仪表异常告警记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeterWarningRecord请求对象", description="仪表异常告警记录")
public class MeterWarningRecordReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    private String meterId;

    @ApiModelProperty(value = "告警事件", required = true)
    private Integer warningCode;

    @ApiModelProperty(value = "告警等级", required = true)
    private Integer warningLevel;

    @ApiModelProperty(value = "告警时间", required = true)
    private LocalDateTime warningTime;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;


}
