package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.Employee;
import com.crlink.smartpark.system.entity.Job;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.mapper.EmployeeMapper;
import com.crlink.smartpark.system.service.common.CommonService;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.*;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.CACHE_GROUP_EMPLOYEE;
import static com.crlink.smartpark.common.constants.CacheConstant.CacheKeyGenerator;
import static com.crlink.smartpark.common.constants.Constants.SPECIAL_COMPANY_TYPE;

/**
 * <p>
 * 入住企业员工信息表 服务实现类
 * </p>
 * */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;


    @Autowired
    private JobEmployeeRelationService jobEmployeeRelationService;

    @Autowired
    public CompanyService companyService;

    @Autowired
    private JobService jobService;

    @Resource
    private CommonService commonService;

    @Autowired
    private TagService tagService;

    @Override
    public List<Employee> listEmployeeByLikeName(Collection<String> parkOrgIds,
                                                 String likeName,
                                                 Integer companyType,
                                                 String companyId) {
        return baseMapper.listEmployeeByLikeName(parkOrgIds, likeName, companyType, companyId);
    }


    @Override
    public EmployeeVO findEmployeeByCompanyAndTel(String companyId, String mobile) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Employee::getId, Employee::getName, Employee::getDevicePersonId);
        wrapper.eq(Employee::getMobile, mobile);
        wrapper.eq(Employee::getCompanyId, companyId);
        Employee employee = getOne(wrapper, false);
        if (employee == null) {
            return null;
        }
        EmployeeVO employeeVO = MyBeanUtils.copy(employee, EmployeeVO::new);
        employeeVO.setEmployeeName(employee.getName());
        return employeeVO;
    }

    @Override
    public Map<String, String> findEmployeeNamesByIds(String[] employeeIds) {
        if (ArrayUtil.isEmpty(employeeIds)) {
            return Maps.newHashMap();
        }
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Employee::getId, Employee::getName);
        wrapper.in(Employee::getId, employeeIds);
        List<Employee> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return Maps.newHashMap();
        }
        return list.stream().collect(Collectors.toMap(Employee::getId, Employee::getName));
    }

    @Override
    public List<EmployeeVO> findEmployeeByCompanyIdsAndTagIds(Collection<String> companyIds, Collection<String> tagIds, Boolean translateParkCompany) {
        if (CollUtil.isEmpty(companyIds) && CollUtil.isEmpty(tagIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getStatus, StatusEnum.ENABLE.getCode());
        if (CollUtil.isNotEmpty(companyIds)) {
            wrapper.in(Employee::getCompanyId, companyIds);
        }
        if (CollUtil.isNotEmpty(tagIds)) {
            wrapper.and(e -> {
                for (String tagId : tagIds) {
                    e.apply("JSON_CONTAINS(tag_ids,'\"" + tagId + "\"')").or();
                }
            });
        }
        List<Employee> list = list(wrapper);
        List<EmployeeVO> voList;
        if (translateParkCompany) {
            //园区名map
            Map<String, String> parkNamesMap = commonService.getNamesByIdsAndType(list.stream().map(Employee::getParkOrgId).collect(Collectors.toSet()), SimpleNode.TypeEnum.PARK.getCode());
            //公司名map
            Map<String, String> companyNamesMap = commonService.getNamesByIdsAndType(list.stream().map(Employee::getCompanyId).collect(Collectors.toSet()), SimpleNode.TypeEnum.COMPANY.getCode());
            voList = MyBeanUtils.copyList(list, EmployeeVO::new, (s, t) -> {
                t.setEmployeeName(s.getName());
                t.setParkOrgName(parkNamesMap.get(s.getParkOrgId()));
                t.setCompanyName(companyNamesMap.get(s.getCompanyId()));
            });
        } else {
            voList = MyBeanUtils.copyList(list, EmployeeVO::new);
        }
        return voList;
    }

    @Override
    public Map<String, Integer> queryEmployeesCount(Collection<String> companyIds) {
        Map<String, Integer> map = new HashMap<>(companyIds.size());
        for (String companyId : companyIds) {
            Integer count = this.lambdaQuery().eq(Employee::getCompanyId, companyId).count();
            map.put(companyId, count);
        }
        return map;
    }

    @Override
    public EmployeeVO findEmployeeByDevicePersonId(Long devicePersonId) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getDevicePersonId, devicePersonId);
        Employee employee = getOne(wrapper, false);
        if (Objects.isNull(employee)) {
            return null;
        }
        EmployeeVO employeeVO = MyBeanUtils.copy(employee, EmployeeVO::new);
        employeeVO.setEmployeeName(employee.getName());
        employeeVO.setParkOrgName(commonService.getNamesByIdsAndType(Sets.newHashSet(employee.getParkOrgId()), SimpleNode.TypeEnum.PARK.getCode()).get(employee.getParkOrgId()));
        if (CollUtil.isNotEmpty(employee.getTagIds())) {
            Map<String, String> names = commonService.getNamesByIdsAndType(Sets.newHashSet(employee.getTagIds()), SimpleNode.TypeEnum.Tag.getCode());
            if (MapUtil.isNotEmpty(names)) {
                employeeVO.setTagLabels(Lists.newArrayList(names.values()));
            }
        }
        return employeeVO;
    }

    /**
     * 根据id获取员工信息
     *
     * @param id
     * @return
     */
    @Override
    public EmployeeVO getEmployeeVOById(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        Employee employee = this.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getId, id));
        EmployeeVO employeeVO = MyBeanUtils.copy(employee, EmployeeVO::new);
        if (Objects.nonNull(employee)) {
            employeeVO.setEmployeeName(employee.getName());
        }
        return employeeVO;
    }

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Employee, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> companyId2ValueMap = this.lambdaQuery()
                .select(Employee::getId, sValue)
                .in(Employee::getId, ids)
                .eq(Employee::getStatus, StatusEnum.ENABLE.getCode())
                .list().parallelStream().collect(Collectors.toMap(Employee::getId, sValue));
        return companyId2ValueMap;
    }


    @Override
    @Cacheable(value = CACHE_GROUP_EMPLOYEE, keyGenerator = CacheKeyGenerator)
    public Map<String, Map<String, Object>> queryEmployeeColumns(EmployeeQueryDTO dto) {
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = employeeConditionQuery(dto, SystemUtil.getInstance().getCurrentHeadOrgId());
        if (ObjectUtil.isNotNull(employeeLambdaQueryWrapper)) {
            List<Map<String, Object>> listMaps = this.listMaps(employeeLambdaQueryWrapper);
            Map<String, Map<String, Object>> result = Maps.newHashMap();
            listMaps.forEach(t -> {
                String id = (String) t.get("id");
                result.put(id, t);
            });
            return result;
        }
        return Collections.emptyMap();
    }


    @Override
    @Cacheable(value = CACHE_GROUP_EMPLOYEE, keyGenerator = CacheKeyGenerator)
    public Map<String, EmployeeDTO> queryEmployeeByCondition(EmployeeQueryDTO dto, String currentHeadOrgId) {
        Map<String, EmployeeDTO> result = Maps.newHashMap();
        LambdaQueryWrapper<Employee> lambdaQuery = employeeConditionQuery(dto, currentHeadOrgId);
        if (ObjectUtil.isNull(lambdaQuery)) {
            return result;
        }
        List<Employee> list = this.list(lambdaQuery);
        list.forEach(t -> {
            result.put(t.getId(), MyBeanUtils.copy(t, EmployeeDTO::new));
        });
        setEmployeeInfo(result, dto);
        return result;
    }


    /**
     * 投诉、保修使用
     *
     * @param employeeIds
     * @return
     */
    @Override
    public Map<String, EmployeeDTO> orderRecordQueryEmployeeColumns(Collection<String> employeeIds) {
        if (CollectionUtil.isEmpty(employeeIds)) {
            return Collections.emptyMap();
        }
        List<Employee> employees = baseMapper.orderRecordQueryEmployeeColumns(employeeIds);
//        Map<String, Map<String, Object>> result = Maps.newHashMap();
//        employees.parallelStream().map(t -> {
//            Map<String, Object> employeeMap = new HashMap<>();
//            employeeMap.put("id", t.getId());
//            employeeMap.put("name", t.getName());
//            employeeMap.put("company_id", t.getCompanyId());
//            return employeeMap;
//        }).forEach(m -> {
//            String id = (String) m.get("id");
//            result.put(id, m);
//        });
        Map<String, EmployeeDTO> result = Maps.newHashMap();
        employees.forEach(t -> {
            EmployeeDTO employeeDTO = MyBeanUtils.copy(t, EmployeeDTO::new);
            result.put(t.getId(), employeeDTO);
        });
        EmployeeQueryDTO dto = new EmployeeQueryDTO();
        dto.setShowCompanyName(true);
        setEmployeeInfo(result, dto);
        return result;
    }


    @Override
    public Map<String, String> listSpecialEmployeeByLikeName(String orgId, String likeName) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(orgId);
        List<Employee> employees = baseMapper.listEmployeeByLikeName(parkOrgIds, likeName, SPECIAL_COMPANY_TYPE, null);
        return employees.parallelStream().collect(Collectors.toMap(Employee::getId, Employee::getName));
    }


    @Override
    public Map<String, Set<Integer>> getJobCodeByEmployeeIds(Collection<String> employeeIds) {
        //过滤掉被禁用的员工
        Map<String, Integer> employeeId2StatusMap = (Map<String, Integer>) this.id2ValueMap(employeeIds, Employee::getStatus);
        Map<String, List<String>> employeeId2JobIdListMap = jobEmployeeRelationService.employeeId2JobIdListMap(employeeIds);
        Set<String> jobIds = employeeId2JobIdListMap.values().parallelStream().flatMap(t -> t.stream()).collect(Collectors.toSet());
        Map<String, Integer> jobId2JobCodeMap = (Map<String, Integer>) jobService.id2ValueMap(jobIds, Job::getJobCode);
        Map<String, Set<Integer>> employeeId2JobCodeMap = Maps.newHashMap();
        employeeId2JobIdListMap.forEach((k, v) -> {
            Set<Integer> jobCodes = v.parallelStream().map(jobId2JobCodeMap::get).collect(Collectors.toSet());
            employeeId2JobCodeMap.put(k, jobCodes);
        });
        return employeeId2JobCodeMap;
    }

    @Override
    public Map<String, String> listEmployeeByName(String likeName) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        List<Employee> employees = baseMapper.listEmployeeByLikeName(parkOrgIds, likeName, null, null);
        return employees.parallelStream().collect(Collectors.toMap(Employee::getId, Employee::getName));
    }


    private void setEmployeeInfo(Map<String, EmployeeDTO> result, EmployeeQueryDTO dto) {
        Collection<EmployeeDTO> list = result.values();
        if (dto.isShowParkOrgName()) {
            Set<String> parkOrgIds = list.parallelStream().map(EmployeeDTO::getParkOrgId).collect(Collectors.toSet());
            Map<String, ?> parkOrgId2NameMap = organizationService.id2ValueMap(parkOrgIds, Organization::getOrgName);
            result.forEach((k, v) -> {
                v.setParkOrgName((String) parkOrgId2NameMap.get(v.getParkOrgId()));
            });
        }
        if (dto.isShowCompanyName()) {
            Set<String> companyIds = list.parallelStream().map(EmployeeDTO::getCompanyId).collect(Collectors.toSet());
            Map<String, ?> companyId2NameMap = companyService.id2ValueMap(companyIds, Company::getName);
            result.forEach((k, v) -> {
                v.setCompanyName((String) companyId2NameMap.get(v.getCompanyId()));
            });
        }
        if (dto.isShowJobCodes()) {
            Map<String, Set<Integer>> employeeJobCodesMap = this.getJobCodeByEmployeeIds(result.keySet());
            result.forEach((k, v) -> {
                Set<Integer> jobCodes = employeeJobCodesMap.get(k);
                if (ObjectUtil.isNotNull(jobCodes)) {
                    v.setJobCodes(Lists.newArrayList(jobCodes));
                }
            });
        }
        if (dto.isShowTagNames()) {
            Set<String> tagIds = list.parallelStream().filter(t -> CollectionUtil.isNotEmpty(t.getTagIds())).flatMap(t -> t.getTagIds().stream()).collect(Collectors.toSet());
            Map<String, String> tagId2NameMap = tagService.id2Name(tagIds);
            result.forEach((k, v) -> {
                if (ObjectUtil.isNotEmpty(v.getTagIds())) {
                    List<String> tagNames = v.getTagIds().parallelStream().map(tagId -> tagId2NameMap.get(tagId)).collect(Collectors.toList());
                    v.setTagNames(tagNames);
                }
            });
        }
    }

    private LambdaQueryWrapper<Employee> employeeConditionQuery(EmployeeQueryDTO dto, String currentHeadOrgId) {
        String[] columns = ObjectUtils.addObjectToArray(dto.getColumns(), "id");
        if (dto.isShowParkOrgName() && !ArrayUtil.contains(columns, "park_org_id")) {
            columns = ObjectUtils.addObjectToArray(columns, "park_org_id");
        }
        if (dto.isShowCompanyName() && !ArrayUtil.contains(columns, "company_id")) {
            columns = ObjectUtils.addObjectToArray(columns, "company_id");
        }
        if (dto.isShowTagNames() && !ArrayUtil.contains(columns, "tag_ids")) {
            columns = ObjectUtils.addObjectToArray(columns, "tag_ids");
        }
        LambdaQueryWrapper<Employee> lambdaQuery = Wrappers.<Employee>query()
                .select(columns).lambda()
                .in(CollectionUtil.isNotEmpty(dto.getEmployeeIds()), Employee::getId, dto.getEmployeeIds())
                .eq(StringUtils.hasText(dto.getMobile()), Employee::getMobile, dto.getMobile())
                .like(StringUtils.hasText(dto.getLikeName()), Employee::getName, dto.getLikeName())
                .eq(StringUtils.hasText(dto.getParkOrgId()), Employee::getParkOrgId, dto.getParkOrgId())
                .eq(Employee::getStatus, StatusEnum.ENABLE.getCode());
        if (StrUtil.isNotBlank(currentHeadOrgId)) {
            Set<String> parkOrgIds = organizationManageService.getTenantParkOrgIds(Sets.newHashSet(currentHeadOrgId));
            if (CollUtil.isEmpty(parkOrgIds)) {
                return null;
            }
            lambdaQuery.in(Employee::getParkOrgId, parkOrgIds);
        }
        return lambdaQuery;
    }


}
