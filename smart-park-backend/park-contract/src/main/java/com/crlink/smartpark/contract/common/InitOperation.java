package com.crlink.smartpark.contract.common;

import com.crlink.smartpark.contract.entity.Contract;
import com.crlink.smartpark.contract.service.ContractService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description: 服务初始化操作
 */
@Service
@Slf4j
public class InitOperation {

    @Resource
    private ContractService contractService;


    @PostConstruct
    public void contractNoInit() {
        for (ContractConstants.ContractNoType value : ContractConstants.ContractNoType.values()) {
            Contract one = contractService.lambdaQuery().select(Contract::getNo)
                    .eq(Contract::getType, value.type).orderByDesc(Contract::getNo).last("limit 1").one();
            if (Objects.nonNull(one)) {
                String[] split = one.getNo().split("-");
                Integer count = new Integer(split[4]);
                value.getAtomicInteger().set(count);
            }
        }

    }
}
