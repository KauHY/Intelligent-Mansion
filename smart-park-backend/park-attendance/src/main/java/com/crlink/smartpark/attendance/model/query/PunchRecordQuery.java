package com.crlink.smartpark.attendance.model.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 打卡记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PunchRecord查询对象", description="打卡记录")
public class PunchRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公司ID")
    @NotBlank(message = "公司ID不能为空")
    private String companyId;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "员工ID")
    @NotBlank(message = "员工ID不能为空")
    private String employeeId;

    @ApiModelProperty(value = "员工名")
    private String employeeName;

    @ApiModelProperty(value = "打卡开始时间")
    private LocalDateTime punchDateTimeStart;

    @ApiModelProperty(value = "打卡结束时间")
    private LocalDateTime punchDateTimeEnd;

    @ApiModelProperty(value = "打卡日期", required = true)
    @NotNull(message = "日期不能为空")
    private LocalDate punchDate;

    @ApiModelProperty(value = "打卡类型:1-上下班打卡；2-外勤打卡")
    private Integer type;

}
