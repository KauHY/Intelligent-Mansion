package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.MeetingPermission;
import com.crlink.smartpark.customer.model.query.MeetingPermissionQuery;
import com.crlink.smartpark.customer.model.MeetingPermissionReqDTO;
import com.crlink.smartpark.customer.dto.MeetingPermissionDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

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
}
