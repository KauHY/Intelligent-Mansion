package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.device.dto.PersonDeviceAuthorityDTO;
import com.crlink.smartpark.device.feign.CardProvider;
import com.crlink.smartpark.device.feign.DeviceInfoProvider;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.visitor.controller.request.StatusReqDTO;
import com.crlink.smartpark.visitor.dto.AccessPolicySaveDTO;
import com.crlink.smartpark.visitor.dto.AccessPolicyUpdateDTO;
import com.crlink.smartpark.visitor.dto.DeviceIdsAndCardNosDTO;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.entity.PolicyEmployee;
import com.crlink.smartpark.visitor.mapper.AccessPolicyMapper;
import com.crlink.smartpark.visitor.query.AccessPolicyQuery;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import com.crlink.smartpark.visitor.service.ApartmentPersonService;
import com.crlink.smartpark.visitor.service.PolicyEmployeeService;
import com.crlink.smartpark.visitor.vo.AccessPolicyListVO;
import com.crlink.smartpark.visitor.vo.AccessPolicyVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 人行通行策略表 服务实现类
 * </p>
 * */
@Slf4j
@Service
public class AccessPolicyServiceImpl extends ServiceImpl<AccessPolicyMapper, AccessPolicy> implements AccessPolicyService {

    @Resource
    private CommonProvider commonProvider;
    @Resource
    private DeviceInfoProvider deviceInfoProvider;
    @Resource
    private EmployeeProvider employeeProvider;
    @Resource
    private DeviceOperateProvider deviceOperateProvider;
    @Resource
    private CardProvider cardProvider;
    @Resource
    private PolicyEmployeeService policyEmployeeService;
    @Resource
    private ApartmentPersonService apartmentPersonService;
    @Resource
    private ApartmentAsyncAuthImpl apartmentAsyncAuthImpl;

    @Override
    public void save(AccessPolicySaveDTO dto) {
        dto.setTenantId(SystemUtil.getInstance().getCurrentHeadOrgId());
        dto.setPolicyName(dto.getPolicyName().trim());
        checkUniqueName(dto.getTenantId(), dto.getPolicyName());
        AccessPolicy accessPolicy = MyBeanUtils.copy(dto, AccessPolicy::new);
        EntityUserUtil.setAll(accessPolicy);
        accessPolicy.setExpire(dto.getPolicyExpireTime().isBefore(LocalDate.now()) ? DictUtil.getValue(DictConstant.YesOrNo.YES) : DictUtil.getValue(DictConstant.YesOrNo.NO));
        // TODO: 2022/3/31 考虑后面优化成切面，进行设备刷新策略的操作减少冗余代码
        List<AccessPolicy> beforeSnapshot = getAllByParkOrgId(dto.getParkOrgId());
        AssertUtil.requireTrueOrDataExp(save(accessPolicy));
        this.refreshAccessPolicy(beforeSnapshot, accessPolicy.getParkOrgId());
    }

    /**
     * 新增通行策略
     * 跟产品确认，新增时默认禁用状态，所以这里不做授权操作
     *
     * @date 2022-11-01     * @param dto
     */
    @Override
    public void save2(AccessPolicySaveDTO dto) {
        dto.setTenantId(SystemUtil.getInstance().getCurrentHeadOrgId());
        dto.setPolicyName(dto.getPolicyName().trim());
        checkUniqueName(dto.getTenantId(), dto.getPolicyName());
        AccessPolicy accessPolicy = MyBeanUtils.copy(dto, AccessPolicy::new);
        EntityUserUtil.setAll(accessPolicy);
        accessPolicy.setStatus(DictUtil.getValue(DictConstant.YesOrNo.NO));//默认禁用
        accessPolicy.setExpire(dto.getPolicyExpireTime().isBefore(LocalDate.now()) ? DictUtil.getValue(DictConstant.YesOrNo.YES) : DictUtil.getValue(DictConstant.YesOrNo.NO));
        //新增
        AssertUtil.requireTrueOrDataExp(save(accessPolicy));
        this.refreshPolicyEmployee(accessPolicy);
    }

    @Override
    public void update(AccessPolicyUpdateDTO dto) {
        dto.setTenantId(SystemUtil.getInstance().getCurrentHeadOrgId());
        dto.setPolicyName(dto.getPolicyName().trim());
        AccessPolicy accessPolicyById = getById(dto.getId());
        if (!accessPolicyById.getPolicyName().equals(dto.getPolicyName())) {
            checkUniqueName(dto.getTenantId(), dto.getPolicyName());
        }
        AccessPolicy accessPolicy = MyBeanUtils.copy(dto, AccessPolicy::new);
        EntityUserUtil.setUpdateBy(accessPolicy);
        accessPolicy.setExpire(dto.getPolicyExpireTime().isBefore(LocalDate.now()) ? DictUtil.getValue(DictConstant.YesOrNo.YES) : DictUtil.getValue(DictConstant.YesOrNo.NO));
        List<AccessPolicy> beforeSnapshot = getAllByParkOrgId(accessPolicy.getParkOrgId());
        AssertUtil.requireTrueOrDataExp(updateById(accessPolicy));
        if (StatusEnum.ENABLE.getCode().equals(accessPolicyById.getStatus())) {
            this.refreshAccessPolicy(beforeSnapshot, accessPolicy.getParkOrgId());
        }
    }

    /**
     * 编辑通行策略
     * 区别：
     * 1、相比以前比较整个园区的通行策略，该方法只比较当前通行策略；
     * 2、使用增量授权方式授权；
     *
     * @param dto
     * @date 2022-11-01     */
    @Override
    public void update2(AccessPolicyUpdateDTO dto) {
        dto.setTenantId(SystemUtil.getInstance().getCurrentHeadOrgId());
        dto.setPolicyName(dto.getPolicyName().trim());
        //旧策略
        AccessPolicy oldPolicy = getById(dto.getId());
        if (!oldPolicy.getPolicyName().equals(dto.getPolicyName())) {
            checkUniqueName(dto.getTenantId(), dto.getPolicyName());
        }
        //新策略
        AccessPolicy newPolicy = MyBeanUtils.copy(dto, AccessPolicy::new);
        EntityUserUtil.setUpdateBy(newPolicy);
        newPolicy.setExpire(dto.getPolicyExpireTime().isBefore(LocalDate.now()) ? DictUtil.getValue(DictConstant.YesOrNo.YES) : DictUtil.getValue(DictConstant.YesOrNo.NO));
        //修改
        AssertUtil.requireTrueOrDataExp(updateById(newPolicy));
        if (StatusEnum.ENABLE.getCode().equals(oldPolicy.getStatus())) {
            DeviceIdsAndCardNosDTO policyEmployeeCollection = policyEmployeeService.getDeviceIdsAndCardNosByPolicyId(dto.getId());
            Set<String> oldDeviceIds = policyEmployeeCollection.getDeviceIds();
            Set<String> oldCardNos = policyEmployeeCollection.getCardNos();
            if (CollUtil.isEmpty(oldDeviceIds)) {
                oldDeviceIds = oldPolicy.getDeviceIds();
            }
            if (CollUtil.isEmpty(oldCardNos)) {
                oldCardNos = getEmployeeCardNos(oldPolicy.getCompanyId(), oldPolicy.getTagIds());
            }
            Set<String> newDeviceIds = newPolicy.getDeviceIds();
            Set<String> newCardNos = getEmployeeCardNos(newPolicy.getCompanyId(), newPolicy.getTagIds());
            Collection<String> sameCardNos = CollUtil.intersection(oldCardNos, newCardNos);
            if (newDeviceIds.equals(oldDeviceIds)) {
                if (!newCardNos.equals(oldCardNos)) {
                    log.info("编辑通行策略，设备没变化、卡片有变化，需刷新门禁权限");
                    oldCardNos.removeAll(sameCardNos);
                    newCardNos.removeAll(sameCardNos);
                    this.removeAuth(oldDeviceIds, oldCardNos);
                    this.addAuth(oldDeviceIds, newCardNos);
                } else {
                    log.info("编辑通行策略，设备没变化、卡片没变化，无需刷新门禁权限");
                }
            } else {
                Collection<String> sameDeviceIds = CollUtil.intersection(oldDeviceIds, newDeviceIds);
                Set<String> delDeviceIds = new HashSet<>(oldDeviceIds);
                delDeviceIds.removeAll(sameDeviceIds);
                Set<String> addDeviceIds = new HashSet<>(newDeviceIds);
                addDeviceIds.removeAll(sameDeviceIds);
                if (newCardNos.equals(oldCardNos)) {
                    log.info("编辑通行策略，设备有变化、卡片没变化，需刷新门禁权限");
                    this.removeAuth(delDeviceIds, oldCardNos);
                    this.addAuth(addDeviceIds, oldCardNos);
                } else {
                    log.info("编辑通行策略，设备有变化、卡片有变化，需刷新门禁权限");
                    Set<String> delCardNos = new HashSet<>(oldCardNos);
                    delCardNos.removeAll(sameCardNos);
                    Set<String> addCardNos = new HashSet<>(newCardNos);
                    addCardNos.removeAll(sameCardNos);
                    this.removeAuth(oldDeviceIds, delCardNos);
                    this.removeAuth(delDeviceIds, new HashSet<>(sameCardNos));
                    this.addAuth(addDeviceIds, new HashSet<>(sameCardNos));
                    this.addAuth(newDeviceIds, addCardNos);
                }
            }
            this.refreshPolicyEmployee(newPolicy);
        }
    }

    /**
     * 新增授权
     *
     * @date 2022-11-01     * @param deviceIds
     * @param cardNos
     */
    private void addAuth(Set<String> deviceIds, Set<String> cardNos) {
        Map<String, Set<String>> deviceIdCardNosMap = getDeviceIdCardNosMap(deviceIds, cardNos);
        if (MapUtil.isEmpty(deviceIdCardNosMap)) {
            return;
        }
        PersonDeviceAuthorityDTO authDTO = new PersonDeviceAuthorityDTO();
        authDTO.setCardNoDevicesPushMap(wrapMap(deviceIdCardNosMap));
        deviceOperateProvider.pushPersons2Devices(authDTO);
    }

    /**
     * 删除授权
     *
     * @date 2022-11-01     * @param deviceIds
     * @param cardNos
     */
    @Override
    public void removeAuth(Set<String> deviceIds, Set<String> cardNos) {
        Map<String, Set<String>> deviceIdCardNosMap = getDeviceIdCardNosMap(deviceIds, cardNos);
        if (MapUtil.isEmpty(deviceIdCardNosMap)) {
            return;
        }
        PersonDeviceAuthorityDTO authDTO = new PersonDeviceAuthorityDTO();
        authDTO.setDeviceCardNosPushMap(deviceIdCardNosMap);
        deviceOperateProvider.doorAccessPersonsRemove(authDTO);
    }

    /**
     * 刷新策略员工信息
     *
     * @param accessPolicy
     * @date 2022-11-25     */
    @Override
    public void refreshPolicyEmployee(AccessPolicy accessPolicy) {
        log.info("刷新策略员工信息: {}", accessPolicy);
        String id = accessPolicy.getId();
        Set<String> companyIds = accessPolicy.getCompanyId();
        Set<String> deviceIds = accessPolicy.getDeviceIds();
        List<EmployeeVO> employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(companyIds, null, false);
        List<PolicyEmployee> policyEmployeeList = new ArrayList<>(employeeVOList.size());
        for (EmployeeVO empVO : employeeVOList) {
            PolicyEmployee pe = new PolicyEmployee();
            pe.setPolicyId(id);
            pe.setCompanyId(empVO.getCompanyId());
            pe.setEmployeeId(empVO.getId());
            pe.setDevicePersonId(empVO.getDevicePersonId());
            pe.setCardNo(empVO.getCardNo());
            pe.setDeviceIds(deviceIds);
            policyEmployeeList.add(pe);
        }
        policyEmployeeService.refresh(id, policyEmployeeList);
    }

    /**
     * 获取DeviceIdCardNosMap
     *
     * @date 2022-11-01     * @param deviceIds
     * @param cardNos
     */
    @Nullable
    private Map<String, Set<String>> getDeviceIdCardNosMap(Set<String> deviceIds, Set<String> cardNos) {
        if (CollUtil.isEmpty(deviceIds) || CollUtil.isEmpty(cardNos)) {
            return null;
        }
        Map<String, Set<String>> deviceIdCardNosMap = new HashMap<>();
        for (String deviceId : deviceIds) {
            deviceIdCardNosMap.put(deviceId, cardNos);
        }
        return deviceIdCardNosMap;
    }



    /**
     * 获取员工卡号集合
     *
     * @date 2022-11-01     * @param companyIds
     * @param tagIds
     */
    @Override
    public Set<String> getEmployeeCardNos(Set<String> companyIds, Set<String> tagIds) {
        List<EmployeeVO> employeeVOList = null;
        if (CollUtil.isEmpty(tagIds)) {
            employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(companyIds, null, false);
        } else {
            employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(null, tagIds, false);
        }
        Set<String> employeeCardNos = employeeVOList.stream().map(EmployeeVO::getCardNo).collect(Collectors.toSet());
        return employeeCardNos;
    }

    private void checkUniqueName(String tenantId, String name) {
        AssertUtil.requireTrue(this.count(Wrappers.<AccessPolicy>lambdaQuery()
                .eq(AccessPolicy::getTenantId, tenantId)
                .eq(AccessPolicy::getPolicyName, name)) <= 0, "70001");
    }

    @Override
    public List<DictVO> listAccessPolicyByCompany(String companyId) {
        LambdaQueryWrapper<AccessPolicy> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.apply(StringUtils.isNotBlank(companyId), "FIND_IN_SET(" + companyId + ",company_id)");
        queryWrapper.eq(AccessPolicy::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES));
        queryWrapper.eq(AccessPolicy::getAllowVisitor, DictUtil.getValue(DictConstant.YesOrNo.YES));
        queryWrapper.orderByDesc(AccessPolicy::getUpdateTime);
        List<DictVO> list = list(queryWrapper).stream().map(e -> new DictVO(e.getPolicyName(), e.getId())).collect(Collectors.toList());
        return list;
    }

    @Override
    public Set<String> getAccessDeviceIdsByEmployeeInfo(String employeeCompanyId, Collection<String> employeeTagIds) {
        LambdaQueryWrapper<AccessPolicy> wrapper = new LambdaQueryWrapper();
        wrapper.eq(AccessPolicy::getStatus, StatusEnum.ENABLE.getCode()).and(e -> {
            if (CollUtil.isNotEmpty(employeeTagIds)) {
                for (String tagId : employeeTagIds) {
                    e.apply("FIND_IN_SET(" + tagId + ",tag_ids)").or();
                }
            }
            e.apply("FIND_IN_SET(" + employeeCompanyId + ",company_id)");
        });
        List<AccessPolicy> list = list(wrapper);
        Set<String> deviceIds = new HashSet<>();
        for (AccessPolicy accessPolicy : list) {
            //如果关联了标签，那么只认标签匹配
            if (CollUtil.isNotEmpty(accessPolicy.getTagIds()) && !CollUtil.containsAny(accessPolicy.getTagIds(), employeeTagIds)) {
                continue;
            }
            deviceIds.addAll(accessPolicy.getDeviceIds());
        }
        return deviceIds;
    }

    @Override
    public Boolean getTagIdsExist(String employeeTagId) {
        LambdaQueryWrapper<AccessPolicy> wrapper = new LambdaQueryWrapper();
        wrapper.select(AccessPolicy::getId, AccessPolicy::getTagIds);
        String sql = new StringBuilder("FIND_IN_SET(" + employeeTagId + ",tag_ids)").toString();
        wrapper.apply(sql).last("limit 1");
        List<AccessPolicy> list = list(wrapper);
//        for (AccessPolicy accessPolicy : list) {
//            //如果关联了标签，那么只认标签匹配
//            if (CollUtil.isNotEmpty(accessPolicy.getTagIds()) && accessPolicy.getTagIds().contains(employeeTagId)) {
//                return true;
//            }
//        }
        if (CollUtil.isNotEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean delAccessPolicy(String id) {
        AccessPolicy accessPolicy = getById(id);
        Boolean result = removeById(id);
        Set<String> deviceIds = accessPolicy.getDeviceIds();
        Set<String> cardNos = getEmployeeCardNos(accessPolicy.getCompanyId(), accessPolicy.getTagIds());
        log.info("删除通行策略，刷新门禁权限");
        this.removeAuth(deviceIds, cardNos);
        policyEmployeeService.refresh(id, null);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(StatusReqDTO dto) {
        AccessPolicy accessPolicy = getById(dto.getId());
        BeanUtils.copyProperties(dto, accessPolicy);
        Boolean result = updateById(accessPolicy);
        DeviceIdsAndCardNosDTO policyEmployeeCollection = policyEmployeeService.getDeviceIdsAndCardNosByPolicyId(dto.getId());
        Set<String> deviceIds = policyEmployeeCollection.getDeviceIds();
        Set<String> cardNos = policyEmployeeCollection.getCardNos();
        //启用
        if (accessPolicy.getStatus().equals(DictUtil.getValue(DictConstant.YesOrNo.YES))) {
            log.info("启用通行策略，刷新门禁权限");
            this.addAuth(deviceIds, cardNos);
            this.refreshPolicyEmployee(accessPolicy);
        } else {
            log.info("禁用通行策略，刷新门禁权限");
            this.removeAuth(deviceIds, cardNos);
            policyEmployeeService.refresh(dto.getId(), null);
        }
        return result;
    }

    @Override
    public List<AccessPolicy> getAllByParkOrgId(String parkOrgId) {
        LambdaQueryWrapper<AccessPolicy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessPolicy::getParkOrgId, parkOrgId);
        wrapper.eq(AccessPolicy::getStatus, StatusEnum.ENABLE.getCode());
        wrapper.eq(AccessPolicy::getExpire, DictUtil.getValue(DictConstant.YesOrNo.NO));
        List<AccessPolicy> list = list(wrapper);
        return list;
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public AccessPolicyVO getAccessPolicyById(String id) {
        if (StringUtils.isNotBlank(id)) {
            AccessPolicy entity = getById(id);
            if (entity != null) {
                AccessPolicyVO dto = new AccessPolicyVO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<AccessPolicyListVO> listPage(AccessPolicyQuery query) {
        query.startPage();
        LambdaQueryWrapper<AccessPolicy> wrapper = buildPageListRecordWrapper(query);
        List<AccessPolicy> list = list(wrapper);
        return query.createPageInfo(listEntityToListVO(list));
    }

    private List<AccessPolicyListVO> listEntityToListVO(List<AccessPolicy> list) {
        Set<String> parkOrgQueryIds = new HashSet<>();//园区ID集合
        Set<String> companyQueryIds = new HashSet<>();//企业ID集合
        Set<String> deviceQueryIds = new HashSet<>();//设备ID集合
        Set<String> tagQueryIds = new HashSet<>();//标签ID集合

        list.forEach(e -> {
            parkOrgQueryIds.add(e.getParkOrgId());
            companyQueryIds.addAll(e.getCompanyId());
            deviceQueryIds.addAll(e.getDeviceIds());
            tagQueryIds.addAll(e.getTagIds());
        });
        //园区名集合
        Map<String, String> parkNameMap = commonProvider.getNamesByIdsAndType(parkOrgQueryIds, SimpleNode.TypeEnum.PARK.getCode());
        //企业名集合
        Map<String, String> companyNameMap = commonProvider.getNamesByIdsAndType(companyQueryIds, SimpleNode.TypeEnum.COMPANY.getCode());
        //设备名集合
        Map<String, String> deviceNameMap = deviceInfoProvider.getNamesByIds(deviceQueryIds);
        //标签名集合
        Map<String, String> tagNameMap = commonProvider.getNamesByIdsAndType(tagQueryIds,
                SimpleNode.TypeEnum.Tag.getCode());
        List<AccessPolicyListVO> voList = new ArrayList<>();
        list.forEach(e -> {
            AccessPolicyListVO copy = MyBeanUtils.copy(e, AccessPolicyListVO::new);
            copy.setParkOrgName(parkNameMap.get(e.getParkOrgId()));
            //企业拼接
            if (CollectionUtils.isNotEmpty(e.getCompanyId())) {
                StringBuilder companySb = new StringBuilder();
                for (String companyId : e.getCompanyId()) {
                    companySb.append(companyNameMap.get(companyId)).append(",");
                }
                companySb.deleteCharAt(companySb.length() - 1);
                copy.setCompanyName(companySb.toString());
            }

            //设备名称拼接
            StringBuilder deviceNameSb = new StringBuilder();
            for (String deviceId : e.getDeviceIds()) {
                deviceNameSb.append(deviceNameMap.get(deviceId)).append(",");
            }
            deviceNameSb.deleteCharAt(deviceNameSb.length() - 1);
            copy.setDeviceNames(deviceNameSb.toString());

            //员工标签拼接
            if (CollectionUtils.isNotEmpty(e.getTagIds())) {
                StringBuilder labelSb = new StringBuilder();
                for (String labelId : e.getTagIds()) {
                    labelSb.append(tagNameMap.get(labelId)).append(",");
                }
                labelSb.deleteCharAt(labelSb.length() - 1);
                copy.setTagsName(labelSb.toString());
            }
            voList.add(copy);
        });
        return voList;
    }

    /**
     * 封装查询条件
     *
     * @param query 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryWrapper<AccessPolicy> buildPageListRecordWrapper(AccessPolicyQuery query) {
        LambdaQueryWrapper<AccessPolicy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AccessPolicy::getUpdateTime)
                .in(AccessPolicy::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), AccessPolicy::getParkOrgId, query.getParkOrgId())
                .apply(StringUtils.isNotBlank(query.getCompanyId()), "FIND_IN_SET(" + query.getCompanyId() + ",company_id)")
                .like(StringUtils.isNotBlank(query.getPolicyName()), AccessPolicy::getPolicyName, query.getPolicyName())
                .orderByDesc(AccessPolicy::getCreateTime);
        return queryWrapper;
    }

    /**
     * 刷新通行策略
     *
     * @param beforeAccessPolicies
     * @param parkOrgId
     */
    @Override
    public void refreshAccessPolicy(List<AccessPolicy> beforeAccessPolicies, String parkOrgId) {
        List<AccessPolicy> curAccessPolicies = this.getAllByParkOrgId(parkOrgId);
        Map<String, Set<String>> beforeDeviceCardNosMap = getDeviceCardNosMap(beforeAccessPolicies);
        Map<String, Set<String>> curDeviceCardNosMap = getDeviceCardNosMap(curAccessPolicies);
        Map<String, Set<String>> needAddDeviceCardNosMap = new HashMap<>();
        Map<String, Set<String>> needDelDeviceCardNosMap = new HashMap<>();
        Set<String> allDeviceIds = new HashSet<>();
        allDeviceIds.addAll(beforeDeviceCardNosMap.keySet());
        allDeviceIds.addAll(curDeviceCardNosMap.keySet());
        for (String deviceId : allDeviceIds) {
            Set<String> beforeCardNos = beforeDeviceCardNosMap.containsKey(deviceId) ? beforeDeviceCardNosMap.get(deviceId) : new HashSet<>();
            Set<String> curCardNos = curDeviceCardNosMap.containsKey(deviceId) ? curDeviceCardNosMap.get(deviceId) : new HashSet<>();
            Collection<String> sameCardNos = CollUtil.intersection(beforeCardNos, curCardNos);
            beforeCardNos.removeAll(sameCardNos);
            curCardNos.removeAll(beforeCardNos);
            if (CollUtil.isNotEmpty(beforeCardNos)) {
                needDelDeviceCardNosMap.put(deviceId, beforeCardNos);
            }
            if (CollUtil.isNotEmpty(curCardNos)) {
                needAddDeviceCardNosMap.put(deviceId, curCardNos);
            }
        }
        //删除下发
        if (CollUtil.isNotEmpty(needDelDeviceCardNosMap)) {
            PersonDeviceAuthorityDTO delDTO = new PersonDeviceAuthorityDTO();
            delDTO.setDeviceCardNosPushMap(needDelDeviceCardNosMap);
            deviceOperateProvider.doorAccessPersonsRemove(delDTO);
        }
        //新增下发
        if (CollUtil.isNotEmpty(needAddDeviceCardNosMap)) {
            PersonDeviceAuthorityDTO addDTO = new PersonDeviceAuthorityDTO();
            addDTO.setCardNoDevicesPushMap(wrapMap(needAddDeviceCardNosMap));
            deviceOperateProvider.pushPersons2Devices(addDTO);
        }
    }

    private Map<String, Set<String>> wrapMap(Map<String, Set<String>> deviceCardNosMap) {
        Map<String, Set<String>> tempMap = new HashMap<>();
        Iterator<Map.Entry<String, Set<String>>> it = deviceCardNosMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Set<String>> next = it.next();
            String devId = next.getKey();
            Set<String> nos = next.getValue();
            for (String no : nos) {
                if (!tempMap.containsKey(no)) {
                    tempMap.put(no, Sets.newHashSet());
                }
                tempMap.get(no).add(devId);
            }
        }
        return tempMap;
    }

    @Override
    public boolean existsDevice(String deviceId) {
        return this.count(Wrappers.<AccessPolicy>lambdaQuery().apply("FIND_IN_SET({0},device_ids)", deviceId)) > 0;
    }

    private Map<String, Set<String>> getDeviceCardNosMap(List<AccessPolicy> curAccessPolicies) {
        Map<String, Set<String>> deviceCardNosMap = new HashMap<>();
        for (AccessPolicy accessPolicy : curAccessPolicies) {
            //公共门禁
            Set<String> deviceIds = accessPolicy.getDeviceIds();
            List<EmployeeVO> employeeVOList;
            if (CollUtil.isEmpty(accessPolicy.getTagIds())) {
                Set<String> companyIds = accessPolicy.getCompanyId();
                employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(companyIds, null, false);
            } else {
                Set<String> tagIds = accessPolicy.getTagIds();
                employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(null, tagIds, false);
            }
            Set<String> employeeCardNos = employeeVOList.stream().map(EmployeeVO::getCardNo).collect(Collectors.toSet());
            for (EmployeeVO employeeVO : employeeVOList) {
                String apartmentEmployeeId = apartmentPersonService.existNormalPersonByEmployeeId(employeeVO.getId());
                if (StringUtils.isNotBlank(apartmentEmployeeId) && apartmentEmployeeId.equals(employeeVO.getId())) {
                    employeeCardNos.remove(employeeVO.getCardNo());
                    //公寓门禁、智能锁授权
                    apartmentAsyncAuthImpl.apartmentDevicesAuth(employeeVO.getId());
                } else {
                    Set<String> cardNos = cardProvider.findAllCardByEmployeeId(employeeVO.getId());
                    employeeCardNos.addAll(cardNos);
                }
            }
            for (String deviceId : deviceIds) {
                if (!deviceCardNosMap.containsKey(deviceId)) {
                    deviceCardNosMap.put(deviceId, new HashSet<>());
                }
                deviceCardNosMap.get(deviceId).addAll(employeeCardNos);
            }
        }
        return deviceCardNosMap;
    }

}
