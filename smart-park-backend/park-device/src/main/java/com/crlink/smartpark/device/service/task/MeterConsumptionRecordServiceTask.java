package com.crlink.smartpark.device.service.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.crlink.smartpark.contract.feign.ContractProvider;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.device.entity.MeterConsumptionRecord;
import com.crlink.smartpark.device.entity.MeterContractHouseRelation;
import com.crlink.smartpark.device.entity.MeterReadingRecord;
import com.crlink.smartpark.device.model.ContractHouseInfo;
import com.crlink.smartpark.device.service.MeterConsumptionRecordService;
import com.crlink.smartpark.device.service.MeterContractHouseRelationService;
import com.crlink.smartpark.device.service.MeterReadingRecordService;
import com.crlink.smartpark.system.dto.house.HouseDTO;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.YEAR_MONTH_FORMAT;

@Service
public class MeterConsumptionRecordServiceTask {

    @Autowired
    private MeterConsumptionRecordService meterConsumptionRecordService;

    @Autowired
    private MeterReadingRecordService meterReadingRecordService;

    @Autowired
    private MeterContractHouseRelationService meterContractHouseRelationService;


    @Autowired
    private HouseProvider houseProvider;

    @Autowired
    private ContractProvider contractProvider;

    public static ThreadLocal<LocalDateTime> NOW_TIME_TL = new ThreadLocal<>();

    /**
     * 定时任务,生成水电表用量记录,及生成水电表关联的合同房屋，每月1日计算上个月的的水表表的水电用量，计算完成后再计算房屋企业的用电量
     */
    @Transactional
    public void generateMeterConsumptionRecord() {
        //用于手动触发
        LocalDateTime nowTime = NOW_TIME_TL.get();
        if (Objects.nonNull(nowTime)) {
            NOW_TIME_TL.remove();
        } else {
            nowTime = LocalDateTime.now();
        }
        LocalDateTime settlementMonth = nowTime.minusDays(5);
        String settledMonth = settlementMonth.format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT));
        String lastMonth = settlementMonth.minusMonths(1).format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT));
        Map<String, MeterConsumptionRecord> lastMonthRecordMap = meterConsumptionRecordService.lambdaQuery().select(MeterConsumptionRecord::getMeterId, MeterConsumptionRecord::getThisReading, MeterConsumptionRecord::getThisTime)
                .eq(MeterConsumptionRecord::getSettledMonth, lastMonth).list()
                .parallelStream().collect(Collectors.toMap(MeterConsumptionRecord::getMeterId, Function.identity()));
        Map<String, MeterReadingRecord> map = meterReadingRecordService.lambdaQuery()
                .ge(MeterReadingRecord::getReadingTime, settlementMonth.withDayOfMonth(1))
                .le(MeterReadingRecord::getReadingTime, settlementMonth.plusMonths(1).withDayOfMonth(1).minusDays(1))
                .list().parallelStream().collect(Collectors.groupingBy(MeterReadingRecord::getMeterId,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(MeterReadingRecord::getReadingTime)), Optional::get)));
        List<MeterConsumptionRecord> meterConsumptionRecords = Lists.newArrayList();
        List<MeterContractHouseRelation> meterContractHouseRelations = Lists.newArrayList();
        ContractHouseInfo contractHouseInfo = getContractHouseInfo();
        for (MeterReadingRecord meterReadingRecord : map.values()) {
            MeterConsumptionRecord consumptionRecord = MyBeanUtils.copy(meterReadingRecord, MeterConsumptionRecord::new, "id");
            MeterConsumptionRecord lastConsumptionRecord = lastMonthRecordMap.get(meterReadingRecord.getMeterId());
            BigDecimal lastReading;
            LocalDateTime lastTime;
            //首先去找上次记录
            if (lastConsumptionRecord == null) {
                lastConsumptionRecord = meterConsumptionRecordService.lambdaQuery().select(MeterConsumptionRecord::getThisReading, MeterConsumptionRecord::getThisTime)
                        .eq(MeterConsumptionRecord::getMeterId, meterReadingRecord.getMeterId()).orderByDesc(MeterConsumptionRecord::getThisTime).last("limit 1").one();
            }
            if (lastConsumptionRecord != null) {
                lastReading = lastConsumptionRecord.getThisReading();
                lastTime = lastConsumptionRecord.getThisTime();
            } else {
                lastReading = meterReadingRecord.getInitialReading();
                lastTime = meterReadingRecord.getInitialTime();
            }
            consumptionRecord.setSettledMonth(settledMonth);
            consumptionRecord.setLastReading(lastReading);
            consumptionRecord.setLastTime(lastTime);
            consumptionRecord.setThisReading(meterReadingRecord.getCorrectedReading() == null ? meterReadingRecord.getMeterReading() : meterReadingRecord.getCorrectedReading());
            consumptionRecord.setThisTime(meterReadingRecord.getReadingTime());
            consumptionRecord.setConsumption(NumberUtil.sub(consumptionRecord.getThisReading(), consumptionRecord.getLastReading()));
            meterConsumptionRecords.add(consumptionRecord);
            //自用表计或公摊表计才在之后计算合同房屋用量
            if (Sets.newHashSet(1, 2).contains(meterReadingRecord.getEffectProperty())) {
                Set<String> contractHouseIds = getContractHouseIds(meterReadingRecord.getBuildingId(), meterReadingRecord.getFloors(), meterReadingRecord.getHouseIds(), contractHouseInfo);
                for (String contractHouseId : contractHouseIds) {
                    meterContractHouseRelations.add(new MeterContractHouseRelation(settledMonth, meterReadingRecord.getMeterId(), contractHouseId));
                }
            }
        }
        meterContractHouseRelationService.saveBatch(meterContractHouseRelations);
        meterConsumptionRecordService.saveBatch(meterConsumptionRecords);
    }


    private ContractHouseInfo getContractHouseInfo() {
        Set<String> effectiveHouseIds = contractProvider.getEffectiveHouseIds();
        Map<String, HouseDTO> byHouseId = houseProvider.getHouseByHouseId(effectiveHouseIds, Sets.newHashSet("building_id", "floor"));
        Map<String, Set<String>> map = Maps.newHashMap();
        byHouseId.forEach((key, value) -> {
            map.compute(buildKey(value.getBuildingId(), value.getFloor()), (k, v) -> {
                if (v == null) {
                    v = Sets.newHashSet(key);
                } else {
                    v.add(key);
                }
                return v;
            });
        });
        return new ContractHouseInfo(effectiveHouseIds, map);
    }


    private Set<String> getContractHouseIds(String buildingId, List<Integer> floors, List<String> houseIds, ContractHouseInfo houseInfo) {
        Collection<String> tmpSet = null;
        Set<String> result = Sets.newHashSet();
        if (CollectionUtil.isNotEmpty(houseIds)) {
            tmpSet = CollectionUtil.intersection(houseInfo.getHouseIds(), houseIds);
        }
        if (CollectionUtil.isNotEmpty(floors)) {
            for (Integer floor : floors) {
                Map<String, Set<String>> map = houseInfo.getBuildingFloorHouseMap();
                tmpSet = map.get(buildKey(buildingId, floor));
            }
        }
        if (tmpSet != null) {
            result.addAll(tmpSet);
        }
        return result;
    }

    private String buildKey(String buildingId, Integer floor) {
        return buildingId + "_" + floor;
    }
}
