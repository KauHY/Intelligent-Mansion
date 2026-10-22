package com.crlink.smartpark.device.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 设备信息表 服务类
 * </p>
 * */
@FeignClient(name = "park-device", path = "/smartPark/park-device/dahua/icc", contextId = "DahuaIccProvider")
public interface DahuaIccProvider {

    /**
     * 封装完整图片路径
     *
     * @param urls
     * @return
     */
    @PostMapping(value = "/getFullOssUrls")
    Map<String, String> getFullOssUrls(@RequestBody Set<String> urls);

    /**
     * 封装完整图片路径
     *
     * @param urls
     * @return
     */
    @PostMapping(value = "/getFullParkUrls")
    Map<String, String> getFullParkUrls(@RequestBody Set<String> urls);

}
