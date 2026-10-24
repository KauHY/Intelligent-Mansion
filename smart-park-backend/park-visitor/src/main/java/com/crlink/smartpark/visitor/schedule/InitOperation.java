package com.crlink.smartpark.visitor.schedule;

import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 初始化操作
 *
 */
@Slf4j
@Component
public class InitOperation {

    @Resource
    private AccessPolicyService accessPolicyService;

    @PostConstruct
    public void init() {
        log.info("初始化通行策略Start");
        List<AccessPolicy> accessPolicyList = accessPolicyService.lambdaQuery()
                .eq(AccessPolicy::getStatus, StatusEnum.ENABLE.getCode())
                .eq(AccessPolicy::getExpire, StatusEnum.DISABLE.getCode()).list();
        for (AccessPolicy accessPolicy : accessPolicyList) {
            accessPolicyService.refreshPolicyEmployee(accessPolicy);
        }
        log.info("初始化通行策略{}条End", accessPolicyList.size());
    }
}
