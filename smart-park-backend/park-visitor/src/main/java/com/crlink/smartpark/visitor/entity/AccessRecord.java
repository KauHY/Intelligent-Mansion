package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 人行抓拍记录表
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vis_access_record")
public class AccessRecord implements Serializable {
    private static final long serialVersionUID = -5574018213896128902L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 登记照片url
     */
    private String faceImageUrl;

    /**
     * 抓拍照片url
     */
    private String sceneImageUrl;

    /**
     * 用户标签ID(逗号隔开)
     */
    private String labelIds;

    /**
     * 用户标签(逗号隔开)
     */
    private String labels;

    /**
     * 识别时间
     */
    private LocalDateTime recognizeTime;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 园区名
     */
    private String parkOrgName;

    /**
     * 设备位置
     */
    private String deviceLocation;

    /**
     * 是否通过：0-识别不通过，1-识别通过
     */
    private Integer isPass;

    /**
     * 体温是否超过37.3：0正常、1超标
     */
    private Integer isOverTemp;

    /**
     * 员工ID
     */
    private String employeeId;

    /**
     * 健康码，0：绿色 1：黄色 2：红色
     */
    private Integer healthCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

}
