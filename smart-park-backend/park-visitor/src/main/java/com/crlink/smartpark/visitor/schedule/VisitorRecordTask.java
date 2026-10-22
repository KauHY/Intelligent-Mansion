package com.crlink.smartpark.visitor.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 访客记录刷新
 * 每天23:59:59执行一次
 *
 */
@Slf4j
@Component
public class VisitorRecordTask {

    @Resource
    private VisitorRecordService visitorRecordService;
    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    public void refreshSignOutStatus() {
        log.info("------------VisitorRecordTaskStart-----------");
        QueryWrapper<VisitorRecord> qw = new QueryWrapper<>();
        qw.select("id", "device_person_id");
        qw.eq("sign_out_time", LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        qw.eq("process_status", DictUtil.getValue(DictConstant.ProcessStatus.FIVE));
        List<VisitorRecord> records = visitorRecordService.list(qw);
        log.info("VisitorRecordTask QuerySize: {}", records.size());
        if (CollUtil.isNotEmpty(records)) {
            for (VisitorRecord record : records) {
                if (record.getDevicePersonId() == null) {
                    continue;
                }
                try {
                    String errMsg = deviceOperateProvider.delPerson(record.getDevicePersonId());
                    if (StrUtil.isBlank(errMsg)) {
                        log.info("移除访客{}权限成功", record.getId());
                    } else {
                        log.error("移除访客{}权限失败，{}", record.getId(), errMsg);
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            log.info("------------VisitorRecordTaskEnd-----------");
        }
    }

}
