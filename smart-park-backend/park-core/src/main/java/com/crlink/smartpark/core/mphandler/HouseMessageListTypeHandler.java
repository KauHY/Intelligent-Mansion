package com.crlink.smartpark.core.mphandler;

import com.crlink.smartpark.contract.dto.HouseMessage;
import com.alibaba.fastjson.TypeReference;
import java.util.List;

/**
 * @ClassName: HouseMessageListTypeHandler
 * @Package com.crlink.smartpark.core.mphandler
 * @Description: 合同房屋信息映射对象
 * @Version 1.0
 */
public class HouseMessageListTypeHandler  extends ListTypeHandler<HouseMessage> {
    @Override
    protected TypeReference<List<HouseMessage>> specificType() {
        return new TypeReference<List<HouseMessage>>() {
        };
    }
}
