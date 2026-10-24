package com.crlink.smartpark.system.service.parkmanagement.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.system.entity.JobEmployeeRelation;
import com.crlink.smartpark.system.mapper.JobEmployeeRelationMapper;
import com.crlink.smartpark.system.service.parkmanagement.JobEmployeeRelationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 岗位用户关系 服务实现类
 * </p>
 * */
@Service
public class JobEmployeeRelationServiceImpl extends ServiceImpl<JobEmployeeRelationMapper, JobEmployeeRelation> implements JobEmployeeRelationService {

    @Override
    public Map<String, List<String>> jobId2EmployeeIdsMap(Collection<String> jobIds) {
        if (CollectionUtil.isEmpty(jobIds)) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> jobId2EmployeeIdListMap = this.lambdaQuery()
                .select(JobEmployeeRelation::getJobId, JobEmployeeRelation::getEmployeeId)
                .in(JobEmployeeRelation::getJobId, jobIds).list()
                .parallelStream().collect(Collectors.groupingBy(JobEmployeeRelation::getJobId, Collectors.mapping(JobEmployeeRelation::getEmployeeId, Collectors.toList())));
        return jobId2EmployeeIdListMap;
    }

    @Override
    public Map<String, List<String>> employeeId2JobIdListMap(Collection<String> employeeIds) {
        if (CollectionUtil.isEmpty(employeeIds)) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> employeeId2JobIdListMap = this.lambdaQuery()
                .select(JobEmployeeRelation::getJobId, JobEmployeeRelation::getEmployeeId)
                .in(JobEmployeeRelation::getEmployeeId, employeeIds).list()
                .parallelStream().collect(Collectors.groupingBy(JobEmployeeRelation::getEmployeeId, Collectors.mapping(JobEmployeeRelation::getJobId, Collectors.toList())));
        return employeeId2JobIdListMap;
    }
}
