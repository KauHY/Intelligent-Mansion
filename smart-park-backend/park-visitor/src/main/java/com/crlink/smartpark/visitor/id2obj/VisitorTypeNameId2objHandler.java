package com.crlink.smartpark.visitor.id2obj;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.core.id2obj.Id2objectSingleHandler;
import com.crlink.smartpark.visitor.entity.VisitorType;
import com.crlink.smartpark.visitor.service.VisitorTypeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VisitorTypeNameId2objHandler implements Id2objectSingleHandler {

    @Resource
    private VisitorTypeService visitorTypeService;

    @Override
    public String code() {
        return Id2objCodeConstant.VISITOR_TYPE_NAME_CODE;
    }

    @Override
    public Map<Object, Object> ids2Objects(Set<Object> ids) {
        LambdaQueryWrapper<VisitorType> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(VisitorType::getId, ids);
        wrapper.select(VisitorType::getId, VisitorType::getVisitorTypeName);
        List<VisitorType> list = visitorTypeService.list(wrapper);
        Map<Object, Object> map = list.stream().collect(Collectors.toMap(VisitorType::getId, VisitorType::getVisitorTypeName));
        return map;
    }

}
