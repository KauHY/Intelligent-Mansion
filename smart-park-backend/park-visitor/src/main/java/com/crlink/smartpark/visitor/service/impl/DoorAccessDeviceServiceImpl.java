package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.constants.enums.DevicePersonTypeEnum;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.device.dto.DeviceCardDTO;
import com.crlink.smartpark.device.dto.PersonDeviceAuthorityDTO;
import com.crlink.smartpark.device.dto.PersonDeviceDTO;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.service.DoorAccessDeviceService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class DoorAccessDeviceServiceImpl implements DoorAccessDeviceService {

    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    @Resource
    private FileProvider fileProvider;

    /**
     * 推送访客到设备
     *
     * @param visitorRecord
     * @return
     */
    @Override
    public String pushVisitor(VisitorRecord visitorRecord) {
        String errMsg = null;
        PersonDeviceDTO dto = buildPersonDTO(visitorRecord);
        if (deviceOperateProvider.personExistById(dto.getDevicePersonId())) {
            errMsg = deviceOperateProvider.updatePerson(dto);
        } else {
            errMsg = deviceOperateProvider.addPersonAndCard(dto);
        }
        return errMsg;
    }

    /**
     * 生成人员id 20220808
     *
     * @return id
     */
    @Override
    public Long generatePersonId() {
        return deviceOperateProvider.generatePersonId();
    }

    /**
     * 生成卡片id 20220808
     *
     * @return id
     */
    @Override
    public Long generateCardId() {
        return deviceOperateProvider.generateCardId();
    }

    /**
     * 新增人员和卡片 20220808
     *
     * @param visitorRecord
     * @return
     */
    @Override
    public String addPersonAndCard(VisitorRecord visitorRecord) {
        AssertUtil.requireTrue(!deviceOperateProvider.personExist(visitorRecord.getVisitorTel()), "60012");
        PersonDeviceDTO dto = buildPersonDTO(visitorRecord);
        String errMsg = deviceOperateProvider.addPersonAndCard(dto);
        if (Objects.isNull(errMsg)) {
            ThreadUtil.sleep(5000L);
            Set<String> deviceIds = visitorRecord.getDeviceIds();
            Map<String, Set<String>> pushMap = new HashMap<>();
            pushMap.put(dto.getCardNo(), deviceIds);
            PersonDeviceAuthorityDTO pushDTO = new PersonDeviceAuthorityDTO();
            pushDTO.setCardNoDevicesPushMap(pushMap);
            deviceOperateProvider.pushPersons2Devices(pushDTO);
        }
        return errMsg;
    }

    /**
     * 更新访客卡片信息
     *
     * @param visitorRecord
     * @return
     */
    @Override
    public String updateVisitorCard(VisitorRecord visitorRecord) {
        AssertUtil.requireTrue(deviceOperateProvider.cardExist(visitorRecord.getCardNo()), "60013");
        DeviceCardDTO cardDTO = buildCardDTO(visitorRecord);
        return deviceOperateProvider.updateCard(cardDTO);
    }

    /**
     * 删除访客设备权限
     *
     * @param visitorRecord
     * @return
     */
    @Override
    public String removeVisitorDeviceAuth(VisitorRecord visitorRecord) {
        return deviceOperateProvider.delPerson(visitorRecord.getDevicePersonId());
    }

    private DeviceCardDTO buildCardDTO(VisitorRecord visitorRecord) {
        String personId = String.valueOf(visitorRecord.getDevicePersonId());
        DeviceCardDTO cardDTO = new DeviceCardDTO();
        cardDTO.setCardNo(visitorRecord.getCardNo());
        cardDTO.setStartTime(visitorRecord.getVisitDate().atTime(0, 0, 0));
        cardDTO.setEndTime(visitorRecord.getSignOutTime());
        cardDTO.setPersonId(personId);
        return cardDTO;
    }

    @NotNull
    private PersonDeviceDTO buildPersonDTO(VisitorRecord visitorRecord) {
        byte[] imgBytes = fileProvider.downloadByte(visitorRecord.getVisitorFaceImg());
        String imgFileName = fileProvider.getFileDetailByKeys(Sets.newHashSet(visitorRecord.getVisitorFaceImg())).getData().get(visitorRecord.getVisitorFaceImg()).getFileName();
        AssertUtil.requireTrue(imgBytes != null && StrUtil.isNotBlank(imgFileName), "10001");
        PersonDeviceDTO personDTO = new PersonDeviceDTO();
        personDTO.setDevicePersonId(visitorRecord.getDevicePersonId());
        personDTO.setPersonName(visitorRecord.getVisitorName());
        personDTO.setMobilePhone(visitorRecord.getVisitorTel());
        personDTO.setCardNo(visitorRecord.getCardNo());
        personDTO.setStartTime(visitorRecord.getVisitDate().atTime(0, 0, 0));
        personDTO.setEndTime(visitorRecord.getSignOutTime());
        personDTO.setPersonType(DevicePersonTypeEnum.VISITOR);
        personDTO.setPicBytes(imgBytes);
        personDTO.setPicFileName(imgFileName);
        return personDTO;
    }

}
