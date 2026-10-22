package com.crlink.smartpark.meeting.service;

import com.crlink.smartpark.meeting.entity.MeetingPermission;
import com.crlink.smartpark.meeting.controller.request.query.MeetingPermissionQuery;
import com.crlink.smartpark.meeting.model.dto.MeetingPermissionReqDTO;
import com.crlink.smartpark.meeting.dto.MeetingPermissionDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 会议室权限关联 服务类
 * </p>
 * */
public interface MeetingPermissionService extends IService<MeetingPermission> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingPermission, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingPermission(MeetingPermissionReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingPermissionDTO getMeetingPermissionById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingPermissionDTO> listMeetingPermissionPage(MeetingPermissionQuery query);

    /**
     * 根据会议室ID移除相关权限列表
     * @param meetingId:
     * @return  java.lang.Boolean
     * @MethodName removePermissionByMeetingId
     */
    Boolean removePermissionByMeetingId(String meetingId);


    /**
     * 根据会议室ID查询权限列表
     * @param meetingId: 会议室ID
     * @return  java.util.List<com.crlink.smartpark.meeting.dto.MeetingPermissionDTO>
     * @MethodName findMeetingPermissionByMeetingId
     */
    List<MeetingPermissionDTO> findMeetingPermissionByMeetingId(String meetingId);

    /**
     * 根据 会议室ID 更新权限
     * @param permissionList: 权限列表
     * @param relationType: 权限类型
     * @param meetingId: 会议室ID
     * @return  java.lang.Boolean
     * @MethodName updateMeetingPermisByMeetingId
     */
    Boolean updatePermissionByMeetingId(Set<String> permissionList,Integer relationType,String meetingId);

    /**
     * 获取权限
     *
     * @param
     * @return 分页结果
     */
    Set<String> getPermission(Set<String> meetingIds, String employeeId, String companyId);

    /**
     * 获取权限
     *
     * @param
     * @return 分页结果
     */
    boolean noPermission(String meetingId, String employeeId, String companyId);

    /**
     * 判断当前用户有无任一会议室权限
     * @return  java.lang.Boolean
     * @MethodName checkPermission
     */
    Boolean checkPermission(String parkOrgId);
}
