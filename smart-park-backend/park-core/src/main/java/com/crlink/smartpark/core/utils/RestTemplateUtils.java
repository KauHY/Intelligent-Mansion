package com.crlink.smartpark.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class RestTemplateUtils {

    @Resource(name = "restTemplateProxy")
    private RestTemplate restTemplateProxy;

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    public <T> T getRequest(String url, Class<T> responseType, Map<String, Object> params) {
        log.info("GET请求远程服务RUL:{},请求参数:{}", url, params);
        T result = restTemplate.getForObject(url, responseType, params);
        log.info("GET请求远程服务RUL:{},返回对象:{}", url, result);
        return result;
    }

    public <T> T postRequest(String url, Class<T> responseType, Object requestBody) {
        log.info("POST请求远程服务RUL:{},body:{}", url, requestBody);
        T result = restTemplate.postForObject(url, requestBody, responseType);
        log.info("POST请求远程服务RUL:{},返回对象:{}", url, result);
        return result;
    }

    public <T> T getRequestProxy(String url, Class<T> responseType, Map<String, Object> params) {
        log.info("GET(代理)请求远程服务RUL:{},请求参数:{}", url, params);
        T result = restTemplateProxy.getForObject(url, responseType, params);
        log.info("GET(代理)请求远程服务RUL:{},返回对象:{}", url, result);
        return result;
    }

    public <T> T postRequestProxy(String url, Class<T> responseType, Object requestBody) {
        log.info("POST(代理)请求远程服务RUL:{},body:{}", url, requestBody);
        T result = restTemplateProxy.postForObject(url, requestBody, responseType);
        log.info("POST(代理)请求远程服务RUL:{},返回对象:{}", url, result);
        return result;
    }

}
