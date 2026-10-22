package com.crlink.smartpark.meeting.task;

import com.crlink.smartpark.core.utils.MeetingScreenUtils;
import com.crlink.smartpark.device.feign.DeviceInfoProvider;
import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.entity.ReserveAudit;
import com.crlink.smartpark.meeting.service.MeetingManageService;
import com.crlink.smartpark.meeting.service.ReserveAuditService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_TIME_MINUTES_FORMAT;

/**
 * @ClassName: MeetingAuditTask
 * @Package com.crlink.smartpark.meeting.task
 * @Description: 会议室预约定时任务处理
 * @Version 1.0
 */
@Service
@Slf4j
public class MeetingAuditTask {

    @Resource
    private ReserveAuditService reserveAuditService;

    @Autowired
    private MeetingManageService meetingManageService;

    @Autowired
    private DeviceInfoProvider deviceInfoProvider;


    /**
     * 会议室预约 - 超时未审核自动取消 - 释放会议室
     *
     * @return void
     * @MethodName autoCancelReserve
     */
    public void autoCancelReserve() {
        log.info("超时自动取消任务启动！");
        int count = reserveAuditService.autoCancelReserve();
        log.info("超时自动取消任务结束！共调整 {} 条数据。", count);
    }

    /**
     * 1、空闲中：会议室处于“开启”状态，且当前无会议，且未来30分钟也无会议预定信息，显示为“空闲中”状态。信息屏灯光为“绿色”.
     * 2、使用中：会议室处于“开启”状态，且当前有进行中会议，显示为“使用中”状态。信息屏灯光为“红色".
     * 3、即将开始：会议室处于“开启”状态，且当前无会议，但未来30分钟有会议预定信息，显示为“即将开始”状态。信息屏灯光为“蓝色"
     * 4、暂停使用：会议室处于“关闭”状态，显示为“暂停使用:状态.信息屏灯光关闭
     *
     * @See com.crlink.smartpark.meeting.service.impl.MeetingManageServiceImpl.changeStatusMeetingManage
     */
    public void controlScreenLight() {
        log.info("会议室信息屏灯光控制任务开始！");
        List<ReserveAudit> meetings = queryMeeting();
        LocalTime now = LocalTime.now();
        Map<String, String> map = getMeetingScreenDeviceId();
        log.info("会议室和设备映射：{}", map);
        //所有安装了会议室信息屏的会议室
        Set<String> meetingIds = map.keySet();
        for (ReserveAudit meeting : meetings) {
            meetingIds.remove(meeting.getMeetingId());
            String deviceId = map.get(meeting.getMeetingId());
            LocalTime startTime = LocalTime.parse(meeting.getStartTime(), DateTimeFormatter.ofPattern(DEFAULT_TIME_MINUTES_FORMAT));
            LocalTime endTime = LocalTime.parse(meeting.getEndTime(), DateTimeFormatter.ofPattern(DEFAULT_TIME_MINUTES_FORMAT));
            if (Objects.nonNull(deviceId)) {
                if (startTime.isBefore(now.plusSeconds(90)) && startTime.isAfter(now.minusSeconds(90))) {
                    log.info("当前会议室处于使用中,deviceId:{}", deviceId);
                    MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.red, 90);
                } else if (startTime.isAfter(now)) {
                    log.info("当前会议室即将开始,deviceId:{}", deviceId);
                    MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.blue, 90);
                } else if (endTime.isBefore(now.plusSeconds(90)) && endTime.isAfter(now.minusSeconds(90))) {
                    log.info("当前会议室处于空闲,deviceId:{}", deviceId);
                    MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.green, 90);
                }
            }
        }
        for (String meetingId : meetingIds) {
            String deviceId = map.get(meetingId);
            log.info("没有被预定审核表记录的会议室设备id，会议室处于空闲中,deviceId:{}", deviceId);
            MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.green, 90);
        }
        log.info("会议室信息屏灯光控制任务结束！");
    }

    /**
     * 会议室审核表中有记录
     *
     * @return
     */
    public List<ReserveAudit> queryMeeting() {
        LocalTime now = LocalTime.now();
        List<ReserveAudit> meetings = reserveAuditService.lambdaQuery().select(ReserveAudit::getMeetingId, ReserveAudit::getStartTime, ReserveAudit::getEndTime)
                .eq(ReserveAudit::getNowDate, LocalDate.now())
                .nested(q -> q.nested(r -> r.ge(ReserveAudit::getStartTime, now.minusMinutes(5)).le(ReserveAudit::getStartTime, now.plusMinutes(30)))
                        .or(t -> t.ge(ReserveAudit::getEndTime, now.minusMinutes(5)).le(ReserveAudit::getEndTime, now.plusMinutes(5))))
                .list();
        return meetings;

    }

    private Map<String, String> getMeetingScreenDeviceId() {
        Map<String, String> meetingId2ScreenDeviceId = meetingManageService.lambdaQuery().select(MeetingManage::getId, MeetingManage::getScreenDeviceId)
                .isNotNull(MeetingManage::getScreenDeviceId)
                .eq(MeetingManage::getStatus, 1)
                .list()
                .parallelStream().collect(Collectors.toMap(MeetingManage::getId, MeetingManage::getScreenDeviceId));
        Map<String, String> deviceIdByIds = deviceInfoProvider.getDeviceIdByIds(Sets.newHashSet(meetingId2ScreenDeviceId.values()));
        Map<String, String> result = new HashMap<>();
        meetingId2ScreenDeviceId.forEach((k, v) -> {
            result.put(k, deviceIdByIds.get(v));
        });
        return result;
    }

    public static void main(String[] args) {
        LocalTime startTime = LocalTime.parse("20:00", DateTimeFormatter.ofPattern(DEFAULT_TIME_MINUTES_FORMAT));
        System.out.println(startTime);
    }


}
