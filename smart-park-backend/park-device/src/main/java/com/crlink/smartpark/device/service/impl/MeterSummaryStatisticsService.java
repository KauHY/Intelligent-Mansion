package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.crlink.smartpark.device.controller.response.MeterReadingTrendVO;
import com.crlink.smartpark.device.controller.response.StatisticsVO;
import com.crlink.smartpark.device.entity.DayReading;
import com.crlink.smartpark.device.entity.HourReading;
import com.crlink.smartpark.device.entity.MonthReading;
import com.crlink.smartpark.device.service.DayReadingService;
import com.crlink.smartpark.device.service.HourReadingService;
import com.crlink.smartpark.device.service.MonthReadingService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description 能源汇总趋势统计
 * @Created by jiangjiye
 */
@Service
public class MeterSummaryStatisticsService {

    @Autowired
    private MonthReadingService monthReadingService;

    @Autowired
    private DayReadingService dayReadingService;

    @Autowired
    private HourReadingService hourReadingService;

    /**
     * 按年汇总计算每月用电用水同比
     * @param eRoomMeterIds 房间用电
     * @param eAirMeterIds 空调用电
     * @param wDeviceIds 用水
     * @param ePublicMeterIds 公共用电
     * @return
     */
    public List<MeterReadingTrendVO> summaryByYear(Set<String> eRoomMeterIds, Set<String> eAirMeterIds, Set<String> wDeviceIds, Collection<String> ePublicMeterIds) {
        List<MeterReadingTrendVO> result = new ArrayList<>();
        // 今天
        LocalDate today = LocalDate.now();
        // 去年今天
        LocalDate lastYearToday = today.minusYears(1);
        // 当前年份
        int tYear = today.getYear();
        // 去年年份
        int lYear = lastYearToday.getYear();
        // 去年月份
        int lmonth = lastYearToday.getMonthValue();

        /** 以下数据 取回的列表都是顺序存储，1-12月份的数据，可以通过(月份-1)得到数据下标 start*/
        // 求出去年数据
        // 房间每月用水用电
        List<StatisticsVO> lr = getMonthConsumption(lYear, lmonth, eRoomMeterIds, wDeviceIds);
        // 空调每月用电没有用水
        List<StatisticsVO> la = getMonthConsumption(lYear, lmonth, eAirMeterIds, null);
        // 公共每月用电没有用水
        List<StatisticsVO> lp = getMonthConsumption(lYear, lmonth, ePublicMeterIds, null);

        // 求出今年数据（从1月开始）
        // 房间每月用水用电
        List<StatisticsVO> tr = getMonthConsumption(tYear, 1, eRoomMeterIds, wDeviceIds);
        // 空调每月用电没有用水
        List<StatisticsVO> ta = getMonthConsumption(tYear, 1, eAirMeterIds, null);
        // 公共每月用电没有用水
        List<StatisticsVO> tp = getMonthConsumption(tYear, 1, ePublicMeterIds, null);
        /** 以下数据 取回的列表都是顺序存储，1-12月份的数据，可以通过(月份-1)得到数据下标 end*/

        // 封装响应数据
        for (int month = 0; month < 12; month++) {
            MeterReadingTrendVO vo = new MeterReadingTrendVO();
            // 从去年开始加月份
            LocalDate date_flag = lastYearToday.plusMonths(month);
            // 设置日期
            vo.setDate(date_flag);
            // 计算月份对应的下标值
            int m_flag = date_flag.getMonthValue() - 1;
            // 房间每月用电量
            BigDecimal e = tr.get(m_flag).getElectricityCount();
            // 空调每月用电量
            BigDecimal a = ta.get(m_flag).getElectricityCount();
            // 公共每月用电量
            BigDecimal p = tp.get(m_flag).getElectricityCount();
            // 每月用水量
            BigDecimal w = tr.get(m_flag).getWaterCount();
            // 封装水电数据
            vo.setERoom(e.setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setEAir(a.setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setEPublic(p.setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setWater(w.setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal t = NumberUtil.add(e, a, p);
            vo.setETotal(t.setScale(BigDecimal.ROUND_HALF_UP));
            // 开始计算同比
            {
                BigDecimal el = lr.get(m_flag).getElectricityCount();
                BigDecimal elw = lr.get(m_flag).getWaterCount();
                BigDecimal al = la.get(m_flag).getElectricityCount();
                BigDecimal pl = lp.get(m_flag).getElectricityCount();
                BigDecimal tl = NumberUtil.add(el, al, pl);
                vo.setELastMonthPercent(t == BigDecimal.ZERO || tl == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(t, tl, 4));
                vo.setWLastMonthPercent(w == BigDecimal.ZERO || elw == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(w, elw, 4));
            }
            result.add(vo);
        }

        return result;
    }

    /**
     * 按月汇总计算每月用电用水同比
     * @param eRoomMeterIds 房间用电
     * @param eAirMeterIds 空调用电
     * @param wDeviceIds 用水
     * @param ePublicMeterIds 公共用电
     * @return
     */
    public List<MeterReadingTrendVO> summaryByMonth(Set<String> eRoomMeterIds, Set<String> eAirMeterIds, Set<String> wDeviceIds, Collection<String> ePublicMeterIds) {
        List<MeterReadingTrendVO> result = new ArrayList<>();
        // 今天
        LocalDate today = LocalDate.now();
        // 过去14天
        LocalDate last14DayWithToday = today.minusDays(14);
        // 去年今天
        LocalDate lastYearToday = today.minusYears(1);
        // 去年今天的倒推14天
        LocalDate lastYearTodayLast14DayWithToday = lastYearToday.minusDays(14);
        /** 以下数据 取回的列表都是顺序存储，过去14天的数据，可以通过(0-13)得到数据下标 start*/
        // 求出去年数据
        // 过去14天到当前
        List<StatisticsVO> lr = getDayConsumption(lastYearTodayLast14DayWithToday, lastYearToday, eRoomMeterIds, wDeviceIds);
        // 过去14天用电没有用水
        List<StatisticsVO> la = getDayConsumption(lastYearTodayLast14DayWithToday, lastYearToday, eAirMeterIds, null);
        // 过去14用电没有用水
        List<StatisticsVO> lp = getDayConsumption(lastYearTodayLast14DayWithToday, lastYearToday, ePublicMeterIds, null);

        // 求出今年数据
        // 过去14天到当前
        List<StatisticsVO> tr = getDayConsumption(last14DayWithToday, today, eRoomMeterIds, wDeviceIds);
        // 过去14天用电没有用水
        List<StatisticsVO> ta = getDayConsumption(last14DayWithToday, today, eAirMeterIds, null);
        // 过去14用电没有用水
        List<StatisticsVO> tp = getDayConsumption(last14DayWithToday, today, ePublicMeterIds, null);
        /** 以下数据 取回的列表都是顺序存储，过去14天的数据，可以通过(0-13)得到数据下标 end*/
        // 封装响应数据
        for (int day = 0; day < 14; day++) {
            MeterReadingTrendVO vo = new MeterReadingTrendVO();
            // 设置日期
            vo.setDate(last14DayWithToday.plusDays(day));
            // 房间每月用电量
            BigDecimal e = tr.get(day).getElectricityCount();
            // 空调每月用电量
            BigDecimal a = ta.get(day).getElectricityCount();
            // 公共每月用电量
            BigDecimal p = tp.get(day).getElectricityCount();
            // 每月用水量
            BigDecimal w = tr.get(day).getWaterCount();

            vo.setERoom(e.setScale(2,BigDecimal.ROUND_HALF_UP));
            vo.setEAir(a.setScale(2,BigDecimal.ROUND_HALF_UP));
            vo.setEPublic(p.setScale(2,BigDecimal.ROUND_HALF_UP));
            vo.setWater(w.setScale(2,BigDecimal.ROUND_HALF_UP));
            BigDecimal t = NumberUtil.add(e, a, p);
            vo.setETotal(t.setScale(BigDecimal.ROUND_HALF_UP));
            // 开始计算同比
            {
                BigDecimal el = lr.get(day).getElectricityCount();
                BigDecimal elw = lr.get(day).getWaterCount();
                BigDecimal al = la.get(day).getElectricityCount();
                BigDecimal pl = lp.get(day).getElectricityCount();
                BigDecimal tl = NumberUtil.add(el, al, pl);
                vo.setELastMonthPercent(tl == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(t, tl, 4));
                vo.setWLastMonthPercent(elw == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(w, elw, 4));
            }
            result.add(vo);
        }
        return result;
    }

    /**
     * 按天汇总计算每月用电用水同比
     * @param eRoomMeterIds 房间用电
     * @param eAirMeterIds 空调用电
     * @param wDeviceIds 用水
     * @param ePublicMeterIds 公共用电
     * @return
     */
    public List<MeterReadingTrendVO> summaryByDay(Set<String> eRoomMeterIds, Set<String> eAirMeterIds, Set<String> wDeviceIds, Collection<String> ePublicMeterIds) {
        List<MeterReadingTrendVO> result = new ArrayList<>();
        // 今天
        LocalDateTime currentTime = LocalDateTime.now();
        // 倒推12小时
        LocalDateTime last12HourForCurrentTime = currentTime.minusHours(12);
        // 去年今天
        LocalDateTime lastYearCurrentTime = currentTime.minusYears(1);
        // 去年今天过去12小时
        LocalDateTime lastYearCurrentTimeLast12HourForCurrentTime = lastYearCurrentTime.minusHours(12);

        /** 以下数据 取回的列表都是顺序存储，过去12小时的数据，可以通过(0-13)得到数据下标 start*/
        // 求出去年数据
        // 过去12小时到当前
        List<StatisticsVO> lr = getHourConsumption(lastYearCurrentTime, lastYearCurrentTimeLast12HourForCurrentTime, eRoomMeterIds, wDeviceIds);
        // 过去12小时用电没有用水
        List<StatisticsVO> la = getHourConsumption(lastYearCurrentTime, lastYearCurrentTimeLast12HourForCurrentTime, eAirMeterIds, null);
        // 过去12小时用电没有用水
        List<StatisticsVO> lp = getHourConsumption(lastYearCurrentTime, lastYearCurrentTimeLast12HourForCurrentTime, ePublicMeterIds, null);

        // 求出今年数据
        // 过去12小时到当前
        List<StatisticsVO> tr = getHourConsumption(currentTime, last12HourForCurrentTime, eRoomMeterIds, wDeviceIds);
        // 过去12小时用电没有用水
        List<StatisticsVO> ta = getHourConsumption(currentTime, last12HourForCurrentTime, eAirMeterIds, null);
        // 过去12小时用电没有用水
        List<StatisticsVO> tp = getHourConsumption(currentTime, last12HourForCurrentTime, ePublicMeterIds, null);
        /** 以下数据 取回的列表都是顺序存储，过去12小时的数据，可以通过(0-13)得到数据下标 end*/
        // 封装响应数据
        for (int hour = 0; hour < 12; hour++) {
            MeterReadingTrendVO vo = new MeterReadingTrendVO();
            // 设置日期
            vo.setDate(last12HourForCurrentTime.plusHours(hour));
            // 房间每月用电量
            BigDecimal e = tr.get(hour).getElectricityCount();
            // 空调每月用电量
            BigDecimal a = ta.get(hour).getElectricityCount();
            // 公共每月用电量
            BigDecimal p = tp.get(hour).getElectricityCount();
            // 每月用水量
            BigDecimal w = tr.get(hour).getWaterCount();

            vo.setERoom(e.setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setEAir(a.setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setEPublic(p.setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setWater(w.setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal t = NumberUtil.add(e, a, p);
            vo.setETotal(t.setScale(BigDecimal.ROUND_HALF_UP));
            // 开始计算同比
            {
                BigDecimal el = lr.get(hour).getElectricityCount();
                BigDecimal elw = lr.get(hour).getWaterCount();
                BigDecimal al = la.get(hour).getElectricityCount();
                BigDecimal pl = lp.get(hour).getElectricityCount();
                BigDecimal tl = NumberUtil.add(el, al, pl);
                vo.setELastMonthPercent(tl == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(t, tl, 4));
                vo.setWLastMonthPercent(elw == BigDecimal.ZERO ? BigDecimal.ZERO : NumberUtil.div(w, elw, 4));
            }
            result.add(vo);
        }
        return result;
    }

    /**
     *  查询并封装电水每月总用量
     * @param year 年份
     * @param startMonth 开始月份
     * @param eDeviceIds 电表集合
     * @param wDeviceIds 水表集合
     * @return
     */
    private List<StatisticsVO> getMonthConsumption(int year, int startMonth, Collection<String> eDeviceIds, Collection<String> wDeviceIds) {
        List<StatisticsVO> result = Lists.newArrayList();
        // 按每月分组统计电表总用量
        List<Map<String, Object>> eList = queryMonthConsumption(year, startMonth, eDeviceIds);
        // 按每月分组统计水表总用量
        List<Map<String, Object>> wList = queryMonthConsumption(year, startMonth, wDeviceIds);
        // 字段名称
        String col = "month";
        // 封装1-12月数据
        for (int month = 0; month < 12; month++) {
            String m = String.valueOf(month + 1);
            BigDecimal e = BigDecimal.ZERO;
            BigDecimal w = BigDecimal.ZERO;
            e = getConsumption(eList, m, col, e);
            w = getConsumption(wList, m, col, w);
            result.add(new StatisticsVO(e, w));
        }
        return result;
    }

    /**
     *  查询并封装电水每月总用量
     * @param startDate 过去14天
     * @param endDate 今天
     * @param eDeviceIds 电表id集合
     * @param wDeviceIds 水表id集合
     * @return
     */
    private List<StatisticsVO> getDayConsumption(LocalDate startDate, LocalDate endDate, Collection<String> eDeviceIds, Collection<String> wDeviceIds) {
        List<StatisticsVO> result = Lists.newArrayList();
        // 按每天分组统计电表总用量
        List<Map<String, Object>> eList = queryDayConsumption(startDate, endDate, eDeviceIds);
        // 按每天分组统计水表总用量
        List<Map<String, Object>> wList = queryDayConsumption(startDate, endDate, wDeviceIds);
        // 日期格式
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 字段名称
        String col = "reading_date";
        // 封装过去14天数据
        for (int day = 0; day < 14; day++) {
            String d = df.format(startDate.plusDays(day));
            BigDecimal e = BigDecimal.ZERO;
            BigDecimal w = BigDecimal.ZERO;
            e = getConsumption(eList, d, col, e);
            w = getConsumption(wList, d, col, w);
            result.add(new StatisticsVO(e, w));
        }
        return result;
    }

    /**
     *  查询并封装电水每小时数据
     * @param currentTime 当前时间
     * @param last12HourForCurrentTime 过去12小时
     * @param eDeviceIds 电表id集合
     * @param wDeviceIds 水表id集合
     * @return
     */
    private List<StatisticsVO> getHourConsumption(LocalDateTime currentTime, LocalDateTime last12HourForCurrentTime, Collection<String> eDeviceIds, Collection<String> wDeviceIds) {
        List<StatisticsVO> result = Lists.newArrayList();
        // 按每天分组统计电表总用量
        List<Map<String, Object>> eList = queryHourConsumption(currentTime, last12HourForCurrentTime, eDeviceIds);
        // 按每天分组统计水表总用量
        List<Map<String, Object>> wList = queryHourConsumption(currentTime, last12HourForCurrentTime, wDeviceIds);
        // 字段名称
        String col = "reading_hour";
        // 封装过去12小时数据
        for (int hour = 0; hour < 12; hour++) {
            int h = last12HourForCurrentTime.plusHours(hour).getHour();
            BigDecimal e = BigDecimal.ZERO;
            BigDecimal w = BigDecimal.ZERO;
            e = getConsumption(eList, String.valueOf(h), col, e);
            w = getConsumption(wList, String.valueOf(h), col, w);
            result.add(new StatisticsVO(e, w));
        }
        return result;
    }

    /**
     * 以月份维度统计用水用电量
     * @param year 年份
     * @param startMonth 开始月份
     * @param ids 表计id
     * @return 集合
     */
    private List<Map<String, Object>> queryMonthConsumption(int year, int startMonth, Collection<String> ids){
        if(ids == null) {
            return new ArrayList<>();
        }
        return monthReadingService.listMaps(new QueryWrapper<MonthReading>()
                .select(" month,sum(consumption) as consumption ")
                // 年份
                .eq("year", year)
                // 大于等于
                .ge("month", startMonth)
                .in("device_id", ids)
                .groupBy("month")
                .orderByAsc("id"));
    }

    /**
     * 以天维度统计用水用电量
     * @param startDate 过去14天
     * @param endDate 今天
     * @param ids 表计id
     * @return 集合
     */
    private List<Map<String, Object>> queryDayConsumption(LocalDate startDate, LocalDate endDate, Collection<String> ids){
        if(ids == null) {
            return new ArrayList<>();
        }
        return dayReadingService.listMaps(new QueryWrapper<DayReading>()
                .select(" reading_date,sum(consumption) as consumption ")
                // 大于等于14天那天
                .ge("reading_date", startDate)
                // 小于当天，因为当天还没过完
                .lt("reading_date", endDate)
                .in("device_id", ids)
                .groupBy("reading_date")
                .orderByAsc("id"));
    }

    /**
     * 以小时维度统计用水用电量
     * @param currentTime 当前时间
     * @param last12HourForCurrentTime 过去12小时
     * @param ids 表计id
     * @return 集合
     */
    private List<Map<String, Object>> queryHourConsumption(LocalDateTime currentTime, LocalDateTime last12HourForCurrentTime, Collection<String> ids){
        if(ids == null) {
            return new ArrayList<>();
        }
        return hourReadingService.listMaps(new QueryWrapper<HourReading>()
                .select(" reading_hour,sum(consumption) as consumption ")
                // 大于等于过去12小时
                .ge("reading_date_time", last12HourForCurrentTime)
                // 大于等于当前
                .le("reading_date_time", currentTime)
                .in("device_id", ids)
                .groupBy("reading_hour")
                .orderByAsc("id"));
    }

    /**
     * 解析查询出来的数据
     * @param list SQL查询集合
     * @param m 月份
     * @param e 总计量值
     * @return 总计量值
     */
    private BigDecimal getConsumption(List<Map<String, Object>> list, String m, String col, BigDecimal e) {
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, Object> monthMap = list.stream().filter(l -> String.valueOf(m).equals(l.get(col).toString())).findFirst().orElse(null);
            if (monthMap != null) {
                Object t = monthMap.get("consumption");
                e = t != null ? new BigDecimal(t.toString()) : BigDecimal.ZERO;
            }
        }
        return e;
    }

}
