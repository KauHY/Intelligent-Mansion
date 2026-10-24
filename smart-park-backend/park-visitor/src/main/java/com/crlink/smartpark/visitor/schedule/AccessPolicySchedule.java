package com.crlink.smartpark.visitor.schedule;

import cn.hutool.core.collection.CollUtil;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class AccessPolicySchedule {

    @Resource
    private AccessPolicyService accessPolicyService;

    public void refreshAccessPolicy() {
        log.info("过期通行策略Task Start");
        List<AccessPolicy> accessPolicies = accessPolicyService.lambdaQuery()
                .eq(AccessPolicy::getStatus, StatusEnum.ENABLE.getCode())
                .eq(AccessPolicy::getExpire, DictUtil.getValue(DictConstant.YesOrNo.NO))
                .lt(AccessPolicy::getPolicyExpireTime, LocalDate.now()).list();
        if (CollUtil.isEmpty(accessPolicies)) {
            log.info("无过期通行策略");
            return;
        }
        log.info("过期通行策略Task Size={}", accessPolicies.size());
        for (AccessPolicy accessPolicy : accessPolicies) {
            accessPolicy.setExpire(DictUtil.getValue(DictConstant.YesOrNo.YES));
            accessPolicyService.updateById(accessPolicy);
            log.info("过期通行策略Task删除授权");
            Set<String> cardNos = accessPolicyService.getEmployeeCardNos(accessPolicy.getCompanyId(), accessPolicy.getTagIds());
            accessPolicyService.removeAuth(accessPolicy.getDeviceIds(), cardNos);
        }
        log.info("过期通行策略Task End");
    }

}
