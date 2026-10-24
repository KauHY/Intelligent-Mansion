package com.crlink.smartpark.core.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateUtils {

    /**
     * 获取某月所需周几的日期
     *
     * @param daysOfWeek
     * @param localDate
     * @return
     */
    public static List<LocalDate> getWeekDaysOfMonth(List<Integer> daysOfWeek, LocalDate localDate) {
        LocalDate startDay = localDate.withDayOfMonth(1);
        LocalDate endDay = startDay.plusDays(startDay.lengthOfMonth() - 1);

        return getDaysOfWeekByRange(startDay, endDay, daysOfWeek);
    }

    /**
     * 获取区间内所需周几的日期
     *
     * @param startDay
     * @param endDay
     * @param daysOfWeek
     * @return
     */
    public static List<LocalDate> getDaysOfWeekByRange(LocalDate startDay, LocalDate endDay, List<Integer> daysOfWeek) {
        List<LocalDate> localDateList = new ArrayList<>();
        TemporalField fieldISO = WeekFields.of(DayOfWeek.of(1), 1).dayOfWeek();
        LocalDate tempDay;
        for (Integer day : daysOfWeek) {
            tempDay = startDay.with(fieldISO, day.longValue());
            if (tempDay.isBefore(startDay)) {
                tempDay = tempDay.plusWeeks(1);
            }
            while (tempDay.isBefore(endDay) || tempDay.isEqual(endDay)) {
                localDateList.add(tempDay);
                tempDay = tempDay.plusWeeks(1);
            }
        }
        return localDateList;
    }

    public static long upGetMonths(LocalDate start, LocalDate end) {
        long months = ChronoUnit.MONTHS.between(start, end);
        if (start.getDayOfMonth() != end.getDayOfMonth()) {
            months++;
        }
        return months;
    }

    /**
     * 获取一个月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getLastDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }
}
