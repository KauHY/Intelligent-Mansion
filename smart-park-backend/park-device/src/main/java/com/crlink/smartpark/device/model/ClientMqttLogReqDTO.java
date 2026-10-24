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
 * mqtt日志记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ClientMqttLog请求对象", description="mqtt日志记录")
public class ClientMqttLogReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id", required = true)
      private String id;

    @ApiModelProperty(value = "主题", required = true)
    private String topic;

    @ApiModelProperty(value = "消息内容", required = true)
    private String msg;

    @ApiModelProperty(value = "1：发送 2：接收", required = true)
    private Integer type;

    @ApiModelProperty(value = "网关sn", required = true)
    private String gatewaySn;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;


}
