package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.system.entity.Job;
import com.crlink.smartpark.system.mapper.JobMapper;
import com.crlink.smartpark.system.service.parkmanagement.JobService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作岗位 服务实现类
 * </p>
 * */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

//    @Override
//    public Map<String, Integer> id2JobCodeMap(Set<String> ids) {
//        if (CollectionUtil.isEmpty(ids)) {
//            return Collections.emptyMap();
//        }
//        Map<String, Integer> id2JobCodeMap = this.lambdaQuery()
//                .select(Job::getId, Job::getJobCode)
//                .in(Job::getId, ids)
//                .list().parallelStream().collect(Collectors.toMap(Job::getId, Job::getJobCode));
//        return id2JobCodeMap;
//    }

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Job, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> jobId2ValueMap = this.lambdaQuery()
                .select(Job::getId, sValue)
                .in(Job::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(Job::getId, sValue));
        return jobId2ValueMap;
    }

    @Override
    public Map<Integer, Set<String>> jobCode2JobIds(String parkOrgId, Set<Integer> jobCodes) {
        Map<Integer, Set<String>> jobCode2JobIdsMap = this.lambdaQuery().select(Job::getId, Job::getJobCode)
                .eq(Job::getParkOrgId, parkOrgId)
                .in(Job::getJobCode, jobCodes).list()
                .parallelStream().collect(Collectors.groupingBy(Job::getJobCode, Collectors.mapping(Job::getId, Collectors.toSet())));
        return jobCode2JobIdsMap;
    }

    @Override
    public Map<Integer, String> jobCode2JobIdByCompanyId(String companyId, Collection<Integer> jobCodes) {
        Map<Integer, String> jobCode2JobIdMap = this.lambdaQuery().select(Job::getId, Job::getJobCode)
                .eq(Job::getCompanyId, companyId)
                .in(Job::getJobCode, jobCodes).list()
                .parallelStream().collect(Collectors.toMap(Job::getJobCode, Job::getId));
        return jobCode2JobIdMap;
    }


}
