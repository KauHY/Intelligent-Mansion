package com.crlink.smartpark.common.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.ProxyConnectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 获取天气数据
 *
 */
@Slf4j
@Api(value = "天气数据", tags = "天气数据")
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private static final String WEATHER_URL = "https://devapi.qweather.com/v7/weather/now?key=ae3206d80c9e4f2a9979221f578a9936&location=101190907";
    private static final String WARNING_URL = "https://devapi.qweather.com/v7/warning/now?key=ae3206d80c9e4f2a9979221f578a9936&location=101190907";
    private static final String AIR_URL = "https://devapi.qweather.com/v7/air/now?key=ae3206d80c9e4f2a9979221f578a9936&location=101190907";
    @Value("${param.wx.timeOut:60000}")
    private Integer timeOut; //请求超时时间：单位毫秒
    @Value("${param.wx.proxyIP:}")
    private String proxyIP;
    @Value("${param.wx.proxyPort:80}")
    private Integer proxyPort;
    private static final String SUCCESS_CODE = "200";
    private static final String WEATHER_KEY = "weather_data";
    private static final Map<String, String> colorMap = new HashMap<>();
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    static {
        colorMap.put("White", "白色");
        colorMap.put("Blue", "蓝色");
        colorMap.put("Green", "绿色");
        colorMap.put("Yellow", "黄色");
        colorMap.put("Orange", "橙色");
        colorMap.put("Red", "红色");
        colorMap.put("Black", "黑色");
    }

    @ApiOperation(value = "获取", tags = "获取")
    @GetMapping("/get")
    public ResponseResult<Object> getWeather() {
        JSONObject resultJson = new JSONObject();
        try {
            String weatherStr = redisTemplate.opsForValue().get(WEATHER_KEY);
            if (StringUtils.isNotBlank(weatherStr)) {
                log.info("拿到天气缓存：{}", weatherStr);
                resultJson = JSONUtil.parseObj(weatherStr);
            } else {
                JSONObject finalResultJson = resultJson;
                CompletableFuture<JSONObject> future1 = CompletableFuture.supplyAsync(() ->
                    JSONUtil.parseObj((ProxyConnectionUtil.get(WEATHER_URL, timeOut, proxyIP, proxyPort)))
                ).whenComplete((tempJson, throwable) -> {
                    log.info("天气：{}", tempJson);
                    if (SUCCESS_CODE.equals(tempJson.getStr("code"))) {
                        tempJson = tempJson.getJSONObject("now");
                        finalResultJson.set("temp", tempJson.getStr("temp"));
                        finalResultJson.set("text", tempJson.getStr("text"));
                        finalResultJson.set("precip", tempJson.getStr("precip"));
                        finalResultJson.set("icon", tempJson.getStr("icon"));
                    }
                });
                CompletableFuture<JSONObject> future2 = CompletableFuture.supplyAsync(() ->
                        JSONUtil.parseObj((ProxyConnectionUtil.get(WARNING_URL, timeOut, proxyIP, proxyPort)))
                ).whenComplete((tempJson, throwable) -> {
                    log.info("天气预警：{}", tempJson);
                    if (SUCCESS_CODE.equals(tempJson.getStr("code"))) {
                        JSONArray jsonArray = tempJson.getJSONArray("warning");
                        if (!jsonArray.isEmpty()) {
                            tempJson = jsonArray.getJSONObject(0);
                            String colorName = colorMap.get(tempJson.getStr("severityColor"));
                            finalResultJson.set("warning", colorName + tempJson.getStr("typeName"));
                        } else {
                            finalResultJson.set("warning", "无");
                        }
                    }
                });
                CompletableFuture<JSONObject> future3 = CompletableFuture.supplyAsync(() ->
                        JSONUtil.parseObj((ProxyConnectionUtil.get(AIR_URL, timeOut, proxyIP, proxyPort)))
                ).whenComplete((tempJson, throwable) -> {
                    log.info("PM质量：{}", tempJson);
                    if (SUCCESS_CODE.equals(tempJson.getStr("code"))) {
                        tempJson = tempJson.getJSONObject("now");
                        finalResultJson.set("pm10", tempJson.getStr("pm10"));
                        finalResultJson.set("pm2p5", tempJson.getStr("pm2p5"));
                    }
                });
                CompletableFuture.allOf(future1, future2, future3).join();
                if (finalResultJson.size() > 0) {
                    redisTemplate.opsForValue().set(WEATHER_KEY, JSONUtil.toJsonStr(finalResultJson), 1L, TimeUnit.HOURS);
                } else {
                    resultJson = getEmptyWeather();
                }
            }
        } catch (Exception e) {
            log.error("查询天气失败！", e);
            resultJson = getEmptyWeather();
        }
        return ResponseResult.success(resultJson);
    }

    private JSONObject getEmptyWeather() {
        String str = "{\"pm10\":\"0\",\"pm2p5\":\"0\",\"precip\":\"0\",\"warning\":\"无\",\"icon\":\"\",\"text\":\"未知\",\"temp\":\"0\"}";
        return JSONUtil.parseObj(str);
    }

}
