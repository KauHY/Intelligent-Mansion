package com.crlink.smartpark.meeting.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class FaceCheckinMeetingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "会议主题不能为空")
    private String meetingSubject;

    @NotNull(message = "开始时间不能为空")
    private String meetingTimeStart;

    @NotNull(message = "结束时间不能为空")
    private String meetingTimeEnd;

    private String attendanceStartTime;

    private String attendanceEndTime;

    private List<String> personIds;

    private String remark;
}
