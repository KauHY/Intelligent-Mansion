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
    private static final String INFERENCE_MATCH_FEATURE_URL = System.getenv().getOrDefault(
            "FACE_INFERENCE_MATCH_FEATURE_URL", INFERENCE_URL.replaceFirst("/match/?$", "/match-feature"));

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
    @Transactional(rollbackFor = Exception.class)
    public FaceCheckinResultVO verify(FaceCheckinVerifyDTO dto) {
        FaceCheckinResultVO result = new FaceCheckinResultVO();
        result.setRequestId(dto.getRequestId());
        result.setMeetingReserveId(dto.getMeetingReserveId());
        MeetingReserve verifyMeeting = meetingReserveService.getById(dto.getMeetingReserveId());
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
                MeetingReserve priorMeeting = meetingReserveService.getById(dto.getMeetingReserveId());
                if (priorMeeting != null) { result.setMeetingSubject(priorMeeting.getMeetingSubject()); }
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
            MeetingReserve currentMeeting = meetingReserveService.getById(dto.getMeetingReserveId());
            if (currentMeeting != null) { result.setMeetingSubject(currentMeeting.getMeetingSubject()); }

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
            MeetingReserve reserve = meetingReserveService.getById(dto.getMeetingReserveId());
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
        FaceCheckinAttendance bestEvent = null;
        double bestSimilarity = 0.0;
        for (FaceCheckinAttendance event : events) {
            if (StringUtils.isBlank(event.getPhotoPath())) {
                continue;
            }
            if (photoBase64.equals(event.getPhotoPath())) {
                return event;
            }
            double similarity = callInferenceService(photoBase64, event.getPhotoPath());
            if (similarity > bestSimilarity) {
                bestSimilarity = similarity;
                bestEvent = event;
            }
        }
        return bestEvent != null && bestSimilarity >= MATCH_THRESHOLD ? bestEvent : null;
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
        List<FaceCheckinPerson> persons = list(new LambdaQueryWrapper<FaceCheckinPerson>()
                .eq(FaceCheckinPerson::getStatus, 1)
                .eq(FaceCheckinPerson::getAuditStatus, 1));
        if (persons.isEmpty()) {
            return null;
        }
        FaceCheckinPerson bestMatch = null;
        double bestSimilarity = 0.0;
        for (FaceCheckinPerson person : persons) {
            if (StringUtils.isBlank(person.getFaceVector())) {
                continue;
            }
            double similarity = comparePhotoToTemplate(photoBase64, person.getFaceVector());
            log.info("Face candidate personId={} personName={} similarity={}",
                    person.getId(), person.getPersonName(), similarity);
            if (similarity > bestSimilarity) {
                bestSimilarity = similarity;
                bestMatch = person;
            }
        }
        log.info("Face match summary candidates={} bestSimilarity={} matched={}",
                persons.size(), bestSimilarity, bestMatch != null && bestSimilarity >= MATCH_THRESHOLD);
        if (bestMatch != null && bestSimilarity >= MATCH_THRESHOLD) {
            return bestMatch;
        }
        return null;
    }

    private double comparePhotoToTemplate(String photoBase64, String faceVector) {
        if (StringUtils.isBlank(faceVector)) {
            return -1.0;
        }
        return faceVector.startsWith("data:image/")
                ? callInferenceService(photoBase64, faceVector)
                : callInferenceFeatureService(photoBase64, faceVector);
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

    private double callInferenceFeatureService(String photoBase64, String featureBase64) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            Map<String, String> body = new java.util.HashMap<>();
            body.put("photoBase64", photoBase64);
            body.put("templateFeatureBase64", featureBase64);
            org.springframework.http.HttpEntity<Map<String, String>> entity = new org.springframework.http.HttpEntity<>(body, headers);
            org.springframework.http.ResponseEntity<Map> response = new org.springframework.web.client.RestTemplate()
                    .postForEntity(INFERENCE_MATCH_FEATURE_URL, entity, Map.class);
            Map result = response.getBody();
            if (result != null && Boolean.TRUE.equals(result.get("success"))) {
                Object sim = result.get("similarity");
                double similarity = sim instanceof Number ? ((Number) sim).doubleValue() : -1.0;
                log.info("Face feature match faceCount={} similarity={}", result.get("faceCount1"), similarity);
                return similarity;
            }
            log.info("Face feature match rejected reason={}", result != null ? result.get("reason") : "empty");
            return -1.0;
        } catch (Exception e) {
            log.warn("Face feature match call failed: {}", e.getMessage());
            return -1.0;
        }
    }

    /**
     * Call the Python face inference microservice to compare two base64 images.
     * Returns similarity (0.0-1.0), or -1.0 on failure.
     */
    private double callInferenceService(String photoBase64, String templateBase64) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            Map<String, String> body = new java.util.HashMap<>();
            body.put("photoBase64", photoBase64);
            body.put("templateBase64", templateBase64);
            org.springframework.http.HttpEntity<Map<String, String>> entity = new org.springframework.http.HttpEntity<>(body, headers);
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            org.springframework.http.ResponseEntity<Map> response = restTemplate.postForEntity(
                    INFERENCE_URL, entity, Map.class);
            Map result = response.getBody();
            if (result != null && Boolean.TRUE.equals(result.get("success"))) {
                Object sim = result.get("similarity");
                double similarity = sim instanceof Number ? ((Number) sim).doubleValue() : -1.0;
                log.info("Face legacy photo match faceCount1={} faceCount2={} similarity={}",
                        result.get("faceCount1"), result.get("faceCount2"), similarity);
                return similarity;
            }
            log.info("Face legacy photo match rejected reason={}", result != null ? result.get("reason") : "empty");
            return -1.0;
        } catch (Exception e) {
            log.warn("Face inference service call failed: {}", e.getMessage());
            return -1.0;
        }
    }

    /**
     * Compute cosine similarity between two face feature vectors (base64 encoded doubles).
     * Returns -1.0 if either vector is empty or dimension mismatch.
     */
    private double cosineSimilarity(String vectorA, String vectorB) {
        if (StringUtils.isBlank(vectorA) || StringUtils.isBlank(vectorB)) {
            return -1.0;
        }
        try {
            byte[] bytesA = java.util.Base64.getDecoder().decode(vectorA);
            byte[] bytesB = java.util.Base64.getDecoder().decode(vectorB);
            int len = Math.min(bytesA.length, bytesB.length) / 8;
            if (len == 0) { return -1.0; }
            java.nio.ByteBuffer bufA = java.nio.ByteBuffer.wrap(bytesA);
            java.nio.ByteBuffer bufB = java.nio.ByteBuffer.wrap(bytesB);
            double dotProduct = 0.0;
            double normA = 0.0;
            double normB = 0.0;
            for (int i = 0; i < len; i++) {
                double a = bufA.getDouble();
                double b = bufB.getDouble();
                dotProduct += a * b;
                normA += a * a;
                normB += b * b;
            }
            if (normA == 0 || normB == 0) { return -1.0; }
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        } catch (Exception e) {
            log.warn("cosineSimilarity error: {}", e.getMessage());
            return -1.0;
        }
    }

    private boolean checkTimeWindow(String meetingReserveId) {
        MeetingReserve reserve = meetingReserveService.getById(meetingReserveId);
        if (reserve == null || reserve.getMeetingTimeStart() == null || reserve.getMeetingTimeEnd() == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(reserve.getMeetingTimeStart()) && !now.isAfter(reserve.getMeetingTimeEnd());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            throw new IllegalArgumentException("人脸照片不能为空");
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
        List<FaceCheckinPerson> list = list(wrapper.orderByDesc(FaceCheckinPerson::getCreateTime));
        List<FaceCheckinPersonVO> vos = list.stream().map(this::toPersonVO).collect(Collectors.toList());
        return new PageInfo<>(vos);
    }

        @Override
    public String exportAttendanceCsv(String meetingReserveId) {
        MeetingReserve meeting = meetingReserveService.getById(meetingReserveId);
        List<FaceCheckinMeetingMember> members = memberMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinMeetingMember>()
                        .eq(FaceCheckinMeetingMember::getMeetingReserveId, meetingReserveId)
                        .orderByAsc(FaceCheckinMeetingMember::getCreateTime));
        List<FaceCheckinAttendance> events = attendanceMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinAttendance>()
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
        for (FaceCheckinMeetingMember member : members) {
            FaceCheckinAttendance attendance = attendanceByPerson.get(member.getPersonId());
            FaceCheckinPerson person = getById(member.getPersonId());
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
            FaceCheckinPerson person = StringUtils.isNotBlank(event.getPersonId()) ? getById(event.getPersonId()) : null;
            appendCsvAttendanceRow(sb, meeting, person, event.getParticipantCategory(), event, false, false);
        }
        return sb.toString();
    }

    private boolean checkedIn(FaceCheckinAttendance attendance) {
        return attendance != null && attendance.getStatus() != null && attendance.getStatus() == STATUS_SUCCESS;
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
                .append(csvEscape(person != null ? person.getEmployeeNo() : "")).append(",")
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
        return csvEscape("=\"" + value + "\"");
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
        return members.stream()
                .filter(m -> !checkedInIds.contains(m.getPersonId()))
                .map(m -> {
                    FaceCheckinAbsentVO vo = new FaceCheckinAbsentVO();
                    vo.setPersonId(m.getPersonId());
                    FaceCheckinPerson p = getById(m.getPersonId());
                    vo.setPersonName(p != null ? p.getPersonName() : "");
                    vo.setLeaveStatus(m.getLeaveStatus());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FaceCheckinRequestVO> listRequests(String meetingReserveId) {
        List<FaceCheckinRequest> list = requestMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinRequest>()
                        .eq(StringUtils.isNotBlank(meetingReserveId), FaceCheckinRequest::getMeetingReserveId, meetingReserveId)
                        .orderByDesc(FaceCheckinRequest::getCreateTime)
                        .last("LIMIT 100"));
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
            FaceCheckinPerson requestPerson = StringUtils.isNotBlank(request.getPersonId()) ? getById(request.getPersonId()) : null;
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
                .orderByDesc(MeetingReserve::getMeetingTimeStart)
                .last("LIMIT 100")
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMeeting(FaceCheckinMeetingDTO dto) {
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
        MeetingReserve meeting = meetingReserveService.getById(dto.getId());
        if (meeting == null || Boolean.TRUE.equals(meeting.getDeleted())) {
            throw new IllegalArgumentException("会议不存在");
        }
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
        MeetingReserve meeting = meetingReserveService.getById(meetingId);
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
        vo.setPhotoUrl(person.getPhotoPath());
        vo.setStatus(person.getStatus());
        vo.setAuditStatus(person.getAuditStatus());
        vo.setCreateTime(person.getCreateTime());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMembers(FaceCheckinMemberDTO dto) {
        memberMapper.delete(new LambdaQueryWrapper<FaceCheckinMeetingMember>()
                .eq(FaceCheckinMeetingMember::getMeetingReserveId, dto.getMeetingReserveId()));
        if (dto.getPersonIds() != null) {
            for (String personId : dto.getPersonIds()) {
                FaceCheckinMeetingMember member = new FaceCheckinMeetingMember();
                member.setMeetingReserveId(dto.getMeetingReserveId());
                member.setPersonId(personId);
                member.setStatus(1);
                member.setLeaveStatus(0);
                member.setCreateTime(LocalDateTime.now());
                memberMapper.insert(member);
            }
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
        return members.stream()
                .map(member -> getById(member.getPersonId()))
                .filter(person -> person != null)
                .map(this::toPersonVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FaceCheckinAttendanceVO> listAttendance(String meetingReserveId) {
        List<FaceCheckinAttendance> list = attendanceMapper.selectList(
                new LambdaQueryWrapper<FaceCheckinAttendance>()
                        .eq(FaceCheckinAttendance::getMeetingReserveId, meetingReserveId)
                        .orderByDesc(FaceCheckinAttendance::getCheckinTime));
        return list.stream().map(this::toAttendanceVO).collect(Collectors.toList());
    }

    private FaceCheckinAttendanceVO toAttendanceVO(FaceCheckinAttendance attendance) {
        FaceCheckinAttendanceVO vo = new FaceCheckinAttendanceVO();
        vo.setId(attendance.getId());
        vo.setMeetingReserveId(attendance.getMeetingReserveId());
        vo.setPersonId(attendance.getPersonId());
        vo.setPersonName(attendance.getPersonName());
        FaceCheckinPerson attendancePerson = StringUtils.isNotBlank(attendance.getPersonId()) ? getById(attendance.getPersonId()) : null;
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















