package com.crlink.smartpark.visitor.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.visitor.entity.ApartmentPerson;
import com.crlink.smartpark.visitor.service.ApartmentPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class ApartmentPersonSchedule {

    @Resource
    private ApartmentPersonService apartmentPersonService;

    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    public void refreshApartmentPerson() {
        log.info("开始检查过期公寓人员");
        List<ApartmentPerson> apartmentPeoples = apartmentPersonService.lambdaQuery()
                .eq(ApartmentPerson::getStatus, StatusEnum.ENABLE.getCode())
                .eq(ApartmentPerson::getExpire, DictUtil.getValue(DictConstant.YesOrNo.NO))
                .lt(ApartmentPerson::getEffectEndDate, LocalDate.now()).list();
        if (CollUtil.isEmpty(apartmentPeoples)) {
            log.info("无过期公寓人员");
            return;
        }
        log.info("存在过期公寓人员，开始处理，{}", JSONUtil.toJsonStr(apartmentPeoples));
        for (ApartmentPerson apartmentPerson : apartmentPeoples) {
            apartmentPerson.setExpire(DictUtil.getValue(DictConstant.YesOrNo.YES));
            apartmentPerson.setStatus(StatusEnum.DISABLE.getCode());
            //刷新门禁权限
            deviceOperateProvider.refreshEmployeeDoorAccessAuthorities(apartmentPerson.getEmployeeId());
            //刷新智能锁授权
            deviceOperateProvider.refreshSmartLockAuth(apartmentPerson.getEmployeeId(), true);
            apartmentPersonService.updateById(apartmentPerson);
        }
    }


}
