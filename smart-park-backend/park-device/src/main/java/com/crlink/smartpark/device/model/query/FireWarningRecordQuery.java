package com.crlink.smartpark.device.model.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 报警记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FireWarningRecord查询对象", description = "报警记录")
public class FireWarningRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Set<String> recordIds;

    @JsonIgnore
    private Set<String> parkOrgIds;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    private String deviceId;

    private Integer deviceType;

    @ApiModelProperty(value = "报警类型")
    private Integer warnType;

    @ApiModelProperty(value = "报警时间0")
    private LocalDateTime warnTimeStart;
    @ApiModelProperty(value = "报警时间1")
    private LocalDateTime warnTimeEnd;


    public static FireWarningRecordQuery buildRecordIds(String... recordId) {
        FireWarningRecordQuery query = new FireWarningRecordQuery();
        query.setRecordIds(Sets.newHashSet(recordId));
        return query;
    }

}
