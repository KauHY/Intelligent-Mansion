package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.controller.request.ActivityReqDTO;
import com.crlink.smartpark.customer.controller.request.query.ActivityQuery;
import com.crlink.smartpark.customer.controller.response.ActivityRespDTO;
import com.crlink.smartpark.customer.controller.response.ActivityToppingReqDTO;
import com.crlink.smartpark.customer.controller.response.MyRegisterRecord;
import com.crlink.smartpark.customer.dto.ActivityDTO;
import com.crlink.smartpark.customer.entity.Activity;
import com.crlink.smartpark.customer.vo.ActivityList;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动 服务类
 * </p>
 * */
public interface ActivityService extends IService<Activity> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Activity, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateActivity(ActivityReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ActivityDTO getActivityById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<ActivityRespDTO> listActivityPage(ActivityQuery query);

    /**
     * 小程序，园区活动分页
     *
     * @param query
     * @return
     */
    PageInfo<ActivityDTO> wxListActivityPage(BaseQuery<String> query);

    /**
     * 我的报名活动
     *
     * @param query
     * @return
     */
    PageInfo<ActivityDTO> wxMyListActivityPage(BaseQuery<String> query);

    /**
     * 我的报名，报名详情
     *
     * @param activityId
     * @return
     */
    MyRegisterRecord getWxMyRegisterRecordDetail(String activityId);

    /**
     * 活动是否置顶
     *
     * @param activityId
     * @return
     */
    Boolean toppingActivity(ActivityToppingReqDTO activityId);

    List<ActivityList> getActivityList(String parkOrgId);
}
