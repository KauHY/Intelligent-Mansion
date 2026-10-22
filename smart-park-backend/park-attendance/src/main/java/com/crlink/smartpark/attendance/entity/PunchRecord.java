package com.crlink.smartpark.attendance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 打卡记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "att_punch_record", autoResultMap = true)
@ApiModel(value="PunchRecord数据库对象", description="打卡记录")
public class PunchRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "员工名")
    private String employeeName;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "公司名")
    private String companyName;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "打卡日期")
    private LocalDate punchDate;

    @ApiModelProperty(value = "打卡时间")
    private LocalTime punchTime;

    @ApiModelProperty(value = "打卡具体时间")
    private LocalDateTime punchDateTime;

    @ApiModelProperty(value = "打卡地点")
    private String location;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "打卡类型:1-上下班打卡；2-外勤打卡")
    private Integer type;

    @ApiModelProperty(value = "扩展字段:事由:reason;图片key:picKey")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Map<String, Object> extension;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
