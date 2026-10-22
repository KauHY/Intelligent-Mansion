package com.crlink.smartpark.meeting.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ManageQuery
 * @Package com.crlink.smartpark.meeting.model.query
 * @Description: 会议室分页查询参数
 * @Version 1.0
 */
@Data
public class MeetingManageTreeQuery extends BaseQuery  implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}
