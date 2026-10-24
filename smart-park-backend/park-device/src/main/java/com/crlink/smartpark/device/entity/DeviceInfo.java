package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 设备信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "dev_device_info", autoResultMap = true)
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 拓展字段
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Map<String, Object> extension;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0离线1在线
     */
    private Integer status;

    /**
     * 使用状态,1正常使用
     */
    private Integer usingStatus;

    /**
     * 删除状态
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 修改者
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 入驻企业ID
     */
    private String companyId;

    /**
     * 通道号
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Set<String> channelNos;

    /**
     * 设备大类
     */
    private Integer deviceCategory;


}
