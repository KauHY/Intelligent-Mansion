package com.crlink.smartpark.common.feign;

import com.crlink.smartpark.common.dto.NumberSeqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 编号生成器
 * @Created by ZHOUXINJIAN3
 */
@FeignClient(name = "park-common", path = "/smartPark/park-common/number", contextId = "NumberProvider")
public interface NumberProvider {

    @RequestMapping(value = "/getNumberSeq", method = RequestMethod.GET)
    public NumberSeqDTO getNumberSeq(@RequestParam("numberName") String numberName);
}
