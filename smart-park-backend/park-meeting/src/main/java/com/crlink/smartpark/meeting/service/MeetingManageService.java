package com.crlink.smartpark.meeting.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.meeting.dto.MeetingManageDTO;
import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.model.dto.MeetingManageReqDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingManageStatusDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingOverviewDTO;
import com.crlink.smartpark.meeting.model.query.MeetingManageTreeQuery;
import com.crlink.smartpark.meeting.vo.MeetingManageVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会议室管理 服务类
 * </p>
 * */
public interface MeetingManageService extends IService<MeetingManage> {


    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<MeetingManage, T> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingManage(MeetingManageReqDTO reqDTO);

    /**
     * 启用禁用会议室
     *
     * @param reqDTO:
     * @return java.lang.Boolean
     * @MethodName changeStatusMeetingManage
     */
    Boolean changeStatusMeetingManage(MeetingManageStatusDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeetingManageDTO getMeetingManageById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingManageVO> listMeetingManagePage(MeetingManageTreeQuery query);

    /**
     * 拉取组织树结构
     *
     * @param parkOrgId:
     * @return java.util.List<com.crlink.smartpark.system.vo.DictVO>
     * @MethodName listBuildingDict
     */
    List<DictVO> listBuildingDict(String parkOrgId);

    /**
     * 会议室会议时间概览，idType=1 id为会议室id
     *
     * @param idType
     * @return
     */
    MeetingOverviewDTO getMeetingOverview(String id, Integer idType);

}
