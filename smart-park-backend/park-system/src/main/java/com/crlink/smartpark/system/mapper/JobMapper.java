package com.crlink.smartpark.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.entity.Job;

/**
 * <p>
 * 工作岗位 Mapper 接口
 * </p>
 * */
public interface JobMapper extends BaseMapper<Job> {

    //List<Job> pageJobList(@Param("param") JobQuery param, @Param("parkOrgIds") Set<String> parkOrgIds);
}
