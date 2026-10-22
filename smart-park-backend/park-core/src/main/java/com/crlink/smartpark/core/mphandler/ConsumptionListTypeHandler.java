package com.crlink.smartpark.core.mphandler;

import com.alibaba.fastjson.TypeReference;
import com.crlink.smartpark.device.dto.Consumption;

import java.util.List;

/**
 * @Package com.crlink.smartpark.core.mphandler
 * @Description: 企业或房屋用电或用水映射对象
 * @Version 1.0
 */
public class ConsumptionListTypeHandler extends ListTypeHandler<Consumption> {
    @Override
    protected TypeReference<List<Consumption>> specificType() {
        return new TypeReference<List<Consumption>>() {
        };
    }
}
