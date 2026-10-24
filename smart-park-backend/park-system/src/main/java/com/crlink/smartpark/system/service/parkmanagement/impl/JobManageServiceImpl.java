package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.job.JobDTO;
import com.crlink.smartpark.system.dto.job.JobReqDTO;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.query.JobQuery;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.*;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobManageServiceImpl implements JobManageService {
    @Autowired
    private JobService jobService;

    @Autowired
    private JobEmployeeRelationService jobRelationService;

//    @Autowired
//    private OrganizationManageService organizationManageService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrganizationService organizationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateJob(JobReqDTO jobDTO) {
        checkJobCode(jobDTO.getId(), jobDTO.getJobCode(), jobDTO.getCompanyId());
        Job job = MyBeanUtils.copy(jobDTO, Job::new);
        if (jobDTO.getId() != null) {
            EntityUserUtil.setUpdateBy(job);
            jobRelationService.remove(Wrappers.<JobEmployeeRelation>lambdaQuery().eq(JobEmployeeRelation::getJobId, jobDTO.getId()));
        } else {
            EntityUserUtil.setAll(job);
        }
        jobService.saveOrUpdate(job);
        List<JobEmployeeRelation> jobEmployeeRelations = jobDTO.getEmployeeIds().parallelStream().map(employeeId -> {
            JobEmployeeRelation jobRelation = new JobEmployeeRelation();
            jobRelation.setJobId(job.getId());
            jobRelation.setEmployeeId(employeeId);
            return jobRelation;
        }).collect(Collectors.toList());
        jobRelationService.saveBatch(jobEmployeeRelations);
        return true;
    }

    /**
     * 岗位分页查询
     *
     * @param jobQuery
     * @return
     */
    @Override
    public PageInfo<JobDTO> listJobPage(JobQuery jobQuery) {
        //Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(jobQuery.getParkOrgId());
        Set<String> parkOrgIds = getParkOrgIds(jobQuery.getParkOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        String employeeName = jobQuery.getEmployeeName();
        Integer companyType = jobQuery.getCompanyType();
        Integer jobCode = jobQuery.getJobCode();
        LambdaQueryChainWrapper<Job> wrapper = jobService.lambdaQuery().select(Job::getId, Job::getJobCode, Job::getParkOrgId, Job::getCompanyId)
                .eq(Objects.nonNull(jobCode), Job::getJobCode, jobCode)
                .eq(StringUtils.hasText(jobQuery.getCompanyId()), Job::getCompanyId, jobQuery.getCompanyId())
                .in(Job::getParkOrgId, parkOrgIds)
                .orderByDesc(Job::getCreateTime);

        if (StringUtils.hasText(employeeName)) {
            Set<String> jobIds = queryJobEmployee(employeeName);
            MyPageInfoUtils.isEmptyPage(jobIds);
            wrapper.in(Job::getId, jobIds);
        }
        if (Objects.nonNull(companyType)) {
            Set<String> companyIds = queryCompanyType(parkOrgIds, companyType);
            MyPageInfoUtils.isEmptyPage(companyIds);
            wrapper.in(Job::getCompanyId, companyIds);
        }
        jobQuery.startPage();
        List<Job> jobs = wrapper.list();
        MyPageInfoUtils.isEmptyPage(jobs);
        List<JobDTO> page = jobs2JobDTO(jobs);
        return jobQuery.createPageInfo(page);
    }


    @Override
    public List<DictVO> listJobDict(String companyId) {
        List<Job> jobs = jobService.lambdaQuery().select(Job::getId, Job::getJobCode).eq(Job::getCompanyId, companyId).list();
        Map<Object, String> map = DictUtil.getByType("JOBCODE").values().parallelStream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        List<DictVO> result = jobs.parallelStream().map(job -> new DictVO(map.get(job.getJobCode()), job.getId())).collect(Collectors.toList());
        return result;

    }

    @Override
    public Boolean removeJobById(String id) {
        boolean result = jobService.removeById(id);
        return result;
    }

    private Set<String> queryCompanyType(Set<String> parkOrgIds, Integer companyType) {
        Set<String> companyIds = Collections.emptySet();
        if (companyType != null) {
            companyIds = companyService.lambdaQuery()
                    .select(Company::getId)
                    .in(Company::getParkOrgId, parkOrgIds)
                    .eq(Company::getCompanyType, companyType).list()
                    .parallelStream().map(Company::getId).collect(Collectors.toSet());
        }
        return companyIds;
    }

    private List<JobDTO> jobs2JobDTO(List<Job> jobs) {
        Map<Object, String> jobNameMap = DictUtil.getByType("JOBCODE").values().parallelStream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        Set<String> jobIds = jobs.parallelStream().map(t -> t.getId()).collect(Collectors.toSet());
        Map<String, List<String>> jobId2EmployeeIdsMap = jobRelationService.jobId2EmployeeIdsMap(jobIds);
        Set<String> employeeIds = jobId2EmployeeIdsMap.values().parallelStream().flatMap(t -> t.stream()).collect(Collectors.toSet());
        Map<String, ?> employeeId2NameMap = employeeService.id2ValueMap(employeeIds, Employee::getName);
        Set<String> companyIds = jobs.parallelStream().map(t -> t.getCompanyId()).collect(Collectors.toSet());
        Map<String, Company> id2CompanyMap = companyService.id2CompanyMap(companyIds, Company::getName, Company::getCompanyType);
        Set<String> parkOrgIds = jobs.parallelStream().map(t -> t.getParkOrgId()).collect(Collectors.toSet());
        Map<String, String> parkOrgId2OrgNameMap = (Map<String, String>) organizationService.id2ValueMap(parkOrgIds, Organization::getOrgName);
        List<JobDTO> result = jobs.parallelStream().map(job -> {
            JobDTO jobDTO = MyBeanUtils.copy(job, JobDTO::new);
            jobDTO.setJobName(jobNameMap.get(job.getJobCode()));
            jobDTO.setParkOrgName(parkOrgId2OrgNameMap.get(job.getParkOrgId()));
            Company company = id2CompanyMap.get(job.getCompanyId());
            jobDTO.setCompanyName(company.getName());
            jobDTO.setCompanyType(company.getCompanyType());
            List<String> employeeIdList = jobId2EmployeeIdsMap.getOrDefault(job.getId(), Collections.emptyList());
            jobDTO.setEmployeeIds(employeeIdList);
            List<String> employeeNames = employeeIdList.parallelStream().map(t -> (String) employeeId2NameMap.get(t)).collect(Collectors.toList());
            jobDTO.setEmployeeNames(employeeNames);
            return jobDTO;
        }).collect(Collectors.toList());
        return result;
    }

    private Set<String> queryJobEmployee(String employeeName) {
//        if (StringUtils.hasText(employeeName)) {
        Set<String> employeeIds = employeeService.lambdaQuery()
                .select(Employee::getId)
                .like(Employee::getName, employeeName).list()
                .parallelStream().map(Employee::getId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(employeeIds)) {
            Set<String> jobIds = jobRelationService.lambdaQuery()
                    .select(JobEmployeeRelation::getJobId)
                    .in(JobEmployeeRelation::getEmployeeId, employeeIds).list()
                    .parallelStream().map(JobEmployeeRelation::getJobId).collect(Collectors.toSet());
            return jobIds;
        } else {
            return Collections.emptySet();
        }

//        }
//        return Collections.emptySet();
    }

    private void checkJobCode(String jobId, Integer jobCode, String companyId) {
        Integer count = jobService.lambdaQuery().ne(StringUtils.hasText(jobId), Job::getId, jobId)
                .eq(Job::getJobCode, jobCode)
                .eq(Job::getCompanyId, companyId)
                .count();
        if (count > 0) {
            throw new ResolinkRuntimeException("30004");
        }
    }

    /**
     * 分页查询时，园区查询范围
     *
     * @param parkOrgId
     * @return
     */
    private Set<String> getParkOrgIds(String parkOrgId) {
        if (StringUtils.hasText(parkOrgId)) {
            return Sets.newHashSet(parkOrgId);
        } else {
            Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
            return parkOrgIds;
        }
    }
}
