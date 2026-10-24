package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.entity.JobEmployeeRelation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 岗位用户关系 服务类
 * </p>
 * */
public interface JobEmployeeRelationService extends IService<JobEmployeeRelation> {

    Map<String, List<String>> jobId2EmployeeIdsMap(Collection<String> jobIds);

    Map<String, List<String>> employeeId2JobIdListMap(Collection<String> employeeIds);
}
