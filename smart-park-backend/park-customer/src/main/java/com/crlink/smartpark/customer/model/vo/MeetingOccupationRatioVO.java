package com.crlink.smartpark.customer.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 会议室租赁占用率VO
 *
 */
@Data
public class MeetingOccupationRatioVO implements Serializable {
    private static final long serialVersionUID = 1887775115357570223L;

    private Integer reserveCount;  //会议当天实时占用数
    private Integer meetingCount;  //会议室总数
}
