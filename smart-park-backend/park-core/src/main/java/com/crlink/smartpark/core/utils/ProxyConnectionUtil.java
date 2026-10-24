package com.crlink.smartpark.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

/**
 * 微信接口连接工具
 *
 */
@Slf4j
public final class ProxyConnectionUtil {

    // 不可以创建
    private ProxyConnectionUtil() {
    }

    /**
     * 代理接口请求（GET）
     *
     * @param url       接口的地址
     * @param timeOut   请求超时时间（毫秒）
     * @param proxyIP   代理IP
     * @param proxyPort 代理端口
     * @return 接口返回数据
     */
    public static String get(String url, int timeOut, String proxyIP, Integer proxyPort) {
        try {
            if (StrUtil.isBlank(url)) {
                return null;
            }
            if (StrUtil.isBlank(proxyIP) || proxyPort == null || proxyPort <= 0) {
                return HttpUtil.get(url);
            }
            return HttpUtil.createGet(url.toString()).setProxy(getProxy(proxyIP, proxyPort)).timeout(timeOut > 0 ? timeOut : -1).execute().body();
        } catch (Exception e) {
            log.error("ProxyConnectionUtil.get方法异常！", e);
        }
        return null;
    }

    /**
     * 代理接口请求（GET）
     *
     * @param url       接口的地址
     * @param timeOut   请求超时时间（毫秒）
     * @param proxyIP   代理IP
     * @param proxyPort 代理端口
     * @return 接口返回数据
     */
    public static String get(String url, int timeOut, Map<String, String> heads, String proxyIP, Integer proxyPort) {
        try {
            if (StrUtil.isBlank(url)) {
                return null;
            }
            if (heads == null || heads.isEmpty()) {
                return get(url, timeOut, proxyIP, proxyPort);
            } else {
                HttpRequest getMethod = HttpUtil.createGet(url);
                heads.entrySet().stream().forEach(entry -> {
                    getMethod.header(entry.getKey(), entry.getValue());
                });
                if (StrUtil.isNotBlank(proxyIP) && proxyPort != null && proxyPort > 0) {
                    getMethod.setProxy(getProxy(proxyIP, proxyPort));
                }
                return getMethod.timeout(timeOut > 0 ? timeOut : -1).execute().body();
            }
        } catch (Exception e) {
            log.error("ProxyConnectionUtil.get方法异常！", e);
        }
        return null;
    }

    /**
     * 代理接口请求（POST）
     *
     * @param url        接口的地址
     * @param timeOut    请求超时时间（毫秒）
     * @param jsonString 请求参数
     * @param proxyIP    代理IP
     * @param proxyPort  代理端口
     * @return 接口返回数据
     */
    public static String post(String url, int timeOut, String jsonString, String proxyIP, Integer proxyPort) {
        try {
            if (StrUtil.isBlank(url)) {
                return null;
            }
            if (StrUtil.isBlank(proxyIP) || proxyPort == null || proxyPort <= 0) {
                return HttpUtil.post(url, jsonString);
            }
            return HttpUtil.createPost(url.toString()).setProxy(getProxy(proxyIP, proxyPort)).body(jsonString).timeout(timeOut > 0 ? timeOut : -1).execute().body();
        } catch (Exception e) {
            log.error("ProxyConnectionUtil.post 异常！", e);
        }
        return null;
    }

    /**
     * 代理接口请求（POST）
     *
     * @param url        接口的地址
     * @param timeOut    请求超时时间（毫秒）
     * @param heads      请求头参数
     * @param jsonString 请求参数
     * @param proxyIP    代理IP
     * @param proxyPort  代理端口
     * @return 接口返回数据
     */
    public static String post(String url, int timeOut, Map<String, String> heads, String jsonString, String proxyIP, Integer proxyPort) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        try {
            if (heads == null || heads.isEmpty()) {
                return post(url, timeOut, jsonString, proxyIP, proxyPort);
            } else {
                HttpRequest postMethod = HttpUtil.createPost(url);
                heads.entrySet().stream().forEach(entry -> {
                    postMethod.header(entry.getKey(), entry.getValue());
                });
                if (StrUtil.isNotBlank(proxyIP) && proxyPort != null && proxyPort > 0) {
                    postMethod.setProxy(getProxy(proxyIP, proxyPort));
                }
                return postMethod.body(jsonString).timeout(timeOut > 0 ? timeOut : -1).execute().body();
            }
        } catch (Exception e) {
            log.error("ProxyConnectionUtil.post 异常！", e);
        }
        return null;
    }

    private static Proxy getProxy(String proxyIP, Integer proxyPort) {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
    }
}
