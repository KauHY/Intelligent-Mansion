package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.MeetingScheduleTime;
import com.crlink.smartpark.customer.model.query.MeetingScheduleTimeQuery;
import com.crlink.smartpark.customer.model.MeetingScheduleTimeReqDTO;
import com.crlink.smartpark.customer.dto.MeetingScheduleTimeDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 可预定时段管理 服务类
 * </p>
 * */
public interface MeetingScheduleTimeService extends IService<MeetingScheduleTime> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingScheduleTime, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingScheduleTime(MeetingScheduleTimeReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingScheduleTimeDTO getMeetingScheduleTimeById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingScheduleTimeDTO> listMeetingScheduleTimePage(MeetingScheduleTimeQuery query);
}
