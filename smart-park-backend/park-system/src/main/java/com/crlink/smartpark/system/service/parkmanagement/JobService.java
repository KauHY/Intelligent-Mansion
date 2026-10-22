package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.entity.Job;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 工作岗位 服务类
 * </p>
 * */
public interface JobService extends IService<Job> {
//    Map<String, Integer> id2JobCodeMap(Set<String> ids);

    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Job, ?> sValue);


    Map<Integer, Set<String>> jobCode2JobIds(String parkOrgId, Set<Integer> jobCodes);

    Map<Integer, String> jobCode2JobIdByCompanyId(String companyId, Collection<Integer> jobCodes);
}
