package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.feign.ContractProvider;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.dto.MeterConsumptionCountDTO;
import com.crlink.smartpark.device.dto.MeterConsumptionRecordDTO;
import com.crlink.smartpark.device.dto.MeterDTO;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.entity.MeterConsumptionRecord;
import com.crlink.smartpark.device.mapper.MeterConsumptionRecordMapper;
import com.crlink.smartpark.device.model.excel.ConsumptionExcelExportDTO;
import com.crlink.smartpark.device.model.excel.MeterConsumptionRecordExportDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionAnalysisReqDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionRecordQuery;
import com.crlink.smartpark.device.model.query.MeterQuery;
import com.crlink.smartpark.device.service.*;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.YEAR_MONTH_FORMAT;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.ELECTRICITY_METER;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.WATER_METER;
import static com.crlink.smartpark.device.common.MeterConstant.CONSUMPTION_RECORD_EXPORT_FILENAME;


/**
 * 水电表用量记录 服务实现类
 * */
@Service
public class MeterConsumptionRecordServiceImpl extends ServiceImpl<MeterConsumptionRecordMapper, MeterConsumptionRecord> implements MeterConsumptionRecordService {

    @Autowired
    private MeterService meterService;

    @Autowired
    private MonthReadingService monthReadingService;

    @Autowired
    private DayReadingService dayReadingService;

    @Autowired
    private HouseProvider houseProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private MeterReadingRecordService meterReadingRecordService;

    @Autowired
    private MeterContractHouseRelationService meterContractHouseRelationService;


    /**
     * 换表时把换表的水电表用量同步到用量表
     *
     * @param meter
     * @param oldReading
     */
    @Override
    public void exchangeMeterSaveConsumptionRecord(DeviceInfo meter, BigDecimal oldReading) {
        LocalDate now = LocalDate.now();
        String thisMonth = now.format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT));
        String lastMonth = now.minusMonths(1).format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT));
        List<MeterConsumptionRecord> list = this.lambdaQuery().eq(MeterConsumptionRecord::getMeterId, meter.getId())
                .in(MeterConsumptionRecord::getSettledMonth, thisMonth, lastMonth)
                .orderByDesc(MeterConsumptionRecord::getSettledMonth).list();
        MeterConsumptionRecord newRecord;
        if (CollectionUtil.isNotEmpty(list)) {
            MeterConsumptionRecord lastRecord = list.get(0);
            newRecord = MyBeanUtils.copy(lastRecord, MeterConsumptionRecord::new);
            newRecord.setLastReading(lastRecord.getThisReading());
            newRecord.setLastTime(lastRecord.getThisTime());
        } else {
            MeterDTO meterDTO = meterService.entitiesToDTOs(Lists.newArrayList(meter)).get(0);
            newRecord = MyBeanUtils.copy(meterDTO, MeterConsumptionRecord::new);
            newRecord.setMeterId(meter.getId());
            newRecord.setLastReading(meterDTO.getInitialReading());
            newRecord.setLastTime(meterDTO.getInitialTime());
        }
        newRecord.setThisReading(oldReading);
        newRecord.setThisTime(LocalDateTime.now());
        newRecord.setConsumption(NumberUtil.sub(newRecord.getThisReading(), newRecord.getLastReading()));
        newRecord.setSettledMonth(thisMonth);
        this.save(newRecord);
    }


    @Override
    @Id2objHandle
    public MeterConsumptionRecordDTO getMeterConsumptionRecordById(String id) {
        MeterConsumptionRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeterDTO meterDTO = meterService.getMeterById(entity.getMeterId());
        MeterConsumptionRecordDTO dto = MyBeanUtils.copy(entity, MeterConsumptionRecordDTO::new);
        BeanUtil.copyProperties(meterDTO, dto);
        return dto;
    }

    @Override
    public Map<String, MeterConsumptionRecord> listByMeterIds(Collection<String> meterIds, String settledMonth) {
        Map<String, MeterConsumptionRecord> result = this.lambdaQuery().in(MeterConsumptionRecord::getMeterId, meterIds)
                //自用表或公摊表
                .in(MeterConsumptionRecord::getEffectProperty, 1, 2)
                .eq(MeterConsumptionRecord::getSettledMonth, settledMonth)
                .list().parallelStream().collect(Collectors.toMap(MeterConsumptionRecord::getMeterId, Function.identity()));
        return result;
    }


    @Override
    @Id2objHandle
    public PageInfo<MeterConsumptionRecordDTO> listMeterConsumptionRecordPage(MeterConsumptionRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.setParkOrgIds(parkOrgIds);
        query.startPage();
        List<MeterConsumptionRecord> list = this.list(getConditionWrapper(query));
        List<MeterConsumptionRecordDTO> result = MyBeanUtils.copyList(list, MeterConsumptionRecordDTO::new);
        return query.createPageInfo(result);
    }


    private LambdaQueryWrapper<MeterConsumptionRecord> getConditionWrapper(MeterConsumptionRecordQuery query) {
        LambdaQueryWrapper<MeterConsumptionRecord> wrapper = new LambdaQueryWrapper();
        wrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeterConsumptionRecord::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StringUtils.isNotBlank(query.getBuildingId()), MeterConsumptionRecord::getBuildingId, query.getBuildingId());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), MeterConsumptionRecord::getDeviceName, query.getDeviceName());
        wrapper.eq(StringUtils.isNotBlank(query.getSettledMonth()), MeterConsumptionRecord::getSettledMonth, query.getSettledMonth());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceId()), MeterConsumptionRecord::getDeviceId, query.getDeviceId());
        if (StringUtils.isNotBlank(query.getHouseId())) {
            String sql = new StringBuilder("JSON_CONTAINS(").append("house_ids,").append("JSON_QUOTE('").append(query.getHouseId()).append("'))").toString();
            wrapper.apply(sql);
        }
        if (Objects.nonNull(query.getFloor())) {
            String sql = new StringBuilder(" JSON_CONTAINS(").append("floors,").append(query.getFloor()).append("))").toString();
            wrapper.apply(sql);
        }
        wrapper.in(MeterConsumptionRecord::getParkOrgId, query.getParkOrgIds());
        return wrapper;
    }


    @Override
    public void consumptionRecordDownload(MeterConsumptionRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        query.setParkOrgIds(parkOrgIds);
        Map<Integer, String> dictTypeMaps = DictUtil.getLabels("ELECTRICITY_METER_EFFECT_TYPE");
        ExcelExportUtils.export(
                () -> this.list(getConditionWrapper(query)),
                entities -> MyBeanUtils.copyList(entities, MeterConsumptionRecordExportDTO::new, (s, t) -> {
                    if (s.getDeviceType().equals(WATER_METER.type)) {
                        t.setEffectType(WATER_METER.desc);
                    } else if (CollectionUtil.isNotEmpty(dictTypeMaps)) {
                        t.setEffectType(dictTypeMaps.get(s.getEffectType()));
                    }
                }),
                CONSUMPTION_RECORD_EXPORT_FILENAME,
                MeterConsumptionRecordExportDTO.class);
    }

    @Override
    public Collection<MeterConsumptionCountDTO> listMeterConsumptionAnalysis(MeterConsumptionAnalysisReqDTO query) {
        MeterQuery meterQuery = MyBeanUtils.copy(query, MeterQuery::new);
        Collection<String> meterDeviceId = meterService.getMeterDeviceIdByCondition(meterQuery);
        List<MeterConsumptionCountDTO> result = queryConsumptionMeterReadingRecord(meterDeviceId, query.getYear(), query.getMonth());
        return result;
    }


    @Override
    public void downloadAnalysis(MeterConsumptionAnalysisReqDTO query) {
        Collection<MeterConsumptionCountDTO> list = this.listMeterConsumptionAnalysis(query);
        String fileName;
        String sheetName;
        String time = Objects.nonNull(query.getMonth()) ? "日期" : "月份";
        DateTimeFormatter dateTimeFormatter = Objects.nonNull(query.getMonth()) ? DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT)
                : DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT);
        List<List<String>> header = Lists.newArrayList();
        header.add(Lists.newArrayList(time));
        if (query.getDeviceType() == ELECTRICITY_METER.type) {
            header.add(Lists.newArrayList("用电量(kW·h)"));
            fileName = "用电量";
            sheetName = "用电量";

        } else {
            header.add(Lists.newArrayList("用水量(吨)"));
            fileName = "用水量";
            sheetName = "用水量";
        }
        List<ConsumptionExcelExportDTO> exportDTOS = list.parallelStream().map(t -> {
            ConsumptionExcelExportDTO exportDTO = new ConsumptionExcelExportDTO();
            exportDTO.setConsumption(t.getConsumption());
            exportDTO.setTime(t.getTime().format(dateTimeFormatter));
            return exportDTO;
        }).collect(Collectors.toList());
        ExcelExportUtils.export(exportDTOS, fileName, sheetName, header);
    }


    private List<MeterConsumptionCountDTO> queryConsumptionMeterReadingRecord(Collection<String> meterDeviceIds, int year, Integer month) {
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.of(0, 0, 0);
        List<MeterConsumptionCountDTO> result = Lists.newArrayList();
        //按天统计
        if (Objects.nonNull(month)) {
            LocalDate date = LocalDate.of(year, month, 1);
            for (int i = 1; i <= date.plusMonths(1).minusDays(1).getDayOfMonth(); i++) {
                LocalDate localDate = date.withDayOfMonth(i);
                if (localDate.equals(today)) {
                    return result;
                }
                BigDecimal dayCount = CollectionUtil.isEmpty(meterDeviceIds) ? BigDecimal.ZERO :
                        dayReadingService.accumulatedDayCount(localDate, localDate.plusDays(1), meterDeviceIds);
                result.add(new MeterConsumptionCountDTO(dayCount, LocalDateTime.of(localDate, time)));
            }
            //按月份统计
        } else {
            month = year == today.getYear() ? today.getMonthValue() : 12;
            for (int i = 1; i <= month; i++) {
                BigDecimal monthCount;
                LocalDate startDate = LocalDate.of(year, i, 1);
                if (CollectionUtil.isEmpty(meterDeviceIds)) {
                    monthCount = BigDecimal.ZERO;
                } else {
                    if (year == today.getYear() && i == today.getMonthValue()) {
                        monthCount = dayReadingService.accumulatedDayCount(startDate, startDate.plusMonths(1), meterDeviceIds);
                    } else {
                        LocalDateTime startTime = LocalDateTime.of(LocalDate.of(year, i, 1), time);
                        monthCount = monthReadingService.accumulatedMonthCount(startTime, startTime.plusMonths(1), meterDeviceIds);
                    }

                }
                result.add(new MeterConsumptionCountDTO(monthCount, LocalDateTime.of(startDate, time)));
            }
        }
        return result;
    }


}
