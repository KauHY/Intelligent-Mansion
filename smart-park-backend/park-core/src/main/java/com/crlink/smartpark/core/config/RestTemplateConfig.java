package com.crlink.smartpark.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class RestTemplateConfig {

    @Value("${proxy.ip:crproxy.crcloud.com}")
    private String proxyIp;

    @Value("${proxy.port:80}")
    private Integer proxyPort;

    @Value("${proxy.enable:false}")
    private Boolean proxyEnable;

    @Bean("restTemplate")
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(10000);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(10000);
        return new RestTemplate(httpRequestFactory);
    }

    @Bean("restTemplateProxy")
    public RestTemplate restTemplateProxy() {
        return proxyEnable ? new RestTemplate(this.getSimpleClientHttpRequestFactory()) : this.restTemplate();
    }

    private SimpleClientHttpRequestFactory getSimpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(proxyIp, proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
        httpRequestFactory.setProxy(proxy);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(10000);
        return httpRequestFactory;
    }

}
