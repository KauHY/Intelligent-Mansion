package com.crlink.smartpark.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crlink.smartpark.common.constants.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class HolidayUtil {

    private static RedisUtil<OneYearHolidayDTO> redisUtil;

    /**
     * https://www.tianapi.com 接口请求key
     */
    private static String onlineReqKey = "602d09aa5b60eb65181cfbee140d7fce";

    public static final String API_URL = "http://api.tianapi.com/jiejiari/index";

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        HolidayUtil.redisUtil = redisUtil;
    }

    public static boolean isHoliday(LocalDate date) {
        OneYearHolidayDTO oneYearHolidayDTO = getOneYearHolidaysWithCache(date.getYear());
        if (oneYearHolidayDTO != null) {
            String dateStr = date.toString();
            if (oneYearHolidayDTO.getLegalHolidays().contains(dateStr)) {
                return true;
            }
            if (oneYearHolidayDTO.getLegalWorkdays().contains(dateStr)) {
                return false;
            }
            return DayOfWeek.SATURDAY.equals(date.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(date.getDayOfWeek());
        }
        return getIsHolidayByDayOnline(date);
    }

    public static boolean isWorkDay(LocalDate date) {
        return !isHoliday(date);
    }

    private static OneYearHolidayDTO getOneYearHolidaysWithCache(Integer year) {
        OneYearHolidayDTO oneYearHolidayDTO = redisUtil.getObject(Constants.Redis.HOLIDAY_PREFIX.getBuildCode(year.toString()));
        if (oneYearHolidayDTO == null) {
            oneYearHolidayDTO = getOneYearHolidaysOnline(year);
            if (oneYearHolidayDTO != null) {
                redisUtil.setObject(Constants.Redis.HOLIDAY_PREFIX.getBuildCode(year.toString()), oneYearHolidayDTO);
            }
        }
        return oneYearHolidayDTO;
    }

    private static OneYearHolidayDTO getOneYearHolidaysOnline(int year) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("key", onlineReqKey);
            params.put("type", 1);
            params.put("date", year);
            String strResult = HttpUtil.get(API_URL, params);
            log.info("获取{}年的节假日结果：{}", year, strResult);
            JSONObject result = JSONObject.parseObject(strResult);
            if (result.getInteger("code") != 200) {
                log.error("获取" + year + "年的节假日结果异常!");
                return null;
            }
            OneYearHolidayDTO oneYearHolidayDTO = new OneYearHolidayDTO();
            JSONArray jsonArray = result.getJSONArray("newslist");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                oneYearHolidayDTO.getLegalHolidays().addAll(Arrays.asList(jsonObject.getString("vacation").split("\\|")));
                String workDays = jsonObject.getString("remark");
                if (StrUtil.isNotBlank(workDays)) {
                    oneYearHolidayDTO.getLegalWorkdays().addAll(Arrays.asList(workDays.split("\\|")));
                }
            }
            return oneYearHolidayDTO;
        } catch (Exception e) {
            log.error("在线获取节假日api请求异常", e);
        }
        return null;
    }

    private static Boolean getIsHolidayByDayOnline(LocalDate localDate) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("key", onlineReqKey);
            params.put("date", localDate.toString());
            params.put("type", "");
            String strResult = HttpUtil.get(API_URL, params);
            log.info("获取的{}节假日结果：{}", localDate, strResult);
            JSONObject result = JSONObject.parseObject(strResult);
            if (result.getInteger("code") != 200) {
                log.error("获取{}节假日结果异常!", localDate);
                return false;
            }
            JSONArray jsonArray = result.getJSONArray("newslist");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getInteger("isnotwork") == 1;
        } catch (Exception e) {
            log.error("在线获取节假日api请求异常", e);
        }
        return false;
    }

    @Data
    static class OneYearHolidayDTO {
        private Set<String> legalHolidays = new HashSet<>();
        private Set<String> legalWorkdays = new HashSet<>();
    }
}
