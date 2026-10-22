package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 园区运营平台-人行管理-抓拍记录DTO
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AccessRecord对象", description = "人行抓拍记录表")
public class AccessRecordDTO implements Serializable {

    private static final long serialVersionUID = 401041018959054850L;

    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 登记照片url
     */
    @ApiModelProperty(value = "登记照片url")
    private String faceImageUrl;

    /**
     * 抓拍照片url
     */
    @ApiModelProperty(value = "抓拍照片url", required = true)
    private String sceneImageUrl;

    /**
     * 用户标签ID(逗号隔开)
     */
    @ApiModelProperty(value = "用户标签ID(逗号隔开)")
    private String labelIds;

    /**
     * 用户标签(逗号隔开)
     */
    @ApiModelProperty(value = "用户标签(逗号隔开)")
    private String labels;

    /**
     * 识别时间
     */
    @ApiModelProperty(value = "识别时间")
    private LocalDateTime recognizeTime;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", required = true)
    private String deviceId;

    /**
     * 设备名
     */
    @ApiModelProperty(value = "设备名")
    private String deviceName;

    /**
     * 园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     * 园区名
     */
    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    /**
     * 设备位置
     */
    @ApiModelProperty(value = "设备位置")
    private String deviceLocation;

    /**
     * 是否通过：0-识别不通过，1-识别通过
     */
    @ApiModelProperty(value = "是否通过：0-识别不通过，1-识别通过")
    private Integer isPass;

    /**
     * 体温异常
     */
    @ApiModelProperty(value = "体温异常")
    private Integer isOverTemp;

    /**
     * 健康码，0：绿色 1：黄色 2：红色
     */
    @ApiModelProperty(value = "健康码，0：绿色 1：黄色 2：红色")
    private Integer healthCode;

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    private String createBy;
}
