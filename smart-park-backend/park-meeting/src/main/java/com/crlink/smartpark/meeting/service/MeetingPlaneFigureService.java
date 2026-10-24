package com.crlink.smartpark.meeting.service;

import com.crlink.smartpark.meeting.entity.MeetingPlaneFigure;
import com.crlink.smartpark.meeting.model.dto.MeetingPlaneFigureReqDTO;
import com.crlink.smartpark.meeting.vo.MeetingPlaneFigureVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.meeting.model.query.MeetingPlaneFigureTreeQuery;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 会议室平面图 服务类
 * </p>
 * */
public interface MeetingPlaneFigureService extends IService<MeetingPlaneFigure> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingPlaneFigure, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveMeetingPlaneFigure(MeetingPlaneFigureReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingPlaneFigureVO getMeetingPlaneFigureById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingPlaneFigureVO> listMeetingPlaneFigurePage(MeetingPlaneFigureTreeQuery query);

    String findPlaneFigure(String buildingId, String floorNum);
}
