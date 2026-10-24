package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.system.dto.job.JobDTO;
import com.crlink.smartpark.system.dto.job.JobReqDTO;
import com.crlink.smartpark.system.query.JobQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface JobManageService {
    Boolean saveOrUpdateJob(JobReqDTO jobDTO);

    PageInfo<JobDTO> listJobPage(JobQuery jobQuery);

    List<DictVO> listJobDict(String companyId);

    Boolean removeJobById(String id);
}
