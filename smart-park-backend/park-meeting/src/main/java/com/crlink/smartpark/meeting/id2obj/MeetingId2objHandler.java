package com.crlink.smartpark.meeting.id2obj;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.core.id2obj.Id2objectSingleHandler;
import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.service.MeetingManageService;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName: MeetingId2objHandler
 * @Package com.crlink.smartpark.core.id2obj
 * @Description: 会议室ID名字映射
 * @Version 1.0
 */
@Component
public class MeetingId2objHandler implements Id2objectSingleHandler {

    @Resource
    private MeetingManageService meetingManageService;

    @Override
    public String code() {
        return Id2objCodeConstant.MEETING_NAME_CODE;
    }


    @Override
    public Map<Object, Object> ids2Objects(Set<Object> ids) {
        LambdaQueryWrapper<MeetingManage> wrapper = new LambdaQueryWrapper<> ();
        wrapper.in(MeetingManage::getId, ids);
        wrapper.select(MeetingManage::getId, MeetingManage::getMeetingName);
        List<MeetingManage> list = meetingManageService.list(wrapper);
        Map<Object, Object> map = list.stream().collect(Collectors.toMap(MeetingManage::getId, MeetingManage::getMeetingName));
        return map;
    }


}
