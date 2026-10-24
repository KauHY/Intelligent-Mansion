package com.crlink.smartpark.common.controller;

import cn.hutool.http.HttpUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @GetMapping
    public String proxy(@RequestParam("url") String url) {
        return HttpUtil.get(url);
    }
}
