package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.device.service.task.MeterConsumptionRecordServiceTask;
import com.crlink.smartpark.device.service.task.MeterConsumptionTask;
import com.crlink.smartpark.device.service.task.MeterReadingRecordServiceTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.Constants.TIME_FORMATTER;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestMeterController {
    @Autowired
    private MeterReadingRecordServiceTask meterReadingRecordServiceTask;

    @Autowired
    private MeterConsumptionRecordServiceTask meterConsumptionRecordServiceTask;

    @Autowired
    private MeterConsumptionTask meterConsumptionTask;


    @GetMapping("/readingRecordTask")
    public void readingRecordTask(@RequestParam("readingDate") LocalDate readingDate) {
        MeterReadingRecordServiceTask.READING_DATE_TL.set(readingDate);
        meterReadingRecordServiceTask.generateMeterReadingRecord();
    }

    @GetMapping("/consumptionRecordServiceTask")
    public void consumptionRecordServiceTask(@RequestParam("time") LocalDateTime time) {
        MeterConsumptionRecordServiceTask.NOW_TIME_TL.set(time);
        meterConsumptionRecordServiceTask.generateMeterConsumptionRecord();
    }

    @GetMapping("/meterConsumptionTask")
    public void meterConsumptionTask(@RequestParam("time") LocalDate time) {
        MeterConsumptionTask.NOW_TIME_TL.set(time);
        meterConsumptionTask.generateMeterConsumption();
    }

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.of(2022, 11, 1, 0, 0, 10);
        String format = time.format(TIME_FORMATTER);
        System.out.println(format);
    }
}
