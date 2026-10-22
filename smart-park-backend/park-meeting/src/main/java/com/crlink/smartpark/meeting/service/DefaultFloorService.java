package com.crlink.smartpark.meeting.service;

import com.crlink.smartpark.meeting.entity.DefaultFloor;
import com.crlink.smartpark.meeting.controller.request.query.DefaultFloorQuery;
import com.crlink.smartpark.meeting.model.dto.DefaultFloorReqDTO;
import com.crlink.smartpark.meeting.dto.DefaultFloorDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 会议室默认楼层 服务类
 * </p>
 * */
public interface DefaultFloorService extends IService<DefaultFloor> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<DefaultFloor, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateDefaultFloor(DefaultFloorReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    DefaultFloorDTO getDefaultFloorById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<DefaultFloorDTO> listDefaultFloorPage(DefaultFloorQuery query);

    /**
     * 根据当前员工ID拉取默认楼栋楼层
     * @return  com.crlink.smartpark.meeting.dto.DefaultFloorDTO
     * @MethodName getDefaultFloor
     */
    DefaultFloorDTO getDefaultFloor();

    /**
     * 设置当前
     * 
     * @param reqDTO:
     * @return java.lang.Boolean
     * @MethodName settingDefaultFloor
     */
    String settingDefaultFloor(DefaultFloorReqDTO reqDTO);
}
