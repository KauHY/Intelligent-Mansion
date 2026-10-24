package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.constants.enums.DevicePersonTypeEnum;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.device.dto.PersonDeviceAddAndAuthorityDTO;
import com.crlink.smartpark.device.dto.PersonDeviceAuthorityDTO;
import com.crlink.smartpark.device.dto.PersonDeviceDTO;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.entity.Employee;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeDeviceService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeService;
import com.crlink.smartpark.visitor.feign.AccessPolicyProvider;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** * @desc
 */
@Service
@Slf4j
public class EmployeeDeviceServiceImpl implements EmployeeDeviceService {

    @Resource
    private AccessPolicyProvider accessPolicyProvider;

    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private FileProvider fileProvider;

    @Override
    public String addEmployee(Employee employee) {
        return deviceOperateProvider.addPersonAndCard(buildPersonDTO(employee));
    }

    @Override
    @Async
    public void batchAddEmployees(List<Employee> employees) {
        if (CollUtil.isEmpty(employees)) {
            return;
        }
        for (Employee employee : employees) {
            addEmployee(employee);
            try {
                //防止下发速率过快
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                log.info("线程唤醒！", e);
            }
        }
    }

    @Override
    public String updateEmployee(Employee employee) {
        AssertUtil.requireTrue(personExistById(employee.getDevicePersonId()), "60014");
        PersonDeviceDTO personDTO = buildPersonDTO(employee);
        return deviceOperateProvider.updatePerson(personDTO);
        //} else {
        //如果设备人员不存在，刷新设备人员ID，如果旧的人员ID在ICC平台添加过并被删除了，重复使用旧的人员ID会导致人脸特征值提取失败（大华ICC的BUG！）
        //employee.setDevicePersonId(NoAndIdUtil.generateDevicePersonId());
        //employeeService.updateById(employee);
        //personDTO.setDevicePersonId(employee.getDevicePersonId());
        //return deviceOperateProvider.addPersonAndCard(personDTO);
        //}
    }

    /**
     * 下发人员到设备
     *
     * @param employee
     */
    @Override
    public void pushEmployee(Employee employee) {
        ThreadUtil.sleep(5000L);
        PersonDeviceAuthorityDTO dto = buildPushDTO(employee);
        deviceOperateProvider.pushPersons2Devices(dto);
    }

    /**
     * 该方法没有被调用
     *
     * @param employee
     */
    @Deprecated
    @Override
    @Async
    public void addAndPushEmployee(Employee employee) {
        PersonDeviceAuthorityDTO authorityDTO = buildPushDTO(employee);
        PersonDeviceDTO personDTO = buildPersonDTO(employee);
        PersonDeviceAddAndAuthorityDTO dto = new PersonDeviceAddAndAuthorityDTO();
        dto.setPersonDeviceAuthorityDTO(authorityDTO);
        dto.setPersonDTO(personDTO);
        deviceOperateProvider.addPersonAndPush(dto);
    }

    /**
     * 该方法没有被调用
     *
     * @param employee
     */
    @Deprecated
    @Override
    @Async
    public void updateAndPushEmployee(Employee employee) {
        AssertUtil.requireTrue(personExist(employee.getMobile()), "60011");
        PersonDeviceAuthorityDTO authorityDTO = buildPushDTO(employee);
        PersonDeviceDTO personDTO = buildPersonDTO(employee);
        PersonDeviceAddAndAuthorityDTO dto = new PersonDeviceAddAndAuthorityDTO();
        dto.setPersonDTO(personDTO);
        dto.setPersonDeviceAuthorityDTO(authorityDTO);
        deviceOperateProvider.updatePersonAndPush(dto);
        //if (personExist(employee.getDevicePersonId())) {
        //
        //} else {
        //    //如果设备人员不存在，刷新设备人员ID，如果旧的人员ID在ICC平台添加过并被删除了，重复使用旧的人员ID会导致人脸特征值提取失败（大华ICC的BUG！）
        //    employee.setDevicePersonId(NoAndIdUtil.generateDevicePersonId());
        //    employeeService.updateById(employee);
        //    dto.getPersonDTO().setDevicePersonId(employee.getDevicePersonId());
        //    deviceOperateProvider.addPersonAndPush(dto);
        //}
    }

    @NotNull
    private PersonDeviceDTO buildPersonDTO(Employee employee) {
        byte[] imgBytes = fileProvider.downloadByte(employee.getPics());
        String imgFileName = fileProvider.getFileDetailByKeys(Sets.newHashSet(employee.getPics())).getData().get(employee.getPics()).getFileName();
        AssertUtil.requireTrue(imgBytes != null && StrUtil.isNotBlank(imgFileName), "10029");
        PersonDeviceDTO personDTO = new PersonDeviceDTO();
        personDTO.setDevicePersonId(employee.getDevicePersonId());
        personDTO.setPersonName(employee.getName());
        personDTO.setMobilePhone(employee.getMobile());
        personDTO.setCardNo(employee.getCardNo());
        LocalDateTime now = LocalDateTime.now();
        personDTO.setStartTime(now);
        personDTO.setEndTime(now.plusYears(10));
        personDTO.setPersonType(DevicePersonTypeEnum.NORMAL);
        personDTO.setPicBytes(imgBytes);
        personDTO.setPicFileName(imgFileName);
        personDTO.setFingers(employee.getFingers());
        return personDTO;
    }

    @Override
    public String delEmployee(Long personId, String cardNo) {
        String result = null;
        if (Objects.nonNull(personId)) {
            result = deviceOperateProvider.delPerson(personId);
        }
        if (Objects.isNull(result) && StrUtil.isNotBlank(cardNo)) {
            result = deviceOperateProvider.delCard(cardNo);
        }
        return result;
    }

    @Override
    public Boolean personExistById(Long personId) {
        return deviceOperateProvider.personExistById(personId);
    }

    @Override
    public Boolean personExist(String telephone) {
        return deviceOperateProvider.personExist(telephone);
    }

    @Override
    public String updateStatus(String employeeId, Integer status) {
        Employee employee = employeeService.getById(employeeId);
        AssertUtil.requireTrue(Objects.nonNull(employee) && Objects.nonNull(employee.getDevicePersonId()), "10003");
        return deviceOperateProvider.updatePersonStatus(employee.getDevicePersonId(), status);
    }

    @Override
    public String enablePerson(List<Long> ids) {
        return deviceOperateProvider.enablePerson(ids);
    }

    @Override
    public String unablePerson(List<Long> ids) {
        return deviceOperateProvider.unablePerson(ids);
    }

    private PersonDeviceAuthorityDTO buildPushDTO(Employee employee) {
        Set<String> deviceIds = accessPolicyProvider.getAccessDeviceIdsByEmployeeInfo(employee.getCompanyId(), employee.getTagIds());
        PersonDeviceAuthorityDTO dto = new PersonDeviceAuthorityDTO();
        Map<String, Set<String>> pushMap = new HashMap<>();
        pushMap.put(employee.getCardNo(), deviceIds);
        dto.setCardNoDevicesPushMap(pushMap);
        return dto;
    }

}
