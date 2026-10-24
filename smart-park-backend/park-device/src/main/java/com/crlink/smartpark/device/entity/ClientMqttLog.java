package com.crlink.smartpark.device.entity;

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
 * mqtt日志记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dev_client_mqtt_log")
@ApiModel(value="ClientMqttLog数据库对象", description="mqtt日志记录")
public class ClientMqttLog implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "主题")
    private String topic;

    @ApiModelProperty(value = "消息内容")
    private String msg;

    @ApiModelProperty(value = "1：发送 2：接收")
    private Integer type;

    @ApiModelProperty(value = "网关sn")
    private String gatewaySn;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
