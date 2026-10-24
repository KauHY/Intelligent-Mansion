package com.crlink.smartpark.device.controller;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/dahua/icc")
public class DahuaIccController {

    /**
     * 封装完整图片路径
     *
     * @param urls
     * @return
     */
    @PostMapping(value = "/getFullOssUrls")
    public Map<String, String> getFullOssUrls(@RequestBody Set<String> urls) {
        Map<String, String> map = new HashMap<>(urls.size());
        if (CollUtil.isEmpty(urls)) {
            return map;
        }
//        urls.forEach(url -> map.put(url, IccUtil.buildFullOssUrlWithoutToken(url)));
        return map;
    }

    /**
     * 封装完整图片路径
     *
     * @param urls
     * @return
     */
    @PostMapping(value = "/getFullParkUrls")
    public Map<String, String> getFullParkUrls(@RequestBody Set<String> urls) {
        Map<String, String> map = new HashMap<>(urls.size());
        if (CollUtil.isEmpty(urls)) {
            return map;
        }
        urls.forEach(url -> {
            StringBuilder sb = new StringBuilder();
//            sb.append(IccProfile.URL_SCHEME);
            sb.append(PARK_PREFIX);
            sb.append(url);
            map.put(url, sb.toString());
        });
        return map;
    }

    private static final String PARK_PREFIX = "/evo-apigw/ipms/imageConvert/originalImage?fileUrl=";

}
