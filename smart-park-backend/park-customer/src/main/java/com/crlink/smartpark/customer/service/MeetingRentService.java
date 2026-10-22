package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.controller.request.query.MeetingRentStatusDTO;
import com.crlink.smartpark.customer.dto.MeetingRentDTO;
import com.crlink.smartpark.customer.entity.MeetingRent;
import com.crlink.smartpark.customer.model.MeetingRentReqDTO;
import com.crlink.smartpark.customer.model.query.MeetingRentQuery;
import com.crlink.smartpark.customer.model.query.MeetingRentWxQuery;
import com.crlink.smartpark.customer.model.vo.MeetingOccupationRatioVO;
import com.crlink.smartpark.customer.model.vo.MeetingRentDetailVO;
import com.crlink.smartpark.customer.model.vo.MeetingRentDetailWxVO;
import com.crlink.smartpark.customer.model.vo.MeetingRentReserveVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 会议室租赁管理 服务类
 * </p>
 * */
public interface MeetingRentService extends IService<MeetingRent> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingRent, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingRent(MeetingRentReqDTO reqDTO);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean removeByMeetingId(String id);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean updateStatus(MeetingRentStatusDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingRentDetailVO getMeetingRentById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingRentDTO> listMeetingRentPage(MeetingRentQuery query);

    /**
     * C端小程序页面查询可租赁会议室列表
     * （token存parkOrgId、employeeId）
     * @param query:
     * @return  com.github.pagehelper.PageInfo<com.crlink.smartpark.customer.dto.MeetingRentDTO>
     * @MethodName listWxMeetingRentPage
     */
    PageInfo<MeetingRentDTO> listWxMeetingRentPage(BaseQuery query);

    /**
     * 根据ID查询C端小程序会议室详情数据
     * @param id:
     * @return  com.crlink.smartpark.customer.dto.MeetingRentDTO
     * @MethodName getWxMeetingRentById
     */
    MeetingRentDetailWxVO getWxMeetingRentById(String id);

    /**
     * 根据ID、月份拉取C端会议室小程序预定数据
     * @param query:
     * @return  com.crlink.smartpark.customer.model.vo.MeetingRentReserveVO
     * @MethodName getWxMeetingRentReserve
     */
    MeetingRentReserveVO getWxMeetingRentReserve(MeetingRentWxQuery query);

    /**
     * 获取会议室租赁占用率（运营大屏）
     * @param parkOrgId
     * @return
     */
    MeetingOccupationRatioVO getMeetingOccupationRatio(String parkOrgId);

}
