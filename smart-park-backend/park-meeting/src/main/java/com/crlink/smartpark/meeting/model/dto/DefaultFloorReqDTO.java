package com.crlink.smartpark.meeting.model.dto;

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
 * 会议室默认楼层
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DefaultFloor请求对象", description="会议室默认楼层")
public class DefaultFloorReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "所属园区", required = true)
    private String parkOrgId;

//    @ApiModelProperty(value = "绑定的企业ID", required = true)
//    private String companyId;

    @ApiModelProperty(value = "关联员工id", required = true)
    private String employeeId;

    @ApiModelProperty(value = "设置默认楼栋单元", required = true)
    private String buildingId;

    @ApiModelProperty(value = "默认楼层", required = true)
    private String floorNum;



}
