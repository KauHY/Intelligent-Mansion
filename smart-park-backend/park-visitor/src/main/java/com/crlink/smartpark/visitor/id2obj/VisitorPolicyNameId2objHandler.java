package com.crlink.smartpark.visitor.id2obj;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.core.id2obj.Id2objectSingleHandler;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VisitorPolicyNameId2objHandler implements Id2objectSingleHandler {

    @Resource
    private AccessPolicyService accessPolicyService;

    @Override
    public String code() {
        return Id2objCodeConstant.VISITOR_ACCESS_POLICY_NAME_CODE;
    }

    @Override
    public Map<Object, Object> ids2Objects(Set<Object> ids) {
        LambdaQueryWrapper<AccessPolicy> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AccessPolicy::getId, ids);
        wrapper.select(AccessPolicy::getId, AccessPolicy::getPolicyName);
        List<AccessPolicy> list = accessPolicyService.list(wrapper);
        Map<Object, Object> map = list.stream().collect(Collectors.toMap(AccessPolicy::getId, AccessPolicy::getPolicyName));
        return map;
    }

}
