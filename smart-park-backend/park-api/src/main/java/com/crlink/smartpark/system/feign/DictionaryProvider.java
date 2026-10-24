package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 * <p>
 * //smartPark/park-system/
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/dictionary", contextId = "DictionaryProvider")
public interface DictionaryProvider {

    @RequestMapping(value = "/listDictByTypes", method = RequestMethod.POST)
    ResponseResult<Map<String, List<DictVO>>> listDictByTypes(@RequestBody String[] types);

    @ApiOperation(value = "字典数据初始化", tags = "内部接口，用于初始化字典数据")
    @GetMapping("/initDictionary")
    Map<String, Map<String, DictVO>> initDictionary();
}
