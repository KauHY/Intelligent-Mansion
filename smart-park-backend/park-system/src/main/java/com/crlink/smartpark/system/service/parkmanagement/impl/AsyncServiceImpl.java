package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.core.utils.FileUtil;
import com.crlink.smartpark.device.dto.PersonDeviceDTO;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.entity.Employee;
import com.crlink.smartpark.system.service.parkmanagement.AsyncService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeDeviceService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 * */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private FileProvider fileProvider;
    @Resource
    private DeviceOperateProvider deviceOperateProvider;
    @Autowired
    private EmployeeService employeeService;
    @Resource
    private EmployeeDeviceService employeeDeviceService;

    @Override
    @Async
    public void uploadMinio(String fileKey, File file) {
        fileProvider.upload(fileKey, FileUtil.file2MultiPartFile(file));
    }

    @Override
    public Map<Long, String> uploadIcc(PersonDeviceDTO dto, File file) {
        Map<Long, String> result = Maps.newHashMap();
        // 1.上传文件
        String picImgUrl = deviceOperateProvider.uploadImg(FileUtil.file2MultiPartFile(file));
        if (StringUtils.isBlank(picImgUrl) || picImgUrl.startsWith("ERROR:")) {
            //return "大华上传图片异常！";
            result.put(dto.getDevicePersonId(), StringUtils.isBlank(picImgUrl) ? "大华上传图片异常" : picImgUrl);
            return result;
        }
        dto.setPicImgUrl(picImgUrl);
        // 2.下发大华
        result.put(dto.getDevicePersonId(), deviceOperateProvider.addPersonAndCardNew(dto));
        return result;
    }
    @Async
    @Override
    public void uploadIcc(List<PersonDeviceDTO> personList) {
        if (CollectionUtil.isEmpty(personList)) {
            return;
        }
        for (PersonDeviceDTO personDeviceDTO : personList) {
            File picFile = new File(personDeviceDTO.getPicFileName());
            String errMsg = "";
            String picImgUrl = "";
            if (picFile.exists()) {
                // 1.上传文件
                picImgUrl = deviceOperateProvider.uploadImg(FileUtil.file2MultiPartFile(picFile));
                if (StringUtils.isBlank(picImgUrl) || picImgUrl.startsWith("ERROR:")) {
                    errMsg = "员工数据下发大华异常，上传文件失败！";
                }
            } else {
                errMsg = "员工数据下发大华异常，文件不存在！";
            }
            if (StringUtils.isNotBlank(errMsg)) {
                recordErrorMsg(personDeviceDTO.getDevicePersonId(), errMsg);
                continue;
            }
            // 2.ICC平台创建人员
            personDeviceDTO.setPicImgUrl(picImgUrl);
            errMsg = deviceOperateProvider.addPersonAndCardNew(personDeviceDTO);
            if (StringUtils.isNotBlank(errMsg)) {
                recordErrorMsg(personDeviceDTO.getDevicePersonId(), errMsg);
            } else {
                Employee employee = new Employee();
                employee.setCompanyId(personDeviceDTO.getCompanyId());
                employee.setTagIds(personDeviceDTO.getTagIds());
                employee.setCardNo(personDeviceDTO.getCardNo());
                employeeDeviceService.pushEmployee(employee);
            }
        }
    }

    private void recordErrorMsg(Long devicePersionId, String errMsg) {
        employeeService.update(Wrappers.<Employee>lambdaUpdate()
                .eq(Employee::getDevicePersonId, devicePersionId)
                .set(Employee::getStatus, StatusEnum.DISABLE.code)
                .set(Employee::getRemark, errMsg));
    }
}
