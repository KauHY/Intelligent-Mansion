package com.crlink.smartpark.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.system.service.common.DictionaryService;
import com.crlink.smartpark.system.vo.DictVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 * */
@Slf4j
@RestController
@Api(tags = "字典表数据")
@RequestMapping("/dictionary")
public class DictionaryController {

    /**
     * 字典表
     */
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 【字典表】根据类型查询
     *
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "根据类型查询字典", tags = "字典表")
    @PostMapping({"/listDictByTypes", "/wx/listDictByTypes"})
    public ResponseResult<Map<String, Object>> listDictByTypes(@ApiParam(value = "类型", required = true) @RequestBody String[] types) {
        Map<String, Object> result = dictionaryService.listDictionaryByTypes(Lists.newArrayList(types));
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "字典数据初始化", tags = "内部接口，用于初始化字典数据")
    @GetMapping("/initDictionary")
    public Map<String, Map<String, DictVO>> initDictionary() {
        Map<String, Map<String, DictVO>> result = dictionaryService.initDictionary();
        return result;
    }

    @ApiOperation(value = "字典数据Redis初始化", tags = "内部接口，用于初始化字典Redis数据")
    @GetMapping("/initDictionaryForRedis")
    public void initDictionaryForRedis() {
        Map<String, Map<String, DictVO>> typeMap = dictionaryService.initDictionary();
        if (CollectionUtil.isEmpty(typeMap)) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            for (Map.Entry<String, Map<String, DictVO>> entry : typeMap.entrySet()) {
                redisUtil.setObject(Constants.Redis.DICTIONARY_TYPE_PREFIX.getBuildCode(entry.getKey()), entry.getValue());
            }
        });
    }


    ////String uri = URLUtil.getPath(request.getRequestURI());
    //@ApiOperation(value = "test", tags = "字典表")
    //@GetMapping("/test")
    //public ResponseResult<String> test(HttpServletRequest request) {
    //    String uri = URLUtil.getPath(request.getRequestURI());
    //    System.out.println(uri);
    //    return ResponseResult.success(uri);
    //}
}

