package com.crlink.smartpark.attendance.model;

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
 * 考勤规则关联员工
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AttendanceRuleRelation请求对象", description="考勤规则关联员工")
public class AttendanceRuleRelationReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID", required = true)
      private String id;

    @ApiModelProperty(value = "考勤规则ID", required = true)
    private String ruleId;

    @ApiModelProperty(value = "员工ID", required = true)
    private String employeeId;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "修改人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "删除状态", required = true)
    private Boolean deleted;


}
