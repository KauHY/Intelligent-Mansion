package com.crlink.smartpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.meeting.entity.FaceCheckinAttendance;
import com.crlink.smartpark.meeting.entity.FaceCheckinPerson;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinMemberDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinPersonDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinVerifyDTO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinAttendanceVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinPersonVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinRequestVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinResultVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Face check-in service interface.
 */
public interface FaceCheckinService extends IService<FaceCheckinPerson> {

    /**
     * Verify a face image against the registered person database and perform attendance logic.
     */
    FaceCheckinResultVO verify(FaceCheckinVerifyDTO dto);

    /**
     * Register a person with face photo.
     */
    Boolean savePerson(FaceCheckinPersonDTO dto);

    /**
     * List registered persons with pagination.
     */
    PageInfo<FaceCheckinPersonVO> listPersons(int pageNum, int pageSize, String keyword);

    /**
     * Update meeting member list for face check-in.
     */
    Boolean saveMembers(FaceCheckinMemberDTO dto);

    List<FaceCheckinPersonVO> listMembers(String meetingReserveId);

    /**
     * Query attendance records for a meeting.
     */
    List<FaceCheckinAttendanceVO> listAttendance(String meetingReserveId);

    /**
     * Export attendance records as CSV content.
     */
    String exportAttendanceCsv(String meetingReserveId);

    /**
     * Update person audit status (approve/reject).
     */
    Boolean auditPerson(String personId, Integer auditStatus);

    /**
     * Generate absent list: expected attendees minus those with successful check-in.
     */
    java.util.List<com.crlink.smartpark.meeting.model.vo.FaceCheckinAbsentVO> listAbsent(String meetingReserveId);

    /**
     * List hardware upload/verification request records without photo content.
     */
    List<FaceCheckinRequestVO> listRequests(String meetingReserveId);

    java.util.List<com.crlink.smartpark.meeting.entity.MeetingReserve> listMeetings();

    Boolean saveMeeting(com.crlink.smartpark.meeting.model.dto.FaceCheckinMeetingDTO dto);

    Boolean updateMeeting(com.crlink.smartpark.meeting.model.dto.FaceCheckinMeetingDTO dto);

    Boolean removeMeeting(String meetingId);
}
