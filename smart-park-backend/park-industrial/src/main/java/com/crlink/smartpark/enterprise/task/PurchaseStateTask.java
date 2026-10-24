package com.crlink.smartpark.enterprise.task;

import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.enterprise.entity.PurchaseService;
import com.crlink.smartpark.enterprise.service.PurchaseServiceService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PurchaseStateTask {
    @Resource
    private PurchaseServiceService purchaseServiceService;


    public static final int pageSize = 1_000;

    public void stateUpdate() {
        Integer count = purchaseServiceService.lambdaQuery()
                .in(PurchaseService::getState, Sets.newHashSet(DictConstant.PurchaseState.IN_PROGRESS.getCode(),
                        DictConstant.PurchaseState.TO_BE_PUBLISHED.getCode()))
                .count();
        int times = count / pageSize + 1;
        int i = 1;
        LocalDateTime now = LocalDateTime.now();
        while (i <= times) {
            PageHelper.startPage(i, pageSize);
            List<PurchaseService> list = purchaseServiceService.lambdaQuery()
                    .select(PurchaseService::getId, PurchaseService::getEffectiveEndTime)
                    .in(PurchaseService::getState, Sets.newHashSet(DictConstant.PurchaseState.IN_PROGRESS.getCode(), DictConstant.PurchaseState.TO_BE_PUBLISHED.getCode())).list();
            list.forEach(e -> e.setState(now.isAfter(e.getEffectiveEndTime()) ? DictConstant.PurchaseState.EXPIRED.getCode()
                    : DictConstant.PurchaseState.IN_PROGRESS.getCode()));
            purchaseServiceService.updateBatchById(list);
            i++;
        }
    }

}
