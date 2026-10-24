package com.crlink.smartpark.customer.id2obj;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.crlink.smartpark.customer.entity.MeetingRent;
import com.crlink.smartpark.customer.service.MeetingRentService;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.core.id2obj.Id2objectSingleHandler;

/**
 * @ClassName: MeetingId2objHandler
 * @Package com.crlink.smartpark.core.id2obj
 * @Description: 会议室ID名字映射
 * @Version 1.0
 */
@Component
public class CusMeetingId2objHandler implements Id2objectSingleHandler {

    @Resource
    private MeetingRentService meetingRentService;

    @Override
    public String code() {
        return Id2objCodeConstant.CUS_MEETING_NAME_CODE;
    }


    @Override
    public Map<Object, Object> ids2Objects(Set<Object> ids) {
        LambdaQueryWrapper<MeetingRent> wrapper = new LambdaQueryWrapper<> ();
        wrapper.in(MeetingRent::getId, ids);
        wrapper.select(MeetingRent::getId, MeetingRent::getMeetingName);
        List<MeetingRent> list = meetingRentService.list(wrapper);
        Map<Object, Object> map = list.stream().collect(Collectors.toMap(MeetingRent::getId, MeetingRent::getMeetingName));
        return map;
    }


}
