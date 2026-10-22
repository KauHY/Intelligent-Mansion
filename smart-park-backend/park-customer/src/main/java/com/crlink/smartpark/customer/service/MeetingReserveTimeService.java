package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.MeetingReserveTime;
import com.crlink.smartpark.customer.model.query.MeetingReserveTimeQuery;
import com.crlink.smartpark.customer.model.MeetingReserveTimeReqDTO;
import com.crlink.smartpark.customer.dto.MeetingReserveTimeDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 租赁会议室预订时间管理 服务类
 * </p>
 * */
public interface MeetingReserveTimeService extends IService<MeetingReserveTime> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReserveTime, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingReserveTime(MeetingReserveTimeReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingReserveTimeDTO getMeetingReserveTimeById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingReserveTimeDTO> listMeetingReserveTimePage(MeetingReserveTimeQuery query);

    /**
     * 取消申请或审核不通过 释放时间
     * @param meetingReserveId:
     * @return  void
     * @MethodName changeMeetingStates
     */
    void changeMeetingStates(String meetingReserveId);
}
