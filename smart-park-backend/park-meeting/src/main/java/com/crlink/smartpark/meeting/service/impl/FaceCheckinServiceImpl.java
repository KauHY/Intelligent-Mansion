package com.crlink.smartpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.meeting.entity.FaceCheckinAttendance;
import com.crlink.smartpark.meeting.entity.FaceCheckinMeetingMember;
import com.crlink.smartpark.meeting.entity.FaceCheckinPerson;
import com.crlink.smartpark.meeting.entity.FaceCheckinRequest;
import com.crlink.smartpark.meeting.mapper.FaceCheckinAttendanceMapper;
import com.crlink.smartpark.meeting.mapper.FaceCheckinMeetingMemberMapper;
import com.crlink.smartpark.meeting.mapper.FaceCheckinPersonMapper;
import com.crlink.smartpark.meeting.mapper.FaceCheckinRequestMapper;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinMemberDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinMeetingDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinPersonDTO;
import com.crlink.smartpark.meeting.model.dto.FaceCheckinVerifyDTO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinAbsentVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinAttendanceVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinPersonVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinRequestVO;
import com.crlink.smartpark.meeting.model.vo.FaceCheckinResultVO;
import com.crlink.smartpark.meeting.entity.MeetingReserve;
import com.crlink.smartpark.meeting.service.FaceCheckinService;
import com.crlink.smartpark.meeting.service.MeetingReserveService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Face check-in service implementation.
 */
@Slf4j
@Service
public class FaceCheckinServiceImpl extends ServiceImpl<FaceCheckinPersonMapper, FaceCheckinPerson> implements FaceCheckinService {

    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_ALREADY = 2;
    private static final int STATUS_NO_PERMISSION = 3;
    private static final int STATUS_NOT_RECOGNIZED = 4;
    private static final int STATUS_NOT_STARTED = 5;
    private static final int STATUS_FAILED = 7;
    private static final int CATEGORY_MEMBER = 1;
    private static final int CATEGORY_NOT_MEMBER = 2;
    private static final int CATEGORY_UNREGISTERED = 3;
    private static final double MATCH_THRESHOLD = 0.50;
    private static final String INFERENCE_URL = System.getenv().getOrDefault(
            "FACE_INFERENCE_URL", "http://face-inference:8901/match");
    private static final String INFERENCE_EXTRACT_URL = System.getenv().getOrDefault(
            "FACE_INFERENCE_EXTRACT_URL", INFERENCE_URL.replaceFirst("/match/?$", "/extract"));
    private static final String INFERENCE_BATCH_URL = System.getenv().getOrDefault(
            "FACE_INFERENCE_BATCH_URL", INFERENCE_URL.replaceFirst("/match/?$", "/match-batch"));

    @Autowired
    private FaceCheckinAttendanceMapper attendanceMapper;
    @Autowired
    private FaceCheckinRequestMapper requestMapper;
    @Autowired
    private FaceCheckinMeetingMemberMapper memberMapper;
    @Autowired
    private MeetingReserveService meetingReserveService;

    @Value("${face-checkin.device-token:board-token}")
    private String expectedDeviceToken;

    @Override
    public FaceCheckinResultVO verify(FaceCheckinVerifyDTO dto) {
        FaceCheckinResultVO result = new FaceCheckinResultVO();
        result.setRequestId(dto.getRequestId());
        result.setMeetingReserveId(dto.getMeetingReserveId());
        MeetingReserve verifyMeeting = getActiveMeeting(dto.getMeetingReserveId());
        if (verifyMeeting != null) { result.setMeetingSubject(verifyMeeting.getMeetingSubject()); }
        try {
            if (StringUtils.isBlank(dto.getDeviceToken()) || !expectedDeviceToken.equals(dto.getDeviceToken())) {
                result.setStatus(STATUS_FAILED);
                result.setMessage("设备未授权");
                return result;
            }

            // Idempotency check: if the same (deviceId, requestId) already exists and is completed, restore the original result.
            FaceCheckinRequest existingReq = requestMapper.selectOne(
                    new LambdaQueryWrapper<FaceCheckinRequest>()
                            .eq(FaceCheckinRequest::getDeviceId, dto.getDeviceId())
                            .eq(FaceCheckinRequest::getRequestId, dto.getRequestId())
                            .eq(FaceCheckinRequest::getMeetingReserveId, dto.getMeetingReserveId()));
            if (existingReq != null && existingReq.getStatus() != null && existingReq.getStatus() == 1) {
                int priorStatus = mapResultToStatus(existingReq.getResult());
                result.setStatus(priorStatus);
                result.setMessage(mapResultToMessage(existingReq.getResult()));
                result.setParticipantCategory(priorStatus == STATUS_NO_PERMISSION ? CATEGORY_NOT_MEMBER
                        : priorStatus == STATUS_NOT_RECOGNIZED ? CATEGORY_UNREGISTERED
                        : priorStatus == STATUS_SUCCESS || priorStatus == STATUS_ALREADY || priorStatus == STATUS_NOT_STARTED
                        ? CATEGORY_MEMBER : null);
                result.setMeetingReserveId(dto.getMeetingReserveId());
                if (verifyMeeting != null) { result.setMeetingSubject(verifyMeeting.getMeetingSubject()); }
                if (existingReq.getPersonId() != null) {
                    FaceCheckinPerson priorPerson = getById(existingReq.getPersonId());
                    if (priorPerson != null) {
                        result.setPersonName(priorPerson.getPersonName());
                        result.setEmployeeNo(priorPerson.getEmployeeNo());
                        result.setPersonType(priorPerson.getPersonType());
                    }
                }
                return result;
            }

            FaceCheckinRequest request = new FaceCheckinRequest();
            request.setRequestId(dto.getRequestId());
            request.setDeviceId(dto.getDeviceId());
            request.setMeetingReserveId(dto.getMeetingReserveId());
            request.setAcceptTime(LocalDateTime.now());
            request.setStatus(0);
            request.setCreateTime(LocalDateTime.now());
            requestMapper.insert(request);

            FaceCheckinPerson matched = matchPerson(dto.getPhotoBase64());
            if (matched == null) {
                FaceCheckinAttendance unregisteredEvent = matchUnregisteredPhoto(dto.getMeetingReserveId(), dto.getPhotoBase64());
                request.setStatus(1);
                request.setResult("NOT_RECOGNIZED");
                requestMapper.updateById(request);
                if (unregisteredEvent == null) {
                    saveUnregisteredAttendanceEvent(dto.getMeetingReserveId(), dto.getRequestId(), dto.getDeviceId(), dto.getPhotoBase64());
                    result.setStatus(STATUS_NOT_RECOGNIZED);
                    result.setMessage("首次识别到非记录人员");
                } else {
                    result.setStatus(STATUS_NOT_RECOGNIZED);
                    result.setMessage("非记录人员已记录");
                }
                result.setParticipantCategory(CATEGORY_UNREGISTERED);
                return result;
            }
            result.setPersonName(matched.getPersonName());
            result.setEmployeeNo(matched.getEmployeeNo());
            result.setPersonType(matched.getPersonType());
            result.setMeetingReserveId(dto.getMeetingReserveId());
            if (verifyMeeting != null) { result.setMeetingSubject(verifyMeeting.getMeetingSubject()); }

            FaceCheckinMeetingMember member = memberMapper.selectOne(
                    new LambdaQueryWrapper<FaceCheckinMeetingMember>()
                            .eq(FaceCheckinMeetingMember::getMeetingReserveId, dto.getMeetingReserveId())
                            .eq(FaceCheckinMeetingMember::getPersonId, matched.getId()));
            if (member == null || member.getStatus() == null || member.getStatus() != 1) {
                result.setStatus(STATUS_NO_PERMISSION);
                result.setMessage("无权限");
                request.setStatus(1);
                request.setResult("NO_PERMISSION");
                request.setPersonId(matched.getId());
                requestMapper.updateById(request);
                saveRecognitionEventOnce(dto.getMeetingReserveId(), matched.getId(), matched.getPersonName(),
                        STATUS_NO_PERMISSION, CATEGORY_NOT_MEMBER, "非与会人员", dto.getRequestId(), dto.getDeviceId(), dto.getPhotoBase64());
                result.setParticipantCategory(CATEGORY_NOT_MEMBER);
                return result;
            }

            FaceCheckinAttendance existing = attendanceMapper.selectOne(
                    new LambdaQueryWrapper<FaceCheckinAttendance>()
                            .eq(FaceCheckinAttendance::getMeetingReserveId, dto.getMeetingReserveId())
                            .eq(FaceCheckinAttendance::getPersonId, matched.getId())
                            .eq(FaceCheckinAttendance::getStatus, STATUS_SUCCESS));
            if (existing != null) {
                result.setStatus(STATUS_ALREADY);
                result.setMessage("已签到");
                result.setParticipantCategory(CATEGORY_MEMBER);
                request.setStatus(1);
                request.setResult("ALREADY_CHECKED_IN");
                request.setPersonId(matched.getId());
                requestMapper.updateById(request);
                result.setStatus(STATUS_ALREADY);
                result.setMessage("已签到");
                return result;
            }

            if (!checkTimeWindow(dto.getMeetingReserveId())) {
                result.setStatus(STATUS_NOT_STARTED);
                result.setMessage("签到未开始或已结束");
                request.setStatus(1);
                request.setResult("OUT_OF_TIME_WINDOW");
                request.setPersonId(matched.getId());
                requestMapper.updateById(request);
                saveRecognitionEventOnce(dto.getMeetingReserveId(), matched.getId(), matched.getPersonName(),
                        STATUS_NOT_STARTED, CATEGORY_MEMBER, "与会人员", dto.getRequestId(), dto.getDeviceId(), dto.getPhotoBase64());
                result.setParticipantCategory(CATEGORY_MEMBER);
                return result;
            }

            LocalDateTime now = LocalDateTime.now();
            MeetingReserve reserve = verifyMeeting;
            String remark = "";
            LocalDateTime attendanceEnd = reserve != null ? reserve.getAttendanceEndTime() : null;
            
            if (attendanceEnd != null && now.isAfter(attendanceEnd)) { remark = "迟到"; }
            FaceCheckinAttendance attendance = new FaceCheckinAttendance();
            attendance.setMeetingReserveId(dto.getMeetingReserveId());
            attendance.setPersonId(matched.getId());
            attendance.setPersonName(matched.getPersonName());
            attendance.setCheckinTime(now);
            attendance.setStatus(STATUS_SUCCESS);
            attendance.setRequestId(dto.getRequestId());
            attendance.setDeviceId(dto.getDeviceId());
            attendance.setPhotoPath(dto.getPhotoBase64());
            attendance.setRemark(remark);
            attendance.setParticipantCategory(CATEGORY_MEMBER);
            attendance.setCreateTime(now);
            attendanceMapper.insert(attendance);

            request.setStatus(1);
            request.setResult("SUCCESS");
            request.setPersonId(matched.getId());
            requestMapper.updateById(request);

            result.setStatus(STATUS_SUCCESS);
            result.setMessage("签到成功");
            result.setParticipantCategory(CATEGORY_MEMBER);
            return result;
        } catch (Exception e) {
            log.error("Face check-in verify failed", e);
            result.setStatus(STATUS_FAILED);
            result.setMessage("处理失败");
            return result;
        }
    }

    private void saveRecognitionEventOnce(String meetingReserveId, String personId, String personName, int status,
                                          int participantCategory, String categoryText, String requestId, String deviceId, String photoBase64) {
        FaceCheckinAttendance existing = attendanceMapper.selectOne(
                new LambdaQueryWrapper<FaceCheckinAttendance>()
                        .eq(FaceCheckinAttendance::getMeetingReserveId, meetingReserveId)
                        .eq(FaceCheckinAttendance::getPersonId, personId)
                        .eq(FaceCheckinAttendance::getStatus, status));
        if (existing != null) {
            return;
        }
        FaceCheckinAttendance event = new FaceCheckinAttendance();
        event.setMeetingReserveId(meetingReserveId);
        event.setPersonId(personId);
        event.setPersonName(personName);
        event.setCheckinTime(LocalDateTime.now());
        event.setStatus(status);
        event.setParticipantCategory(participantCategory);
        event.setRemark(categoryText);
        event.setRequestId(requestId);
        event.setDeviceId(deviceId);
        event.setPhotoPath(photoBase64);
        event.setCreateTime(LocalDateTime.now());
        attendanceMapper.insert(event);
    }

    private FaceCheckinAttendance matchUnregisteredPhoto(String meetingReserveId, String photoBase64) {
        if (StringUtils.isBlank(photoBase64)) {
            return null;
        }
        List<FaceCheckinAttendance> events = attendanceMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinAttendance>()
                        .eq(FaceCheckinAttendance::getMeetingReserveId, meetingReserveId)
                        .eq(FaceCheckinAttendance::getParticipantCategory, CATEGORY_UNREGISTERED));
        List<String> templates = events.stream().map(FaceCheckinAttendance::getPhotoPath).collect(Collectors.toList());
        int bestIndex = callInferenceBatchService(photoBase64, templates);
        return bestIndex >= 0 && bestIndex < events.size() ? events.get(bestIndex) : null;
    }

    private void saveUnregisteredAttendanceEvent(String meetingReserveId, String requestId, String deviceId, String photoBase64) {
        FaceCheckinAttendance event = new FaceCheckinAttendance();
        event.setMeetingReserveId(meetingReserveId);
        event.setPersonName("非记录人员");
        event.setCheckinTime(LocalDateTime.now());
        event.setStatus(STATUS_NOT_RECOGNIZED);
        event.setParticipantCategory(CATEGORY_UNREGISTERED);
        event.setRemark("非记录人员");
        event.setRequestId(requestId);
        event.setDeviceId(deviceId);
        event.setPhotoPath(photoBase64);
        event.setCreateTime(LocalDateTime.now());
        attendanceMapper.insert(event);
    }

    private int mapResultToStatus(String result) {
        if (result == null) { return STATUS_FAILED; }
        switch (result) {
            case "SUCCESS": return STATUS_SUCCESS;
            case "ALREADY_CHECKED_IN": return STATUS_ALREADY;
            case "NO_PERMISSION": return STATUS_NO_PERMISSION;
            case "NOT_RECOGNIZED": return STATUS_NOT_RECOGNIZED;
            case "OUT_OF_TIME_WINDOW": return STATUS_NOT_STARTED;
            default: return STATUS_FAILED;
        }
    }

    private String mapResultToMessage(String result) {
        if (result == null) { return "处理失败"; }
        switch (result) {
            case "SUCCESS": return "签到成功";
            case "ALREADY_CHECKED_IN": return "已签到";
            case "NO_PERMISSION": return "无权限";
            case "NOT_RECOGNIZED": return "未识别";
            case "OUT_OF_TIME_WINDOW": return "签到未开始或已结束";
            default: return "处理失败";
        }
    }

    private FaceCheckinPerson matchPerson(String photoBase64) {
        if (StringUtils.isBlank(photoBase64)) {
            return null;
        }
        /* 比对只需要 face_vector；photo_path（约 37KB/人）无关比对，排除避免每次刷脸全量加载。 */
        List<FaceCheckinPerson> persons = list(new LambdaQueryWrapper<FaceCheckinPerson>()
                .select(FaceCheckinPerson::getId,
                        FaceCheckinPerson::getPersonName,
                        FaceCheckinPerson::getEmployeeNo,
                        FaceCheckinPerson::getPersonType,
                        FaceCheckinPerson::getStatus,
                        FaceCheckinPerson::getAuditStatus,
                        FaceCheckinPerson::getFaceVector)
                .eq(FaceCheckinPerson::getStatus, 1)
                .eq(FaceCheckinPerson::getAuditStatus, 1));
        if (persons.isEmpty()) {
            return null;
        }
        List<String> templates = persons.stream().map(FaceCheckinPerson::getFaceVector).collect(Collectors.toList());
        int bestIndex = callInferenceBatchService(photoBase64, templates);
        if (bestIndex >= 0 && bestIndex < persons.size()) {
            return persons.get(bestIndex);
        }
        return null;
    }

    private int callInferenceBatchService(String photoBase64, List<String> templates) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            Map<String, Object> body = new java.util.HashMap<>();
            body.put("photoBase64", photoBase64);
            body.put("templates", templates);
            org.springframework.http.HttpEntity<Map<String, Object>> entity = new org.springframework.http.HttpEntity<>(body, headers);
            org.springframework.http.ResponseEntity<Map> response = new org.springframework.web.client.RestTemplate()
                    .postForEntity(INFERENCE_BATCH_URL, entity, Map.class);
            Map<?, ?> result = response.getBody();
            if (result != null && Boolean.TRUE.equals(result.get("success"))) {
                Object index = result.get("bestIndex");
                Object similarity = result.get("bestSimilarity");
                double bestSimilarity = similarity instanceof Number ? ((Number) similarity).doubleValue() : -1.0;
                log.info("Face batch match candidates={} bestIndex={} bestSimilarity={}",
                        templates.size(), index, bestSimilarity);
                if (bestSimilarity >= MATCH_THRESHOLD && index instanceof Number) {
                    return ((Number) index).intValue();
                }
            }
            log.info("Face batch match rejected reason={}", result != null ? result.get("reason") : "empty");
        } catch (Exception e) {
            log.warn("Face inference batch call failed: {}", e.getMessage());
        }
        return -1;
    }

    private String extractFaceFeature(String photoBase64) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            Map<String, String> body = new java.util.HashMap<>();
            body.put("photoBase64", photoBase64);
            org.springframework.http.HttpEntity<Map<String, String>> entity = new org.springframework.http.HttpEntity<>(body, headers);
            org.springframework.http.ResponseEntity<Map> response = new org.springframework.web.client.RestTemplate()
                    .postForEntity(INFERENCE_EXTRACT_URL, entity, Map.class);
            Map result = response.getBody();
            if (result == null || !Boolean.TRUE.equals(result.get("success"))) {
                String reason = result != null && result.get("reason") != null ? String.valueOf(result.get("reason")) : "人脸特征提取失败";
                throw new IllegalArgumentException(reason);
            }
            Object feature = result.get("featureBase64");
            if (!(feature instanceof String) || StringUtils.isBlank((String) feature)) {
                throw new IllegalArgumentException("人脸特征提取结果无效");
            }
            return (String) feature;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Face feature extraction failed", e);
            throw new IllegalArgumentException("人脸特征提取服务不可用");
        }
    }

    private boolean checkTimeWindow(String meetingReserveId) {
        MeetingReserve reserve = getActiveMeeting(meetingReserveId);
        if (reserve == null || reserve.getMeetingTimeStart() == null || reserve.getMeetingTimeEnd() == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(reserve.getMeetingTimeStart()) && !now.isAfter(reserve.getMeetingTimeEnd());
    }

    @Override
    public Boolean savePerson(FaceCheckinPersonDTO dto) {
        FaceCheckinPerson person;
        if (StringUtils.isNotBlank(dto.getId())) {
            person = getById(dto.getId());
            if (person == null) {
                return false;
            }
        } else {
            person = new FaceCheckinPerson();
            person.setStatus(1);
            person.setAuditStatus(0);
            person.setCreateTime(LocalDateTime.now());
        }
        person.setPersonName(dto.getPersonName());
        person.setEmployeeNo(dto.getEmployeeNo());
        person.setPhone(dto.getPhone());
        person.setPersonType(dto.getPersonType());
        if (StringUtils.isBlank(dto.getPhotoBase64())) {
            if (person.getId() == null) {
                throw new IllegalArgumentException("人脸照片不能为空");
            }
            /* 编辑时不重传照片：保留原照片与原特征，仅更新基础字段。 */
            person.setUpdateTime(LocalDateTime.now());
            return saveOrUpdate(person);
        }
        String faceFeature = extractFaceFeature(dto.getPhotoBase64());
        person.setPhotoPath(dto.getPhotoBase64());
        person.setFaceVector(faceFeature);
        person.setModelVersion("sface_2021dec");
        person.setUpdateTime(LocalDateTime.now());
        return saveOrUpdate(person);
    }

    @Override
    public PageInfo<FaceCheckinPersonVO> listPersons(int pageNum, int pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<FaceCheckinPerson> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(FaceCheckinPerson::getPersonName, keyword);
        }
        wrapper.select(
                FaceCheckinPerson::getId,
                FaceCheckinPerson::getPersonName,
                FaceCheckinPerson::getEmployeeNo,
                FaceCheckinPerson::getPhone,
                FaceCheckinPerson::getPersonType,
                FaceCheckinPerson::getStatus,
                FaceCheckinPerson::getAuditStatus,
                FaceCheckinPerson::getCreateTime);
        List<FaceCheckinPerson> list = list(wrapper.orderByDesc(FaceCheckinPerson::getCreateTime));
        /* list 实际是 PageHelper 的 Page，先包 PageInfo 保留 total/pages 等分页元数据，再替换为 VO 内容。 */
        PageInfo<FaceCheckinPerson> personPage = new PageInfo<>(list);
        List<FaceCheckinPersonVO> vos = list.stream().map(this::toPersonVO).collect(Collectors.toList());
        PageInfo<FaceCheckinPersonVO> voPage = new PageInfo<>(vos);
        voPage.setTotal(personPage.getTotal());
        voPage.setPages(personPage.getPages());
        voPage.setPageNum(personPage.getPageNum());
        voPage.setPageSize(personPage.getPageSize());
        return voPage;
    }

        @Override
    public String exportAttendanceCsv(String meetingReserveId) {
        MeetingReserve meeting = getActiveMeeting(meetingReserveId);
        List<FaceCheckinMeetingMember> members = memberMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinMeetingMember>()
                        .eq(FaceCheckinMeetingMember::getMeetingReserveId, meetingReserveId)
                        .orderByAsc(FaceCheckinMeetingMember::getCreateTime));
        List<FaceCheckinAttendance> events = attendanceMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinAttendance>()
                        .select(FaceCheckinAttendance::getId,
                                FaceCheckinAttendance::getMeetingReserveId,
                                FaceCheckinAttendance::getPersonId,
                                FaceCheckinAttendance::getPersonName,
                                FaceCheckinAttendance::getCheckinTime,
                                FaceCheckinAttendance::getStatus,
                                FaceCheckinAttendance::getRequestId,
                                FaceCheckinAttendance::getDeviceId,
                                FaceCheckinAttendance::getRemark,
                                FaceCheckinAttendance::getParticipantCategory,
                                FaceCheckinAttendance::getCreateTime)
                        .eq(FaceCheckinAttendance::getMeetingReserveId, meetingReserveId)
                        .orderByAsc(FaceCheckinAttendance::getCheckinTime));
        Map<String, FaceCheckinAttendance> attendanceByPerson = new java.util.LinkedHashMap<>();
        for (FaceCheckinAttendance event : events) {
            if (StringUtils.isBlank(event.getPersonId()) || attendanceByPerson.containsKey(event.getPersonId())) {
                continue;
            }
            attendanceByPerson.put(event.getPersonId(), event);
        }
        for (FaceCheckinAttendance event : events) {
            if (event.getStatus() != null && event.getStatus() == STATUS_SUCCESS
                    && StringUtils.isNotBlank(event.getPersonId())) {
                attendanceByPerson.put(event.getPersonId(), event);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\uFEFF");
        sb.append("会议主题,会议开始时间,会议结束时间,考勤开始时间,考勤截止时间\n");
        sb.append(csvEscape(meeting != null ? meeting.getMeetingSubject() : "")).append(",")
                .append(meeting != null && meeting.getMeetingTimeStart() != null ? meeting.getMeetingTimeStart().toString() : "").append(",")
                .append(meeting != null && meeting.getMeetingTimeEnd() != null ? meeting.getMeetingTimeEnd().toString() : "").append(",")
                .append(meeting != null && meeting.getAttendanceStartTime() != null ? meeting.getAttendanceStartTime().toString() : "").append(",")
                .append(meeting != null && meeting.getAttendanceEndTime() != null ? meeting.getAttendanceEndTime().toString() : "").append("\n");
        sb.append("姓名,工号,人员类型,人员ID,识别分类,签到时间,是否签到,是否迟到,签到状态,备注,设备ID,请求ID\n");
        java.util.Set<String> memberIds = members.stream()
                .map(FaceCheckinMeetingMember::getPersonId)
                .collect(Collectors.toSet());
        List<String> exportPersonIds = java.util.stream.Stream.concat(
                        members.stream().map(FaceCheckinMeetingMember::getPersonId),
                        events.stream().map(FaceCheckinAttendance::getPersonId).filter(StringUtils::isNotBlank))
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, FaceCheckinPerson> exportPersonMap = listPersonBriefMap(exportPersonIds);
        for (FaceCheckinMeetingMember member : members) {
            FaceCheckinAttendance attendance = attendanceByPerson.get(member.getPersonId());
            FaceCheckinPerson person = exportPersonMap.get(member.getPersonId());
            appendCsvAttendanceRow(sb, meeting, person,
                    attendance != null ? attendance.getParticipantCategory() : CATEGORY_MEMBER,
                    attendance, checkedIn(attendance), late(attendance, meeting));
        }

        for (FaceCheckinAttendance event : events) {
            boolean nonMember = event.getParticipantCategory() != null
                    && (event.getParticipantCategory() == CATEGORY_NOT_MEMBER || event.getParticipantCategory() == CATEGORY_UNREGISTERED);
            if (!nonMember || StringUtils.isNotBlank(event.getPersonId()) && memberIds.contains(event.getPersonId())) {
                continue;
            }
            FaceCheckinPerson person = exportPersonMap.get(event.getPersonId());
            appendCsvAttendanceRow(sb, meeting, person, event.getParticipantCategory(), event, false, false);
        }
        return sb.toString();
    }

    private boolean checkedIn(FaceCheckinAttendance attendance) {
        return attendance != null && attendance.getStatus() != null && attendance.getStatus() == STATUS_SUCCESS;
    }

    /**
     * 按 id 集合批量取人员（排除 photo_path/face_vector 两个 mediumtext 大字段）并构建 Map。
     * 轮询接口（attendance/request 列表）10s 一次高频调用，全列加载会把所有人员照片
     * 拉进 JVM；展示场景只需要姓名/工号/类型等基础字段。
     */
    private Map<String, FaceCheckinPerson> listPersonBriefMap(List<String> personIds) {
        if (personIds == null || personIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<String> distinctIds = personIds.stream().filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        if (distinctIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return list(new LambdaQueryWrapper<FaceCheckinPerson>()
                .select(FaceCheckinPerson::getId,
                        FaceCheckinPerson::getPersonName,
                        FaceCheckinPerson::getEmployeeNo,
                        FaceCheckinPerson::getPhone,
                        FaceCheckinPerson::getPersonType,
                        FaceCheckinPerson::getStatus,
                        FaceCheckinPerson::getAuditStatus,
                        FaceCheckinPerson::getCreateTime)
                .in(FaceCheckinPerson::getId, distinctIds))
                .stream()
                .collect(Collectors.toMap(FaceCheckinPerson::getId, Function.identity(), (a, b) -> a));
    }

    private boolean late(FaceCheckinAttendance attendance, MeetingReserve meeting) {
        return checkedIn(attendance) && meeting != null && meeting.getAttendanceEndTime() != null
                && attendance.getCheckinTime() != null
                && attendance.getCheckinTime().isAfter(meeting.getAttendanceEndTime());
    }

    private void appendCsvAttendanceRow(StringBuilder sb, MeetingReserve meeting, FaceCheckinPerson person,
                                        Integer participantCategory, FaceCheckinAttendance attendance,
                                        boolean checkedIn, boolean late) {
        sb.append(csvEscape(person != null ? person.getPersonName()
                : (attendance != null ? attendance.getPersonName() : ""))).append(",")
                // 工号/人员ID 等长数字列加 ' 前缀强制文本，避免 Excel 按数值显示丢精度（15 位后全变 0）。
                .append(csvText(person != null ? person.getEmployeeNo() : "")).append(",")
                .append(person != null && person.getPersonType() != null && person.getPersonType() == 1 ? "工作人员"
                        : (person != null ? "访客" : "未知")).append(",")
                .append(csvText(attendance != null ? attendance.getPersonId() : "")).append(",")
                .append(participantCategoryText(participantCategory)).append(",")
                .append(attendance != null && attendance.getCheckinTime() != null ? attendance.getCheckinTime().toString() : "").append(",")
                .append(checkedIn ? "已签到" : "未签到").append(",")
                .append(late ? "是" : (checkedIn ? "否" : "")).append(",")
                .append(attendance != null ? attendanceStatusText(attendance.getStatus()) : "未签到").append(",")
                .append(csvEscape(attendance != null ? attendance.getRemark() : "")).append(",")
                .append(csvText(attendance != null ? attendance.getDeviceId() : "")).append(",")
                .append(csvText(attendance != null ? attendance.getRequestId() : "")).append("\n");
    }

    private String attendanceStatusText(Integer status) {
        switch (status != null ? status : 0) {
            case 1: return "签到成功";
            case 2: return "重复签到";
            case 3: return "无权限";
            case 4: return "未识别";
            case 5: return "未开始";
            case 6: return "已结束";
            default: return "处理失败";
        }
    }

    private String participantCategoryText(Integer category) {
        switch (category != null ? category : CATEGORY_MEMBER) {
            case CATEGORY_MEMBER: return "与会人员";
            case CATEGORY_NOT_MEMBER: return "非与会人员";
            case CATEGORY_UNREGISTERED: return "非记录人员";
            default: return "未知";
        }
    }

    private String csvEscape(String value) {
        if (value == null) { return ""; }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String csvText(String value) {
        if (StringUtils.isBlank(value)) { return ""; }
        if ("=+-@".indexOf(value.charAt(0)) >= 0) {
            return csvEscape("'" + value);
        }
        // 纯数字且长度达到 Excel 双精度边界（15 位）时加 ' 前缀，防止 Excel 当数值显示丢精度。
        if (value.length() >= 15 && value.chars().allMatch(Character::isDigit)) {
            return csvEscape("'" + value);
        }
        return csvEscape(value);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean auditPerson(String personId, Integer auditStatus) {
        FaceCheckinPerson person = getById(personId);
        if (person == null) { return false; }
        person.setAuditStatus(auditStatus);
        person.setUpdateTime(LocalDateTime.now());
        return updateById(person);
    }

    @Override
    public List<FaceCheckinAbsentVO> listAbsent(String meetingReserveId) {
        List<FaceCheckinMeetingMember> members = memberMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinMeetingMember>()
                        .eq(FaceCheckinMeetingMember::getMeetingReserveId, meetingReserveId)
                        .eq(FaceCheckinMeetingMember::getStatus, 1));
        List<String> checkedInIds = attendanceMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinAttendance>()
                        .eq(FaceCheckinAttendance::getMeetingReserveId, meetingReserveId)
                        .eq(FaceCheckinAttendance::getStatus, STATUS_SUCCESS))
                .stream()
                .map(FaceCheckinAttendance::getPersonId)
                .collect(Collectors.toList());
        List<String> absentIds = members.stream()
                .map(FaceCheckinMeetingMember::getPersonId)
                .filter(personId -> !checkedInIds.contains(personId))
                .distinct()
                .collect(Collectors.toList());
        Map<String, FaceCheckinPerson> absentPersonMap = listPersonBriefMap(absentIds);
        return members.stream()
                .filter(m -> !checkedInIds.contains(m.getPersonId()))
                .map(m -> {
                    FaceCheckinAbsentVO vo = new FaceCheckinAbsentVO();
                    vo.setPersonId(m.getPersonId());
                    FaceCheckinPerson p = absentPersonMap.get(m.getPersonId());
                    vo.setPersonName(p != null ? p.getPersonName() : "");
                    vo.setLeaveStatus(m.getLeaveStatus());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FaceCheckinRequestVO> listRequests(String meetingReserveId) {
        /* VO 不含照片内容；实体 photoPath 也从未写入（verify 不落照片），投影直接排除该 mediumtext 列。 */
        List<FaceCheckinRequest> list = requestMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinRequest>()
                        .select(FaceCheckinRequest::getId,
                                FaceCheckinRequest::getRequestId,
                                FaceCheckinRequest::getDeviceId,
                                FaceCheckinRequest::getMeetingReserveId,
                                FaceCheckinRequest::getAcceptTime,
                                FaceCheckinRequest::getStatus,
                                FaceCheckinRequest::getResult,
                                FaceCheckinRequest::getPersonId,
                                FaceCheckinRequest::getCreateTime)
                        .eq(StringUtils.isNotBlank(meetingReserveId), FaceCheckinRequest::getMeetingReserveId, meetingReserveId)
                        .orderByDesc(FaceCheckinRequest::getCreateTime)
                        .last("LIMIT 100"));
        List<String> personIds = list.stream()
                .map(FaceCheckinRequest::getPersonId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, FaceCheckinPerson> personMap = listPersonBriefMap(personIds);
        return list.stream().map(request -> {
            FaceCheckinRequestVO vo = new FaceCheckinRequestVO();
            vo.setId(request.getId());
            vo.setRequestId(request.getRequestId());
            vo.setDeviceId(request.getDeviceId());
            vo.setMeetingReserveId(request.getMeetingReserveId());
            vo.setAcceptTime(request.getAcceptTime());
            vo.setStatus(request.getStatus());
            vo.setResult(request.getResult());
            vo.setPersonId(request.getPersonId());
            FaceCheckinPerson requestPerson = personMap.get(request.getPersonId());
            vo.setPersonName(requestPerson != null ? requestPerson.getPersonName() : null);
            vo.setEmployeeNo(requestPerson != null ? requestPerson.getEmployeeNo() : null);
            vo.setPersonType(requestPerson != null ? requestPerson.getPersonType() : null);
            vo.setCreateTime(request.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MeetingReserve> listMeetings() {
        return meetingReserveService.lambdaQuery()
                .eq(MeetingReserve::getDeleted, false)
                .orderByDesc(MeetingReserve::getMeetingTimeStart)
                .last("LIMIT 100")
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMeeting(FaceCheckinMeetingDTO dto) {
        validateMeetingTextLength(dto);
        LocalDateTime[] attendanceWindow = validateMeetingTimeWindow(dto);
        MeetingReserve meeting = new MeetingReserve();
        meeting.setEmployeeId("face-checkin");
        meeting.setMeetingType(1);
        meeting.setType(1);
        meeting.setMeetingSubject(dto.getMeetingSubject());
        meeting.setMeetingTimeStart(LocalDateTime.parse(dto.getMeetingTimeStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        meeting.setMeetingTimeEnd(LocalDateTime.parse(dto.getMeetingTimeEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        meeting.setAttendanceStartTime(attendanceWindow[0]);
        meeting.setAttendanceEndTime(attendanceWindow[1]);
        meeting.setRemark(dto.getRemark());
        List<String> memberIds = normalizeMemberIds(dto.getPersonIds());
        meeting.setMeetingPeopleNum(memberIds.size());
        meeting.setAttendees(String.join(",", memberIds));
        meeting.setCreateTime(LocalDateTime.now());
        meeting.setDeleted(false);
        Boolean saved = meetingReserveService.save(meeting);
        if (saved) {
            FaceCheckinMemberDTO memberDTO = new FaceCheckinMemberDTO();
            memberDTO.setMeetingReserveId(meeting.getId());
            memberDTO.setPersonIds(memberIds);
            saveMembers(memberDTO);
        }
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMeeting(FaceCheckinMeetingDTO dto) {
        if (StringUtils.isBlank(dto.getId())) {
            throw new IllegalArgumentException("会议ID不能为空");
        }
        MeetingReserve meeting = getActiveMeeting(dto.getId());
        if (meeting == null || Boolean.TRUE.equals(meeting.getDeleted())) {
            throw new IllegalArgumentException("会议不存在");
        }
        validateMeetingTextLength(dto);
        LocalDateTime[] attendanceWindow = validateMeetingTimeWindow(dto);
        meeting.setMeetingSubject(dto.getMeetingSubject());
        meeting.setMeetingTimeStart(LocalDateTime.parse(dto.getMeetingTimeStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        meeting.setMeetingTimeEnd(LocalDateTime.parse(dto.getMeetingTimeEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        meeting.setAttendanceStartTime(attendanceWindow[0]);
        meeting.setAttendanceEndTime(attendanceWindow[1]);
        meeting.setRemark(dto.getRemark());
        List<String> memberIds = normalizeMemberIds(dto.getPersonIds());
        meeting.setMeetingPeopleNum(memberIds.size());
        meeting.setAttendees(String.join(",", memberIds));
        meeting.setUpdateTime(LocalDateTime.now());
        Boolean updated = meetingReserveService.updateById(meeting);
        if (updated) {
            FaceCheckinMemberDTO memberDTO = new FaceCheckinMemberDTO();
            memberDTO.setMeetingReserveId(meeting.getId());
            memberDTO.setPersonIds(memberIds);
            saveMembers(memberDTO);
        }
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMeeting(String meetingId) {
        MeetingReserve meeting = getActiveMeeting(meetingId);
        if (meeting == null || Boolean.TRUE.equals(meeting.getDeleted())) {
            throw new IllegalArgumentException("会议不存在");
        }
        meeting.setDeleted(true);
        meeting.setUpdateTime(LocalDateTime.now());
        return meetingReserveService.updateById(meeting);
    }
    private List<String> normalizeMemberIds(List<String> personIds) {
        if (personIds == null) {
            return Collections.emptyList();
        }
        return personIds.stream().filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
    }

    /** 主题/备注长度对齐 mee_meeting_reserve 表约束（meeting_subject varchar(30)、remark varchar(255)）。 */
    private void validateMeetingTextLength(FaceCheckinMeetingDTO dto) {
        if (dto.getMeetingSubject() != null && dto.getMeetingSubject().length() > 30) {
            throw new IllegalArgumentException("会议主题最多30字");
        }
        if (dto.getRemark() != null && dto.getRemark().length() > 255) {
            throw new IllegalArgumentException("备注最多255字");
        }
    }

    private LocalDateTime[] validateMeetingTimeWindow(FaceCheckinMeetingDTO dto) {
        LocalDateTime meetingStart = LocalDateTime.parse(dto.getMeetingTimeStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime meetingEnd = LocalDateTime.parse(dto.getMeetingTimeEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (meetingEnd.isBefore(meetingStart)) {
            throw new IllegalArgumentException("结束时间不能早于开始时间");
        }
        LocalDateTime attendanceStart = parseDateTime(dto.getAttendanceStartTime(), meetingStart);
        LocalDateTime attendanceEnd = parseDateTime(dto.getAttendanceEndTime(), meetingEnd);
        if (attendanceStart.isBefore(meetingStart) || attendanceEnd.isAfter(meetingEnd) || attendanceEnd.isBefore(attendanceStart)) {
            return new LocalDateTime[]{meetingStart, meetingEnd};
        }
        return new LocalDateTime[]{attendanceStart, attendanceEnd};
    }

    private MeetingReserve getActiveMeeting(String meetingReserveId) {
        if (StringUtils.isBlank(meetingReserveId)) {
            return null;
        }
        return meetingReserveService.lambdaQuery()
                .eq(MeetingReserve::getId, meetingReserveId)
                .eq(MeetingReserve::getDeleted, false)
                .one();
    }

    private LocalDateTime parseDateTime(String value, LocalDateTime fallback) {
        if (StringUtils.isBlank(value)) { return fallback; }
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private FaceCheckinPersonVO toPersonVO(FaceCheckinPerson person) {
        FaceCheckinPersonVO vo = new FaceCheckinPersonVO();
        vo.setId(person.getId());
        vo.setPersonName(person.getPersonName());
        vo.setEmployeeNo(person.getEmployeeNo());
        vo.setPhone(person.getPhone());
        vo.setPersonType(person.getPersonType());

        vo.setStatus(person.getStatus());
        vo.setAuditStatus(person.getAuditStatus());
        vo.setCreateTime(person.getCreateTime());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMembers(FaceCheckinMemberDTO dto) {
        MeetingReserve meeting = getActiveMeeting(dto.getMeetingReserveId());
        if (meeting == null) {
            throw new IllegalArgumentException("会议不存在或已删除");
        }
        List<String> memberIds = normalizeMemberIds(dto.getPersonIds());
        if (!memberIds.isEmpty()) {
            /* 校验需要 status/auditStatus，列表页轮询不需要照片与特征，统一走精简投影。 */
            Map<String, FaceCheckinPerson> personMap = listPersonBriefMap(memberIds);
            List<String> invalidMemberIds = memberIds.stream()
                    .filter(personId -> {
                        FaceCheckinPerson person = personMap.get(personId);
                        return person == null || person.getStatus() == null || person.getStatus() != 1
                                || person.getAuditStatus() == null || person.getAuditStatus() != 1;
                    })
                    .collect(Collectors.toList());
            if (!invalidMemberIds.isEmpty()) {
                throw new IllegalArgumentException("仅允许添加已启用且审核通过的人员");
            }
        }
        memberMapper.delete(new LambdaQueryWrapper<FaceCheckinMeetingMember>()
                .eq(FaceCheckinMeetingMember::getMeetingReserveId, dto.getMeetingReserveId()));
        for (String personId : memberIds) {
            FaceCheckinMeetingMember member = new FaceCheckinMeetingMember();
            member.setMeetingReserveId(dto.getMeetingReserveId());
            member.setPersonId(personId);
            member.setStatus(1);
            member.setLeaveStatus(0);
            member.setCreateTime(LocalDateTime.now());
            memberMapper.insert(member);
        }
        return true;
    }

    @Override
    public List<FaceCheckinPersonVO> listMembers(String meetingReserveId) {
        if (StringUtils.isBlank(meetingReserveId)) {
            return Collections.emptyList();
        }
        List<FaceCheckinMeetingMember> members = memberMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinMeetingMember>()
                        .eq(FaceCheckinMeetingMember::getMeetingReserveId, meetingReserveId)
                        .orderByAsc(FaceCheckinMeetingMember::getCreateTime));
        List<String> personIds = members.stream()
                .map(FaceCheckinMeetingMember::getPersonId)
                .distinct()
                .collect(Collectors.toList());
        Map<String, FaceCheckinPerson> personMap = listPersonBriefMap(personIds);
        return personIds.stream()
                .map(personMap::get)
                .filter(person -> person != null)
                .map(this::toPersonVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FaceCheckinAttendanceVO> listAttendance(String meetingReserveId) {
        /* VO 无 photo 字段，photo_path 是 mediumtext 大字段，排除投影避免轮询接口每次全量拉照片。 */
        List<FaceCheckinAttendance> list = attendanceMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinAttendance>()
                        .select(FaceCheckinAttendance::getId,
                                FaceCheckinAttendance::getMeetingReserveId,
                                FaceCheckinAttendance::getPersonId,
                                FaceCheckinAttendance::getPersonName,
                                FaceCheckinAttendance::getCheckinTime,
                                FaceCheckinAttendance::getStatus,
                                FaceCheckinAttendance::getRequestId,
                                FaceCheckinAttendance::getDeviceId,
                                FaceCheckinAttendance::getRemark,
                                FaceCheckinAttendance::getParticipantCategory,
                                FaceCheckinAttendance::getCreateTime)
                        .eq(FaceCheckinAttendance::getMeetingReserveId, meetingReserveId)
                        .orderByDesc(FaceCheckinAttendance::getCheckinTime));
        /* 一次批量取齐涉及的人员，避免逐条 getById 的 N+1 查询。 */
        List<String> personIds = list.stream()
                .map(FaceCheckinAttendance::getPersonId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, FaceCheckinPerson> personMap = listPersonBriefMap(personIds);
        return list.stream().map(event -> toAttendanceVO(event, personMap)).collect(Collectors.toList());
    }

    private FaceCheckinAttendanceVO toAttendanceVO(FaceCheckinAttendance attendance, Map<String, FaceCheckinPerson> personMap) {
        FaceCheckinAttendanceVO vo = new FaceCheckinAttendanceVO();
        vo.setId(attendance.getId());
        vo.setMeetingReserveId(attendance.getMeetingReserveId());
        vo.setPersonId(attendance.getPersonId());
        vo.setPersonName(attendance.getPersonName());
        FaceCheckinPerson attendancePerson = StringUtils.isNotBlank(attendance.getPersonId())
                ? personMap.get(attendance.getPersonId()) : null;
        vo.setEmployeeNo(attendancePerson != null ? attendancePerson.getEmployeeNo() : null);
        vo.setPersonType(attendancePerson != null ? attendancePerson.getPersonType() : null);
        vo.setRequestId(attendance.getRequestId());
        vo.setDeviceId(attendance.getDeviceId());
        vo.setCheckinTime(attendance.getCheckinTime());
        vo.setStatus(attendance.getStatus());
        vo.setRemark(attendance.getRemark());
        vo.setParticipantCategory(attendance.getParticipantCategory());
        vo.setCreateTime(attendance.getCreateTime());
        return vo;
    }
}















