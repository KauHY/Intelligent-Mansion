package com.crlink.smartpark.device.service.task;

import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.device.dto.MeterDTO;
import com.crlink.smartpark.device.entity.DayReading;
import com.crlink.smartpark.device.entity.MeterReadingRecord;
import com.crlink.smartpark.device.service.DayReadingService;
import com.crlink.smartpark.device.service.MeterReadingRecordService;
import com.crlink.smartpark.device.service.MeterService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MeterReadingRecordServiceTask {

    public static ThreadLocal<LocalDate> READING_DATE_TL = new ThreadLocal<>();
    @Autowired
    private MeterReadingRecordService meterReadingRecordService;

    @Autowired
    private MeterService meterService;


    @Autowired
    private DayReadingService dayReadingService;

    /**
     * 生成抄表记录，每月28日进行抄表
     */
    @Transactional
    public void generateMeterReadingRecord() {
        //用于手动触发
        LocalDate readingDate = READING_DATE_TL.get();
        if (Objects.nonNull(readingDate)) {
            READING_DATE_TL.remove();
        } else {
            readingDate = LocalDate.now();
        }
        // 从28号计算
        LocalDate customDate = LocalDate.of(readingDate.getYear(), readingDate.getMonthValue(), 28);
        List<DayReading> dayReadings = dayReadingService.queryMaxAllRecord(customDate.minusMonths(1), customDate);
        Map<String, DayReading> readingMap = dayReadings.stream().collect(Collectors.toMap(DayReading::getDeviceId, Function.identity()));
        Map<String, MeterDTO> meterDTOMap = meterService.queryMeterByDeviceIds(readingMap.keySet());
        List entities = Lists.newArrayList();
        for (DayReading dayReading : dayReadings) {
            MeterDTO meterDTO = meterDTOMap.get(dayReading.getDeviceId());
            // 有些设备不在登记表中
            if (Objects.isNull(meterDTO)) {
                continue;
            }
            MeterReadingRecord record = MyBeanUtils.copy(meterDTO, MeterReadingRecord::new, "id");
            record.setMeterReading(dayReading.getEndReadingNum());
            record.setMeterId(meterDTO.getId());
            record.setReadingTime(dayReading.getReadingDateTime());
            entities.add(record);
        }
        meterReadingRecordService.saveBatch(entities);
    }

}
