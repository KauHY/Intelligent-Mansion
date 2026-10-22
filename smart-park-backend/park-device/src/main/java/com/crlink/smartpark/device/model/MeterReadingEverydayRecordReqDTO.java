package com.crlink.smartpark.device.model;

import java.math.BigDecimal;
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
 * 每天抄表记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeterReadingEverydayRecord请求对象", description="每天抄表记录")
public class MeterReadingEverydayRecordReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "仪表id", required = true)
    private String meterId;

    @ApiModelProperty(value = "抄表读数", required = true)
    private BigDecimal meterReading;

    @ApiModelProperty(value = "抄表时间", required = true)
    private LocalDateTime readingTime;

    @ApiModelProperty(value = "修正后读数", required = true)
    private BigDecimal correctedReading;

    @ApiModelProperty(value = "抄表年份", required = true)
    private Integer year;

    @ApiModelProperty(value = "抄表月份", required = true)
    private Integer month;

    @ApiModelProperty(value = "抄表天", required = true)
    private Integer day;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
