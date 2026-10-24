package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.controller.request.ActivityRegisterRecordReqDTO;
import com.crlink.smartpark.customer.controller.request.query.ActivityRegisterRecordQuery;
import com.crlink.smartpark.customer.dto.ActivityRegisterRecordDTO;
import com.crlink.smartpark.customer.entity.ActivityRegisterRecord;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 报名记录 服务类
 * </p>
 * */
public interface ActivityRegisterRecordService extends IService<ActivityRegisterRecord> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ActivityRegisterRecord, ?> sValue);

    Integer queryRegisteredCount(String activityId);

    /**
     * 获取用户报名的所有活动Id
     *
     * @param userIds
     * @return
     */
    Map<String, Set<String>> queryUserRegisterActivityIds(Collection<String> userIds);

    Boolean isRegistered(String activityId, String userId);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateActivityRegisterRecord(ActivityRegisterRecordReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ActivityRegisterRecordDTO getActivityRegisterRecordById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<ActivityRegisterRecordDTO> listActivityRegisterRecordPage(ActivityRegisterRecordQuery query);

    void export(ActivityRegisterRecordQuery query);
}
