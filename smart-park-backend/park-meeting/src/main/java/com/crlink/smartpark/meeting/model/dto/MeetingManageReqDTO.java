package com.crlink.smartpark.meeting.model.dto;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 会议室管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingManage请求对象", description = "会议室管理")
public class MeetingManageReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "所属园区")
    @NotEmpty(message = "所属园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "所属楼栋单元")
    @NotEmpty(message = "所属楼栋单元不能为空")
    private String buildingId;

    @ApiModelProperty(value = "所在楼层")
    @NotEmpty(message = "所在楼层不能为空")
    private String floorId;

    @ApiModelProperty(value = "楼层房号")
    @NotEmpty(message = "楼层房号不能为空")
    private String houseId;

    @ApiModelProperty(value = "会议室名称")
    @NotEmpty(message = "会议室名称不能为空")
    private String meetingName;

    @ApiModelProperty(value = "会议室类型 1：普通会议 2：视频会议")
    @NotNull(message = "会议室类型不能为空")
    private Integer meetingType;

    @ApiModelProperty(value = "开始开放时间")
    @NotNull(message = "开放时间不能为空")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT, timezone = Constants.TIMEZONE)
    private LocalTime openTimeStart;

    @ApiModelProperty(value = "结束开放时间")
    @NotNull(message = "开放时间不能为空")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT, timezone = Constants.TIMEZONE)
    private LocalTime openTimeEnd;

    @ApiModelProperty(value = "容纳人数")
    @NotNull(message = "容纳人数不能为空")
    private Integer capacity;

    @ApiModelProperty(value = "会议室设备 1摄像机 2远程会议设备 3DVR 4电话 5投影仪 6电子白板")
    private String meetingEquipment;

    @ApiModelProperty(value = "预订是否需审核 1是 0否")
    @NotNull(message = "预订是否需审核不能为空")
    private Integer needReview;

    //TODO 会议室审核人列表
    @ApiModelProperty(value = "会议预订审核人列表")
    private List<String> reviewerList;

    @ApiModelProperty(value = "会议室介绍")
    private String remark;

    @ApiModelProperty(value = "会议屏设备的主键Id")
    private String screenDeviceId;

    /**
     * 根据类型区分
     */
    @ApiModelProperty(value = "权限类型 1公司 2员工")
    private Integer relationType;

    @ApiModelProperty(value = "公司权限管理列表")
    private List<MeetingPermissReqDTO> companyIds;

    @ApiModelProperty(value = "员工权限管理列表")
    private List<MeetingPermissReqDTO> employeeIds;
}
