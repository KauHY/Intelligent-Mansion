package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * 园区运营平台-人行管理-抓拍记录分页查询对象
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AccessRecord对象", description="人行抓拍记录表")
public class AccessRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String userName;

    /**
     *手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     *用户标签ID
     */
    @ApiModelProperty(value = "用户标签ID")
    private String labelId;

    /**
     *用户标签
     */
    @ApiModelProperty(value = "用户标签")
    private String label;

    /**
     *设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    /**
     *园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     *是否通过：0-识别不通过，1-识别通过
     */
    @ApiModelProperty(value = "是否通过：0-识别不通过，1-识别通过")
    private Integer isPass;

    /**
     *识别时间段-开始
     */
    @ApiModelProperty(value = "识别时间段-开始")
    private String startTime;

    /**
     *识别时间段-结束
     */
    @ApiModelProperty(value = "识别时间段-结束")
    private String endTime;

    /**
     *考勤规则名称
     */
    @ApiModelProperty(value = "考勤规则名称")
    private String attendanceRuleName;


}
