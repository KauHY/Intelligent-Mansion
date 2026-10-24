package com.crlink.smartpark.meeting.service;

import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.entity.MeetingReserve;
import com.crlink.smartpark.meeting.model.dto.MeetingReserveAddDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.meeting.model.dto.MeetingReserveGetDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingTimeGetDTO;
import com.crlink.smartpark.meeting.model.query.MeetingReserveQuery;
import com.crlink.smartpark.meeting.model.vo.MeetingByBuildingVO;
import com.crlink.smartpark.meeting.model.vo.MeetingReserveVO;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.crlink.smartpark.meeting.model.vo.ReserveTimeVO;

/**
 * <p>
 * 会议室预订 服务类
 * </p>
 * */
public interface MeetingReserveService extends IService<MeetingReserve> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReserve, ?> sValue);

    /**
     * 保存数据
     *
     * @return 实体对象
     */
    Object saveOrUpdateMeetingReserve(MeetingReserveAddDTO reqDTO);

    /**
     * 根据楼栋获取会议室
     *
     * @return 实体对象
     */
    List<MeetingByBuildingVO> getMeeting(MeetingReserveGetDTO reqDTO);

    /**
     * 根据楼栋获取会议室
     *
     * @return 实体对象
     */
    List<ReserveTimeVO> getMeetingTime(MeetingTimeGetDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingReserve getMeetingReserveById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    List<MeetingReserveVO> listMeetingReserve(MeetingReserveQuery query);


}
