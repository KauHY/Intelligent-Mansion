package com.crlink.smartpark.attendance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 节假日调休表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("att_attendance_exchange_holiday")
@ApiModel(value="AttendanceExchangeHoliday数据库对象", description="节假日调休")
public class AttendanceExchangeHoliday implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "节假日调休日期")
    private String exchangeDate;

    @ApiModelProperty(value = "是否是休息日，0：否 1：是")
    private Boolean isHoliday;

    @ApiModelProperty(value = "假期类型，0：工作日，1：元旦，2：春节，3：清明节，4：劳动节，5：端午节，6：中秋节，7：国庆节")
    private Integer holidayType;

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
