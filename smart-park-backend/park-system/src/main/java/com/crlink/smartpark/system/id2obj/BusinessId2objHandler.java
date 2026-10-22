package com.crlink.smartpark.system.id2obj;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.core.id2obj.Id2objectSingleHandler;
import com.crlink.smartpark.system.entity.BusinessUser;
import com.crlink.smartpark.system.service.BusinessUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class BusinessId2objHandler implements Id2objectSingleHandler {

    @Resource
    private BusinessUserService businessUserService;

    @Override
    public String code() {
        return Id2objCodeConstant.BUSINESS_USER_NAME_CODE;
    }


    @Override
    public Map<Object, Object> ids2Objects(Set<Object> ids) {
        LambdaQueryWrapper<BusinessUser> wrapper = new LambdaQueryWrapper<> ();
        wrapper.in(BusinessUser::getId, ids);
        wrapper.select(BusinessUser::getId, BusinessUser::getUsername);
        List<BusinessUser> list = businessUserService.list(wrapper);
        Map<Object, Object> map = list.stream().collect(Collectors.toMap(BusinessUser::getId, BusinessUser::getUsername));
        return map;
    }


}
