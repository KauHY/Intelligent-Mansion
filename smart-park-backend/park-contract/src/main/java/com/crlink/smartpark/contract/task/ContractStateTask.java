package com.crlink.smartpark.contract.task;

import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.contract.entity.Contract;
import com.crlink.smartpark.contract.service.ContractService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ContractStateTask {
    @Resource
    private ContractService contractService;

    public static final int pageSize = 1_000;

    public void stateUpdate() {
        Integer count = contractService.lambdaQuery()
                .in(Contract::getState, Sets.newHashSet(DictConstant.ContractState.TO_BE_EFFECTIVE.getCode(),
                        DictConstant.ContractState.IN_EFFECT.getCode()))
                .count();
        int times = count / pageSize + 1;
        int i = 1;
        while (i <= times) {
            PageHelper.startPage(i, pageSize);
            List<Contract> list = contractService.lambdaQuery()
                    .select(Contract::getId, Contract::getStartDate, Contract::getEndDate, Contract::getUpdateTime)
                    .in(Contract::getState, Sets.newHashSet(DictConstant.ContractState.TO_BE_EFFECTIVE.getCode(),
                            DictConstant.ContractState.IN_EFFECT.getCode())).list();
            list.forEach(e -> e.setState(betweenDateStartAndEnd(e.getStartDate(), e.getEndDate())));
            contractService.updateBatchById(list);
            i++;
        }
    }

    /**
     * 自动生成水电滞纳金费用
     *
     * @return
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void stateWithdrawnUpdate() {
        Integer count = contractService.lambdaQuery()
                .in(Contract::getState, DictConstant.ContractState.IN_EFFECT.getCode())
                .count();
        int times = count / pageSize + 1;
        int i = 1;
        while (i <= times) {
            PageHelper.startPage(i, pageSize);
            List<Contract> list = contractService.lambdaQuery()
                    .select(Contract::getId, Contract::getStartDate, Contract::getEndDate, Contract::getUpdateTime)
                    .in(Contract::getState, DictConstant.ContractState.IN_EFFECT.getCode()).list();
            list.forEach(e -> {
                if (e.getWithdrawDate() != null) {
                    if (LocalDate.now().isAfter(e.getWithdrawDate()) || e.getWithdrawDate().equals(LocalDate.now())) {
                        e.setState(DictConstant.ContractState.WITHDRAWN.getCode());
                    }
                }
            });
            contractService.updateBatchById(list);
            i++;
        }
    }

    /**
     * @return 0-小于start,1-大于等于start&小于等于end，2-大于end
     */
    public static int betweenDateStartAndEnd(LocalDate start, LocalDate end) {
        LocalDate nowTime = LocalDate.now();
        if (nowTime.isBefore(start)) {
            return DictConstant.ContractState.TO_BE_EFFECTIVE.getCode();
        }
        if (nowTime.isAfter(end)) {
            return DictConstant.ContractState.EXPIRED.getCode();
        }
        return DictConstant.ContractState.IN_EFFECT.getCode();
    }
}
