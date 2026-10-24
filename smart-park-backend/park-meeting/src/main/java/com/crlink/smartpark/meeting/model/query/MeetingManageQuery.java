package com.crlink.smartpark.meeting.controller.request.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalTime;
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
 * 会议室管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingManage查询对象", description="会议室管理")
public class MeetingManageQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属楼栋单元")
    private String buildingId;

    @ApiModelProperty(value = "所在楼层")
    private String floorId;

    @ApiModelProperty(value = "楼层房号")
    private String houseId;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "会议室类型 1：普通会议 2：视频会议")
    private Integer meetingType;

    @ApiModelProperty(value = "开始开放时间")
    private LocalTime openTimeStart;

    @ApiModelProperty(value = "结束开放时间")
    private LocalTime openTimeEnd;

    @ApiModelProperty(value = "容纳人数")
    private Integer capacity;

    @ApiModelProperty(value = "会议室设备 1摄像机 2远程会议设备 3DVR 4电话 5投影仪 6电子白板")
    private String meetingEquipment;

    @ApiModelProperty(value = "状态：1开启 0关闭")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;


}
