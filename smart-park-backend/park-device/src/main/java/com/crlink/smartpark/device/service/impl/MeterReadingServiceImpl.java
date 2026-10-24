package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.device.controller.response.*;
import com.crlink.smartpark.device.dto.MeterDTO;
import com.crlink.smartpark.device.dto.MeterInfoDTO;
import com.crlink.smartpark.device.entity.DayReading;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.entity.HourReading;
import com.crlink.smartpark.device.entity.MonthReading;
import com.crlink.smartpark.device.service.*;
import com.crlink.smartpark.system.feign.BuildingProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.ELECTRICITY_METER;
import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.WATER_METER;

@Slf4j
@Service
public class MeterReadingServiceImpl implements MeterReadingService {

    @Autowired
    private MeterService meterService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private DayReadingService dayReadingService;

    @Autowired
    private MonthReadingService monthReadingService;

    @Autowired
    private HourReadingService hourReadingService;

    @Autowired
    private BuildingProvider buildingProvider;

    @Autowired
    private MeterSummaryStatisticsService meterSummaryStatisticsService;


    @Override
    public MeterReadingStatisticsVO getMeterReadingStatistics(String parkOrgId, LocalDate localDate) {
        Set<String> deviceIds = queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type);
        LocalDate newYearDay = LocalDate.of(localDate.getYear(), 1, 1);
        BigDecimal thisYear = accumulatedCount(newYearDay, localDate, deviceIds);
        BigDecimal lastYear = accumulatedCount(newYearDay.minusYears(1), newYearDay.minusDays(1), deviceIds);

        BigDecimal thisMonth = accumulatedCount(localDate.withDayOfMonth(1), localDate, deviceIds);
        BigDecimal lastMonth = accumulatedCount(localDate.minusMonths(1).withDayOfMonth(1), localDate.withDayOfMonth(1).minusDays(1), deviceIds);
        BigDecimal lastYearMonth = accumulatedCount(localDate.minusYears(1).withDayOfMonth(1), localDate.minusYears(1).plusMonths(1).withDayOfMonth(1).minusDays(1), deviceIds);


        BigDecimal thisDay = BigDecimal.ZERO;
        BigDecimal lastDay = BigDecimal.ZERO;
        BigDecimal lastYearDay = BigDecimal.ZERO;


        MeterReadingStatisticsVO vo = new MeterReadingStatisticsVO();
        vo.setYearCount(thisYear);
        vo.setYearPercent(lastYear == BigDecimal.ZERO ? BigDecimal.ONE : NumberUtil.div(thisYear.subtract(lastYear), lastYear, 4));

        vo.setMonthCount(thisMonth);
        vo.setLastMonthPercent(lastMonth == BigDecimal.ZERO ? BigDecimal.ONE : NumberUtil.div(thisMonth.subtract(lastMonth), lastMonth, 4));
        vo.setLastYearMonthPercent(lastYearMonth == BigDecimal.ZERO ? BigDecimal.ONE : NumberUtil.div(thisMonth.subtract(lastYearMonth), lastYearMonth, 4));

        vo.setDayCount(thisDay);
        vo.setLastDayPercent(lastDay == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(thisDay.subtract(lastDay), lastDay, 4));
        vo.setLastYearDayPercent(lastYearDay == BigDecimal.ZERO ? BigDecimal.ONE : NumberUtil.div(thisDay.subtract(lastYearDay), lastYearDay, 4));

        return vo;
    }

    //@Override
    //public MeterReadingTotalStatisticsVO getMeterReadingTotalStatistics(String parkOrgId) {
    //    Set<String> eMeterIds = queryMeterIds(parkOrgId, ELECTRICITY_METER.code);
    //    Set<String> wMeterIds = queryMeterIds(parkOrgId, WATER_METER.code);
    //    LocalDate now = LocalDate.now();
    //    LocalDate newYearDay = LocalDate.of(now.getYear(), 1, 1);
    //    BigDecimal thisYearElectricity = accumulatedCount(newYearDay, now, eMeterIds);
    //    BigDecimal thisYearWater = accumulatedCount(newYearDay, now, wMeterIds);
    //
    //    MeterReadingTotalStatisticsVO vo = new MeterReadingTotalStatisticsVO();
    //    vo.setYearElectricityCount(thisYearElectricity);
    //    vo.setYearWaterCount(thisYearWater);
    //    //vo.setCarbonEmission();
    //    return vo;
    //}
    @Override
    public MeterReadingTotalStatisticsVO getMeterReadingTotalStatistics(String parkOrgId) {
        Set<String> eDeviceIds = queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type);
        Set<String> wDeviceIds = queryMeterDeviceIds(parkOrgId, WATER_METER.type);
        StatisticsVO consumption = getThisYearConsumption(eDeviceIds, wDeviceIds);

        MeterReadingTotalStatisticsVO vo = new MeterReadingTotalStatisticsVO();
        vo.setYearElectricityCount(consumption.getElectricityCount());
        vo.setYearWaterCount(consumption.getWaterCount());
        BigDecimal e = NumberUtil.mul(vo.getYearElectricityCount(), 0.785);
        BigDecimal w = NumberUtil.mul(vo.getYearWaterCount(), 0.91);
        vo.setCarbonEmission(NumberUtil.mul(e.add(w), 1000));
        return vo;
    }

    @Override
    public List<MeterReadingTrendVO> getMeterReadingTrend(String parkOrgId, Integer timeType) {
        log.info(">>>>>>>>>>>>>>能耗趋势<<<<<<<<<<<<<<<<<<<<");
        LocalDate today = LocalDate.now();
        // 查询房间用电电表
        Set<String> eRoomMeterIds = queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type, 1);
        // 查询空调用电电表
        Set<String> eAirMeterIds = queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type, 2);
        // 查询所有电表
        Set<String> allMeterIds = queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type);
        // 公共用电=所有电表-房间用电-空调用电
        Collection<String> ePublicMeterIds = firstCollectionDiff(allMeterIds, eRoomMeterIds, eAirMeterIds);
        // 查询所有水表
        Set<String> wMeterIds = queryMeterDeviceIds(parkOrgId, WATER_METER.type);
        log.info(">>>>>>>>>>>eRoomMeterIds:{}<<<,eAirMeterIds:{},ePublicMeterIds:{},wMeterIds:{}", eRoomMeterIds, eAirMeterIds, ePublicMeterIds, wMeterIds);
        List<MeterReadingTrendVO> result = Lists.newArrayList();
        if (timeType == YEAR_TYPE) {
            /*
            LocalDate startDate = today.minusMonths(12).withDayOfMonth(1);
            List<StatisticsVO> roomCount = getLast12MonthConsumption(eRoomMeterIds, wMeterIds);
            List<StatisticsVO> airCount = getLast12MonthConsumption(eAirMeterIds, null);
            List<StatisticsVO> publicCount = getLast12MonthConsumption(ePublicMeterIds, null);
            roomCount.add(getThisMonthConsumption(eRoomMeterIds, wMeterIds));
            airCount.add(getThisMonthConsumption(eRoomMeterIds, null));
            publicCount.add(getThisMonthConsumption(eRoomMeterIds, null));
            for (int i = 0; i < roomCount.size(); i++) {
                MeterReadingTrendVO vo = new MeterReadingTrendVO();
                vo.setDate(startDate.plusMonths(i));
                vo.setERoom(roomCount.get(i).getElectricityCount());
                vo.setEAir(airCount.get(i).getElectricityCount());
                vo.setEPublic(publicCount.get(i).getElectricityCount());
                vo.setWater(roomCount.get(i).getWaterCount());
                vo.setETotal(NumberUtil.add(vo.getERoom(), vo.getEAir(), vo.getEPublic()));
                if (i >= 1) {
                    BigDecimal lastE = result.get(i - 1).getETotal();
                    BigDecimal lastW = result.get(i - 1).getWater();
                    vo.setELastMonthPercent(lastE == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(vo.getETotal().subtract(lastE), lastE, 4));
                    vo.setWLastMonthPercent(lastW == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(vo.getWater().subtract(lastW), lastW, 4));
                }
                result.add(vo);
            }
            result.remove(0);
            */
            return meterSummaryStatisticsService.summaryByYear(eRoomMeterIds, eAirMeterIds, wMeterIds, ePublicMeterIds);
        } else if (timeType == MONTH_TYPE) {
            /*
            List<StatisticsVO> roomCount = getLast14DayConsumption(eRoomMeterIds, wMeterIds);
            List<StatisticsVO> airCount = getLast14DayConsumption(eAirMeterIds, null);
            List<StatisticsVO> publicCount = getLast14DayConsumption(ePublicMeterIds, null);
            LocalDate startDate = today.minusDays(14);
            for (int i = 0; i < 14; i++) {
                MeterReadingTrendVO vo = new MeterReadingTrendVO();
                vo.setDate(startDate.plusDays(i));
                vo.setERoom(roomCount.get(i).getElectricityCount());
                vo.setEAir(airCount.get(i).getElectricityCount());
                vo.setEPublic(publicCount.get(i).getElectricityCount());
                vo.setWater(roomCount.get(i).getWaterCount());
                vo.setETotal(NumberUtil.add(vo.getERoom(), vo.getEAir(), vo.getEPublic()));
                result.add(vo);
            }
            */
            return meterSummaryStatisticsService.summaryByMonth(eRoomMeterIds, eAirMeterIds, wMeterIds, ePublicMeterIds);
        } else if (timeType == DAY_TYPE) {
            /*
            List<StatisticsVO> roomCount = getLast12HourConsumption(eRoomMeterIds, wMeterIds);
            List<StatisticsVO> airCount = getLast12HourConsumption(eAirMeterIds, null);
            List<StatisticsVO> publicCount = getLast12HourConsumption(ePublicMeterIds, null);
            LocalDateTime startTime = LocalDateTime.now().minusHours(12);
            for (int i = 0; i < 12; i++) {
                MeterReadingTrendVO vo = new MeterReadingTrendVO();
                vo.setDate(startTime.plusHours(i));
                vo.setERoom(roomCount.get(i).getElectricityCount());
                vo.setEAir(airCount.get(i).getElectricityCount());
                vo.setEPublic(publicCount.get(i).getElectricityCount());
                vo.setWater(roomCount.get(i).getWaterCount());
                vo.setETotal(NumberUtil.add(vo.getERoom(), vo.getEAir(), vo.getEPublic()));
                result.add(vo);
            }
            */
            return meterSummaryStatisticsService.summaryByDay(eRoomMeterIds, eAirMeterIds, wMeterIds, ePublicMeterIds);
        }
        return result;
    }

    @Override
    @Id2objHandle
    public List<MeterDistributionVO> getDistribution(String parkOrgId, Integer type) {
        List<DeviceInfo> deviceInfos = deviceInfoService.lambdaQuery().select(DeviceInfo::getDeviceId, DeviceInfo::getExtension, DeviceInfo::getDeviceType)
                .eq(DeviceInfo::getParkOrgId, parkOrgId)
                .in(DeviceInfo::getDeviceType, WATER_METER.type, ELECTRICITY_METER.type)
                .list();
        List<MeterDTO> meterDTOS = meterService.entitiesToDTOs(deviceInfos);
        Map<String, Map<Integer, Set<String>>> buildingIdCategoryMeterIdMap = meterDTOS.parallelStream()
                .collect(Collectors.groupingBy(MeterDTO::getBuildingId,
                        Collectors.groupingBy(MeterDTO::getDeviceType, Collectors.mapping(MeterDTO::getDeviceId, Collectors.toSet()))));
        Map<Object, String> buildings = buildingProvider.listBuildingDictByParkOrgId(parkOrgId).getData().parallelStream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        return getDistribution(type, buildingIdCategoryMeterIdMap, buildings);
    }

    @Override
    public ReadingStatisticsVO getMonthConsumption(String parkOrgId, LocalDate localDate, Integer type) {
        Set<String> deviceIds = queryMeterDeviceIds(parkOrgId, type);
        BigDecimal thisMonthCount = accumulatedCount(localDate.withDayOfMonth(1), localDate, deviceIds);
        BigDecimal lastMonthCount = accumulatedCount(localDate.minusMonths(1).withDayOfMonth(1), localDate.withDayOfMonth(1).minusDays(1), deviceIds);
        BigDecimal lastYearMonthCount = accumulatedCount(localDate.minusYears(1).withDayOfMonth(1), localDate.minusMonths(11).withDayOfMonth(1).minusDays(1), deviceIds);
        BigDecimal yOnYRate = lastYearMonthCount == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(NumberUtil.sub(thisMonthCount, lastYearMonthCount), lastYearMonthCount, 4);
        BigDecimal mOnMRate = lastMonthCount == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(NumberUtil.sub(thisMonthCount, lastMonthCount), lastMonthCount, 4);
        ReadingStatisticsVO statisticsVO = new ReadingStatisticsVO(thisMonthCount, yOnYRate, mOnMRate);
        return statisticsVO;
    }

    @Override
    public List<ElectricityConsumptionVO> getMonthElectricityConsumption(String parkOrgId, LocalDate localDate) {
        Set<String> eRoomMeterIds = queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type, 1);
        Set<String> eAirMeterIds = queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type, 2);
        Collection<String> ePublicMeterIds = firstCollectionDiff(queryMeterDeviceIds(parkOrgId, ELECTRICITY_METER.type), eRoomMeterIds, eAirMeterIds);
        BigDecimal eRoomCount = accumulatedCount(localDate.withDayOfMonth(1), localDate, eRoomMeterIds);
        BigDecimal eAirCount = accumulatedCount(localDate.withDayOfMonth(1), localDate, eAirMeterIds);
        BigDecimal ePublicCount = accumulatedCount(localDate.withDayOfMonth(1), localDate, ePublicMeterIds);
        List<ElectricityConsumptionVO> result = Lists.newArrayList();
        result.add(new ElectricityConsumptionVO(eRoomCount, "室内用电"));
        result.add(new ElectricityConsumptionVO(eAirCount, "空调用电"));
        result.add(new ElectricityConsumptionVO(ePublicCount, "公共用电"));
        return result;
    }

    @Override
    public YearMonthStaticVO getYearMonthStatics(String parkOrgId, LocalDate localDate, Integer type) {
        YearMonthStaticVO vo = new YearMonthStaticVO();
        Set<String> deviceIds = queryMeterDeviceIds(parkOrgId, type);
        LocalDate lastYear = localDate.minusYears(1).withMonth(1).withDayOfMonth(1);
        for (int i = 0; i < 12; i++) {
            BigDecimal lastYearMonthCount = accumulatedCount(lastYear.plusMonths(i), lastYear.plusMonths(i + 1).minusDays(1), deviceIds);
            vo.getLastYear().add(lastYearMonthCount);
        }
        LocalDate thisYearStart = localDate.withMonth(1).withDayOfMonth(1);
        for (int i = 0; i < localDate.getMonthValue(); i++) {
            BigDecimal thisYearMonthCount = accumulatedCount(thisYearStart.plusMonths(i), thisYearStart.plusMonths(i + 1).minusDays(1), deviceIds);
            vo.getCurrentYear().add(thisYearMonthCount);
        }
        BigDecimal total = vo.getCurrentYear().parallelStream().reduce(BigDecimal.ZERO, (a, b) -> NumberUtil.add(a, b));
        BigDecimal average = vo.getCurrentYear().size() == 0 ? BigDecimal.ZERO : NumberUtil.div(total, vo.getCurrentYear().size(), 4);
        vo.setCurrentYearAverage(average);
        return vo;
    }


    private List<StatisticsVO> getLast12MonthConsumption(Collection<String> eDeviceIds, Set<String> wDeviceIds) {
        LocalDate startDate = LocalDate.now().minusMonths(12).withDayOfMonth(1);
        LocalTime startTime = LocalTime.of(0, 0, 0);
        LocalTime endTime = LocalTime.of(23, 59, 59, 99);
        List<StatisticsVO> result = Lists.newArrayList();
        for (int i = 0; i < 12; i++) {
            BigDecimal e = monthReadingService.accumulatedMonthCount(LocalDateTime.of(startDate.plusMonths(i), startTime), LocalDateTime.of(startDate.plusMonths(i + 1).minusDays(1), endTime),
                    eDeviceIds);
            BigDecimal w = monthReadingService.accumulatedMonthCount(LocalDateTime.of(startDate.plusMonths(i), startTime), LocalDateTime.of(startDate.plusMonths(i + 1).minusDays(1), endTime),
                    wDeviceIds);
            result.add(new StatisticsVO(e, w));
        }
        return result;

    }

    private List<StatisticsVO> getLast14DayConsumption(Collection<String> eDeviceIds, Set<String> wDeviceIds) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(14);
        List<StatisticsVO> result = Lists.newArrayList();
        for (int i = 0; i < 14; i++) {
            BigDecimal e = dayReadingService.accumulatedDayCount(startDate.plusDays(i), startDate.plusDays(i + 1), eDeviceIds);
            BigDecimal w = dayReadingService.accumulatedDayCount(startDate.plusDays(i), startDate.plusDays(i + 1), wDeviceIds);
            result.add(new StatisticsVO(e, w));
        }
        log.info("14天用量，result:{}", result);
        return result;
    }

    private List<StatisticsVO> getLast12HourConsumption(Collection<String> eDeviceIds, Set<String> wDeviceIds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusHours(12);
        List<StatisticsVO> result = Lists.newArrayList();
        for (int i = 0; i < 12; i++) {
            LocalDateTime dateTime = startTime.plusHours(i);
            BigDecimal e = hourReadingService.accumulatedHourCount(dateTime.toLocalDate(), dateTime.getHour(), eDeviceIds);
            BigDecimal w = hourReadingService.accumulatedHourCount(dateTime.toLocalDate(), dateTime.getHour(), wDeviceIds);
            result.add(new StatisticsVO(e, w));
        }
        return result;
    }

    private StatisticsVO getThisYearConsumption(Set<String> eDeviceIds, Set<String> wDeviceIds) {
        LocalDate today = LocalDate.now();
        LocalDate newYearDay = LocalDate.of(today.getYear(), 1, 1);
        LocalDateTime newYearTime = LocalDateTime.of(today.getYear(), 1, 1, 0, 0, 0);
        BigDecimal thisYearElectricity = accumulatedCount(newYearDay, today, eDeviceIds);
        BigDecimal thisYearWater = accumulatedCount(newYearDay, today, wDeviceIds);
        accumulatedCount(newYearDay, today, eDeviceIds);
        //StatisticsVO consumption = getThisMonthConsumption(eDeviceIds, wDeviceIds);
        StatisticsVO vo = new StatisticsVO();
        vo.setElectricityCount(thisYearElectricity);
        vo.setWaterCount(thisYearWater);
        return vo;
    }


    private StatisticsVO getThisMonthConsumption(Set<String> eDeviceIds, Set<String> wDeviceIds) {
        LocalDate today = LocalDate.now();
        BigDecimal eDay = dayReadingService.accumulatedDayCount(today.withDayOfMonth(1), today.plusDays(1), eDeviceIds);
        BigDecimal wDay = dayReadingService.accumulatedDayCount(today.withDayOfMonth(1), today.plusDays(1), wDeviceIds);
        StatisticsVO vo = new StatisticsVO();
        vo.setElectricityCount(eDay);
        vo.setWaterCount(wDay);
        return vo;
    }

    private StatisticsVO getTodayConsumption(Set<String> eDeviceIds, Set<String> wDeviceIds) {
        LocalDate today = LocalDate.now();
        BigDecimal eDay = hourReadingService.accumulatedHourCount(today, null, eDeviceIds);
        BigDecimal wDay = hourReadingService.accumulatedHourCount(today, null, wDeviceIds);
        StatisticsVO vo = new StatisticsVO();
        vo.setElectricityCount(eDay);
        vo.setWaterCount(wDay);
        return vo;
    }


    private List<MeterDistributionVO> getDistribution(Integer type, Map<String, Map<Integer, Set<String>>> buildingIdCategoryMeterDeviceIdsMap, Map<Object, String> buildings) {
        List<MeterDistributionVO> result = Lists.newArrayListWithCapacity(buildings.size());
        BigDecimal eCount = BigDecimal.ZERO;
        BigDecimal wCount = BigDecimal.ZERO;
        buildings.forEach((buildingId, buildingName) -> {
            Map<Integer, Set<String>> deviceTypeMeterDeviceIdMap = buildingIdCategoryMeterDeviceIdsMap.get(buildingId);
            if (CollUtil.isEmpty(deviceTypeMeterDeviceIdMap)) {
                result.add(new MeterDistributionVO(buildingName, eCount, wCount));
            } else {
                Set<String> eDeviceIds = deviceTypeMeterDeviceIdMap.get(ELECTRICITY_METER.type);
                Set<String> wDeviceIds = deviceTypeMeterDeviceIdMap.get(WATER_METER.type);
                StatisticsVO vo = null;
                if (type == YEAR_TYPE) {
                    vo = getThisYearConsumption(eDeviceIds, wDeviceIds);
                } else if (type == MONTH_TYPE) {
                    vo = getThisMonthConsumption(eDeviceIds, wDeviceIds);
                } else if (type == DAY_TYPE) {
                    vo = getTodayConsumption(eDeviceIds, wDeviceIds);
                }
                result.add(new MeterDistributionVO(buildingName, vo.getElectricityCount(), vo.getWaterCount()));
            }
        });
        return result;
    }


    private Set<String> queryMeterDeviceIds(String parkOrgId, Integer category) {
        Set<String> deviceIds = queryMeterDeviceIds(parkOrgId, category, null);
        return deviceIds;
    }


    private Set<String> queryMeterDeviceIds(String parkOrgId, Integer deviceType, Integer effectType) {
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<DeviceInfo>();
        wrapper.select(DeviceInfo::getDeviceId)
                .eq(Objects.nonNull(deviceType), DeviceInfo::getDeviceType, deviceType)
                .eq(DeviceInfo::getParkOrgId, parkOrgId);
        if (Objects.nonNull(effectType)) {
            String sql = new StringBuilder(" JSON_CONTAINS(").append(LambdaUtil.getFieldName(DeviceInfo::getExtension)).append(",")
                    .append("'" + effectType + "'").append(",")
                    .append("'$.").append(LambdaUtil.getFieldName(MeterInfoDTO::getEffectProperty)).append("'").append(")").toString();
            wrapper.apply(sql);
        }
        Set<String> deviceIds = deviceInfoService.list(wrapper).parallelStream().map(DeviceInfo::getDeviceId).collect(Collectors.toSet());
        return deviceIds;
    }

    private Map<String, BigDecimal> queryMeterInitMap(Set<String> meterIds) {
        return meterService.id2ValueMap(meterIds, MeterDTO::getInitialReading);
    }

    private BigDecimal accumulatedCount(LocalDate startDate, LocalDate endDate, Collection<String> deviceIds) {
        LocalTime startTime = LocalTime.of(0, 0, 0);
        LocalTime endTime = LocalTime.of(23, 59, 59, 99);
        if (!Objects.equals(startDate, startDate.withDayOfMonth(1)) || (startDate.withDayOfMonth(1).equals(endDate.withDayOfMonth(1)))) {
            BigDecimal count = dayReadingService.accumulatedDayCount(startDate, endDate.plusDays(1), deviceIds);
            return count;
        } else {
            BigDecimal monthDecimal = monthReadingService.accumulatedMonthCount(LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime), deviceIds);
            BigDecimal lastMonthCount = dayReadingService.accumulatedDayCount(endDate.withDayOfMonth(1), endDate.plusDays(1), deviceIds);
            return NumberUtil.add(monthDecimal, lastMonthCount);
        }
    }


    private static <T> Collection<T> firstCollectionDiff(Collection<T> col1, Collection<T>... col2) {
        for (Collection<T> collection : col2) {
            for (T t : collection) {
                col1.remove(t);
            }
        }
        return col1;
    }

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(14);
        LocalDate endDate = today.minusDays(1);
        System.out.println(startDate);
        for (int i = 0; i < 14; i++) {
            System.out.println(startDate.plusDays(i));
        }
    }

    @Override
    public DeviceSummaryVO summary(String deviceId, Integer type) {
        DeviceSummaryVO result = new DeviceSummaryVO();
        result.setDeviceId(deviceId);
        // 今天日期
        LocalDate today = LocalDate.now();
        //1、获取当日设备能耗总量
        BigDecimal d = BigDecimal.ZERO;
        QueryWrapper<HourReading> dailyConsumptionWrapper = new QueryWrapper<>();
        dailyConsumptionWrapper.select(" SUM(IFNULL(consumption,0)) as consumption ").eq("device_id", deviceId).eq("reading_date", today);
        Map<String, Object> dailyConsumptionMap = hourReadingService.getMap(dailyConsumptionWrapper);
        if (dailyConsumptionMap != null && !dailyConsumptionMap.isEmpty()) {
            if (dailyConsumptionMap.get("consumption") != null && NumberUtil.isNumber(dailyConsumptionMap.get("consumption").toString())) {
                d = new BigDecimal(dailyConsumptionMap.get("consumption").toString());
            }
        }
        result.setDailyConsumption(d);
        //2、获取当月设备能耗总量
        BigDecimal m = BigDecimal.ZERO;
        // 2.1 本月第1天
        LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());
        QueryWrapper<DayReading> monthlyConsumptionWrapper = new QueryWrapper<>();
        monthlyConsumptionWrapper.select(" SUM(IFNULL(consumption,0)) as consumption ").eq("device_id", deviceId)
                // 大于等于本月第1天
                .ge("reading_date", firstDay)
                // 小于等于今天
                .le("reading_date", today);
        Map<String, Object> monthlyConsumptionMap = dayReadingService.getMap(monthlyConsumptionWrapper);
        if (monthlyConsumptionMap != null && !monthlyConsumptionMap.isEmpty()) {
            if (monthlyConsumptionMap.get("consumption") != null && NumberUtil.isNumber(monthlyConsumptionMap.get("consumption").toString())) {
                m = new BigDecimal(monthlyConsumptionMap.get("consumption").toString());
            }
        }
        m = m.add(d);
        result.setMonthlyConsumption(m);
        //3、获取当年设备能耗总量
        BigDecimal y = BigDecimal.ZERO;
        QueryWrapper<MonthReading> yearConsumptionWrapper = new QueryWrapper<>();
        yearConsumptionWrapper.select(" SUM(IFNULL(consumption,0)) as consumption ").eq("device_id", deviceId).eq("year", today.getYear());
        Map<String, Object> yearConsumptionMap = monthReadingService.getMap(yearConsumptionWrapper);
        if (yearConsumptionMap != null && !yearConsumptionMap.isEmpty()) {
            if (yearConsumptionMap.get("consumption") != null && NumberUtil.isNumber(yearConsumptionMap.get("consumption").toString())) {
                y = new BigDecimal(yearConsumptionMap.get("consumption").toString());
            }
        }
        y = y.add(m);
        result.setYearConsumption(y);
        return result;
    }
}
