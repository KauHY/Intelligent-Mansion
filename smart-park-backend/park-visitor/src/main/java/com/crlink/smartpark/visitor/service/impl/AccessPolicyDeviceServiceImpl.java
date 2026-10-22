package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.device.dto.PersonDeviceAuthorityDTO;
import com.crlink.smartpark.device.feign.CardProvider;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.service.AccessPolicyDeviceService;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

//@Service
public class AccessPolicyDeviceServiceImpl implements AccessPolicyDeviceService {

    //@Resource
    //private AccessPolicyService accessPolicyService;
    //
    //@Resource
    //private EmployeeProvider employeeProvider;
    //
    //@Resource
    //private DeviceOperateProvider deviceOperateProvider;
    //
    //@Resource
    //private CardProvider cardProvider;
    //
    //@Override
    //@Async
    //public void refreshAccessPolicy(List<AccessPolicy> beforeAccessPolicies, String parkOrgId) {
    //    List<AccessPolicy> curAccessPolicies = accessPolicyService.getAllByParkOrgId(parkOrgId);
    //    Map<String, Set<String>> beforeDeviceCardNosMap = getDeviceCardNosMap(beforeAccessPolicies);
    //    Map<String, Set<String>> curDeviceCardNosMap = getDeviceCardNosMap(curAccessPolicies);
    //    Map<String, Set<String>> needAddDeviceCardNosMap = new HashMap<>();
    //    Map<String, Set<String>> needDelDeviceCardNosMap = new HashMap<>();
    //    Set<String> allDeviceIds = new HashSet<>();
    //    allDeviceIds.addAll(beforeDeviceCardNosMap.keySet());
    //    allDeviceIds.addAll(curDeviceCardNosMap.keySet());
    //    for (String deviceId : allDeviceIds) {
    //        if (!needAddDeviceCardNosMap.containsKey(deviceId)) {
    //            needAddDeviceCardNosMap.put(deviceId, new HashSet<>());
    //        }
    //        if (!needDelDeviceCardNosMap.containsKey(deviceId)) {
    //            needDelDeviceCardNosMap.put(deviceId, new HashSet<>());
    //        }
    //        Set<String> beforeCardNos = beforeDeviceCardNosMap.containsKey(deviceId) ? beforeDeviceCardNosMap.get(deviceId) : new HashSet<>();
    //        Set<String> curCardNos = curDeviceCardNosMap.containsKey(deviceId) ? curDeviceCardNosMap.get(deviceId) : new HashSet<>();
    //        Collection<String> sameCardNos = CollUtil.intersection(beforeCardNos, curCardNos);
    //        //需要删除的卡号
    //        beforeCardNos.removeAll(sameCardNos);
    //        //需要新增的卡号
    //        curCardNos.removeAll(sameCardNos);
    //        needDelDeviceCardNosMap.get(deviceId).addAll(beforeCardNos);
    //        needAddDeviceCardNosMap.get(deviceId).addAll(curCardNos);
    //    }
    //
    //    //删除下发
    //    PersonDeviceAuthorityDTO delDTO = new PersonDeviceAuthorityDTO();
    //    delDTO.setDeviceCardNosPushMap(needDelDeviceCardNosMap);
    //    ThreadUtil.execAsync(() -> {
    //        deviceOperateProvider.doorAccessPersonsRemove(delDTO);
    //    });
    //    //新增下发
    //    PersonDeviceAuthorityDTO addDTO = new PersonDeviceAuthorityDTO();
    //    addDTO.setDeviceCardNosPushMap(needAddDeviceCardNosMap);
    //    ThreadUtil.execAsync(() -> {
    //        deviceOperateProvider.pushPersons2Devices(addDTO);
    //    });
    //}
    //
    //private Map<String, Set<String>> getDeviceCardNosMap(List<AccessPolicy> curAccessPolicies) {
    //    Map<String, Set<String>> deviceCardNosMap = new HashMap<>();
    //    for (AccessPolicy accessPolicy : curAccessPolicies) {
    //        Set<String> deviceIds = accessPolicy.getDeviceIds();
    //        List<EmployeeVO> employeeVOList;
    //        if (CollUtil.isEmpty(accessPolicy.getTagIds())) {
    //            Set<String> companyIds = accessPolicy.getCompanyId();
    //            employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(companyIds, null, false);
    //        } else {
    //            Set<String> tagIds = accessPolicy.getTagIds();
    //            employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(null, tagIds, false);
    //        }
    //        Set<String> employeeCardNos = employeeVOList.stream().map(EmployeeVO::getCardNo).collect(Collectors.toSet());
    //        for (EmployeeVO employeeVO : employeeVOList) {
    //            Set<String> cardNos = cardProvider.findAllCardByEmployeeId(employeeVO.getId());
    //            employeeCardNos.addAll(cardNos);
    //        }
    //        for (String deviceId : deviceIds) {
    //            if (!deviceCardNosMap.containsKey(deviceId)) {
    //                deviceCardNosMap.put(deviceId, new HashSet<>());
    //            }
    //            deviceCardNosMap.get(deviceId).addAll(employeeCardNos);
    //        }
    //    }
    //    return deviceCardNosMap;
    //}
}
