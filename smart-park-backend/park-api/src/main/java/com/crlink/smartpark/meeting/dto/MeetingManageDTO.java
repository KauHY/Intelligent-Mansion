package com.crlink.smartpark.meeting.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.annotations.Id2objectNestingField;
import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.*;

/**
 * @ClassName: MeetingManageDTO
 * @Package com.crlink.smartpark.meeting.dto
 * @Description: MeetingManage详情数据
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingManage详情数据", description = "会议室管理")
public class MeetingManageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @Id2objectField(code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "所属楼栋单元")
    private String buildingId;

    @ApiModelProperty("楼栋名")
    @Id2objectField(code = BUILDING_NAME_CODE)
    private String buildingName;

    @ApiModelProperty(value = "所在楼层")
    private String floorId;

    @ApiModelProperty(value = "楼层房号")
    private String houseId;

    @ApiModelProperty(value = "楼层房号名称")
    @Id2objectField(code = HOUSE_NAME_CODE)
    private String houseName;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "会议室类型 1：普通会议 2：视频会议")
    private Integer meetingType;

    @ApiModelProperty(value = "开始开放时间")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT, timezone = Constants.TIMEZONE)
    private LocalTime openTimeStart;

    @ApiModelProperty(value = "结束开放时间")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT, timezone = Constants.TIMEZONE)
    private LocalTime openTimeEnd;

    @ApiModelProperty(value = "容纳人数")
    private Integer capacity;

    @ApiModelProperty(value = "会议室设备 1摄像机 2远程会议设备 3DVR 4电话 5投影仪 6电子白板")
    private String meetingEquipment;

    @ApiModelProperty(value = "预订是否需审核 1是 0否")
    private Integer needReview;

    @ApiModelProperty(value = "审核人列表")
    @Id2objectNestingField()
    private List<MeetingReviewDTO> reviewerList;

    @ApiModelProperty(value = "状态：1开启 0关闭")
    private Integer status;

    @ApiModelProperty(value = "会议室介绍")
    private String remark;

    /**
     * 根据类型区分
     */
    @ApiModelProperty(value = "权限类型 1公司 2员工")
    private Integer relationType;


    @ApiModelProperty(value = "公司权限层级列表")
    @Id2objectNestingField()
    private List<MeetingPermissionDTO> companyList;

    @ApiModelProperty(value = "员工权限层级列表")
    @Id2objectNestingField()
    private List<MeetingPermissionDTO> employeeList;

    @ApiModelProperty(value = "会议屏设备的主键Id")
    private String screenDeviceId;


}
