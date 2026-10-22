package com.crlink.smartpark.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MeetingScreenUtils {
    public enum LightColor {
        red, green, blue;
    }

    //public static final String URL_BUILDER = "https://imsyun.cleartv.cn";
    //public static String URL_BUILDER = "http://172.16.20.11:8001";
    public static String URL_BUILDER;

    public static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    public static final LoadingCache<String, String> IP_CACHE = CacheBuilder
            .newBuilder()
            .maximumSize(4096)
            .expireAfterAccess(2, TimeUnit.DAYS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String id) {
                    Map<String, String> deviceIp = getDeviceIp();
                    IP_CACHE.putAll(deviceIp);
                    return deviceIp.get(id);
                }
            });

    @Value("${screen.url.base:https://imsyun.cleartv.cn}")
    public void setBaseUrl(String url) {
        URL_BUILDER = url;
    }

    public static void controlLight(String id, LightColor color, int brightness) {
        String url = null;
        String deviceIp = null;
        try {
            deviceIp = IP_CACHE.get(id);
            log.info("----------------------id={}", id);
            log.info("----------------------IP_CACHE={}", IP_CACHE.asMap());
            if (Objects.nonNull(deviceIp)) {
                url = deviceIp + ":7777/setledseekbar/" + color.name() + ":" + brightness;
                log.info("----------------------url={}", url);
                HttpUtil.get(url);
            } else {
                Map<String, String> deviceIpMap = IP_CACHE.asMap();
                throw new RuntimeException("设备id不存在" + id + ",设备ip列表:" + deviceIpMap);
            }
        } catch (Exception e) {
            log.error("控制灯光失败，设备IP{},url:{},e:{}", deviceIp, url, e);
        }
    }

    public static Map<String, String> getDeviceIp() {
        String response = getScreenDeviceList();
        JSONObject responseObj = JSONUtil.parseObj(response);
        JSONObject termList = (JSONObject) responseObj.get("termList");
        JSONArray list = termList.getJSONArray("terms");
        Map<String, String> ipMap = new HashMap<>();
        for (Object o : list) {
            JSONObject jsonObject = (JSONObject) o;
            String ip = jsonObject.getStr("IP");
            String id = jsonObject.getStr("ID");
            ipMap.put(id, ip);
        }
        log.info("会议室信息屏ip:{}", ipMap);
        return ipMap;
    }

    public static Map<String, Set<String>> getTermStatus() {
        String response = getScreenDeviceList();
        JSONObject responseObj = JSONUtil.parseObj(response);
        JSONObject termList = (JSONObject) responseObj.get("termList");
        JSONArray list = termList.getJSONArray("terms");
        Map<String, Set<String>> result = new HashMap<>();
        for (Object o : list) {
            JSONObject jsonObject = (JSONObject) o;
            String status = jsonObject.getStr("Status");
            Integer online = Integer.valueOf(jsonObject.getStr("Online"));
            String id = jsonObject.getStr("ID");
            if (online == 1) {
                result.compute(status, (k, v) -> {
                    if (v == null) {
                        v = Sets.newHashSet(id);
                        return v;
                    } else {
                        v.add(id);
                        return v;
                    }
                });
            } else {
                result.compute("Offline", (k, v) -> {
                    if (v == null) {
                        v = Sets.newHashSet(id);
                        return v;
                    } else {
                        v.add(id);
                        return v;
                    }
                });
            }
        }
        return result;

    }

    //获取截屏图片
    public static String getScreenshotUrl(String id) {
        String response = getScreenshots(id);
        JSONObject parseObj = JSONUtil.parseObj(response);
        String result = parseObj.getStr("SnapshotPicURL");
        return result;
    }


    public static boolean sleepDevice(String id) {
        return updateTermStatus(id, "termPowerOffMulti");
    }

    public static boolean wakeUpDevice(String id) {
        return updateTermStatus(id, "termPowerOnMulti");
    }

    /**
     * 下发截屏指令
     * @param id
     * @param count 重试次数，默认3次后不重试
     */
    public static String termSnapshot(String id, int count) {
        String url = UrlBuilder.of(URL_BUILDER + "/backend_mgt/v2/term").toString();
        JSONObject param = new JSONObject();
        param.putOpt("ID", id);
        param.putOpt("action", "termSnapshot");
        // 这个地址其实可以写调用方地址
        param.putOpt("uploadURL", "");
        buildBody(param);
        String body = JSONUtil.toJsonStr(param);
        String post = null;
        try {
            post = HttpUtil.post(url, body, 500);
            if (needRetry(post) && count < 3) {
                post = termSnapshot(id, count++);
            }
        } catch (Exception e) {
            log.error("下发截屏指令，接口调用失败,url:{},body:{}！", url, body, e);
        }
        return post;
    }

    private static String getScreenshots(String id) {
        String url = UrlBuilder.of(URL_BUILDER + "/backend_mgt/v2/term").toString();
        JSONObject map = new JSONObject();
        map.put("ID", id);
        map.put("action", "getInfo");
        buildBody(map);
        String body = JSONUtil.toJsonStr(map);
        String post = null;
        try {
            post = HttpUtil.post(url, body, 500);
            if (needRetry(post)) {
                post = getScreenshots(id);
            }
        } catch (Exception e) {
            log.error("接口调用失败,url:{},body:{}！", url, body, e);
            throw new ResolinkRuntimeException("10001", "网络异常！");
        }
        return post;
    }

    //终端列表
    private static String getScreenDeviceList() {
        String url = URL_BUILDER + "/backend_mgt/v2/termcategory";
        JSONObject map = new JSONObject();
        map.put("action", "getTermList");
        map.put("categoryID", "1");
        JSONObject pager = new JSONObject();
        pager.putOpt("total", -1);
        pager.putOpt("per_page", 1000);
        pager.putOpt("page", 1);
        pager.putOpt("orderby", "");
        pager.putOpt("sortby", "");
        pager.putOpt("keyword", "");
        pager.putOpt("status", "");
        map.putOpt("Pager", pager);
        buildBody(map);
        String body = JSONUtil.toJsonPrettyStr(map);
        String post = null;
        try {
            post = HttpUtil.post(url, body, 500);
            if (needRetry(post)) {
                post = getScreenDeviceList();
            }
        } catch (Exception e) {
            log.error("接口调用失败,url:{},body:{}", url, body, e);
            throw new ResolinkRuntimeException("10001", "网络异常！");
        }
        return post;
    }

    private static boolean updateTermStatus(String id, String action) {
        String rescode = null;
        try {
            String post = updateTermStatus1(id, action);
            rescode = JSONUtil.parseObj(post).getStr("rescode");
        } catch (Exception e) {
            log.error("域名地址：{},接口调用失败,id:{},action:{}", URL_BUILDER, id, action, e);
            throw new ResolinkRuntimeException("10004", "网络异常！");
        }
        return Objects.equals("200", rescode);
    }

    //批量休眠终端
    private static String updateTermStatus1(String id, String action) {
        String url = URL_BUILDER + "/backend_mgt/v2/term";
        JSONObject map = new JSONObject();
        //todo action
        //map.put("action", "termPowerOffMulti");
        //map.put("action", "termPowerOnMulti");
        map.put("action", action);
        JSONObject jsonObject = new JSONObject();
        //jsonObject.putOpt("status", "running");
        jsonObject.putOpt("termID", id);
        JSONArray array = new JSONArray();
        array.put(jsonObject);
        map.putOpt("termList", array);
        buildBody(map);
        String body = JSONUtil.toJsonStr(map);
        String post = null;
        try {
            post = HttpUtil.post(url, body, 500);
            if (needRetry(post)) {
                post = updateTermStatus1(id, action);
            }
        } catch (Exception e) {
            log.error("调用接口失败,id:{},action:{}", id, action);
            log.error("接口调用失败,url:{},body:{}", url, body, e);
            throw new ResolinkRuntimeException("10004", "网络异常！");
        }
        return post;
    }


    private static String generateLoginArgs() {
        JSONObject map = new JSONObject();
        map.putOpt("xxx1", "GetToken");
        map.putOpt("xxx2", "default");
        //map.putOpt("xxx2", "szdemo");
        //map.putOpt("xxx3", "root@szdemo");
        map.putOpt("xxx3", "root");
        //密码
        //String md5Hex = DigestUtil.md5Hex("clear123456");
        String md5Hex = DigestUtil.md5Hex("123456");
        map.putOpt("xxx4", md5Hex);
        String body = JSONUtil.toJsonStr(map);
        //body = StrUtil.replace(body, ":", ": ");
        String encode = Base64.encode(body);
        return encode;
    }


    private static void updateToken() {
        String url = URL_BUILDER + "/backend_mgt/v2/logon";
        String loginBody = generateLoginArgs();
        String response = HttpUtil.post(url, loginBody, 500);
        JSONObject responseObj = JSONUtil.parseObj(response);
        String token = responseObj.getStr("token");
        TOKEN.set(token);
    }

    private static boolean needRetry(String response) {
        JSONObject responseObj = JSONUtil.parseObj(response);
        if (Objects.equals("401", responseObj.getStr("rescode"))) {
            updateToken();
            return true;
        }
        return false;
    }


    private static JSONObject buildBody(JSONObject json) {
        json.put("project_name", "default");
        //map.put("token", "b508c9f2-b960-11ec-bf77-20040feb5ba41746605f-4eca-4aea-8c09-fa1e70c43e70");
        json.put("token", TOKEN.get());
        json.put("user", "root");
        return json;
    }
}
