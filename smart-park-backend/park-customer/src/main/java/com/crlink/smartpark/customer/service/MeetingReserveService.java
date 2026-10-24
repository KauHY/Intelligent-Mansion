package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.dto.MeetingReserveDTO;
import com.crlink.smartpark.customer.entity.MeetingReserve;
import com.crlink.smartpark.customer.model.MeetingReserveInsertDTO;
import com.crlink.smartpark.customer.model.MeetingReserveReqDTO;
import com.crlink.smartpark.customer.model.MeetingReserveReviewDTO;
import com.crlink.smartpark.customer.model.query.MeetingReserveQuery;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租赁会议室预订 服务类
 * </p>
 * */
public interface MeetingReserveService extends IService<MeetingReserve> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReserve, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingReserve(MeetingReserveReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeetingReserveDTO getMeetingReserveById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingReserveDTO> listMeetingReservePage(MeetingReserveQuery query);

    /**
     * 新增会议室预定-小程序
     *
     * @param reqDTO 新增入参
     * @return java.lang.Boolean 成功失败
     * @Description
     * @Date 2022/3/11
     * @MethodName saveMeetingReserve
     */
    String saveMeetingReserve(MeetingReserveInsertDTO reqDTO);

    /**
     * 订单查询-小程序
     *
     * @return com.github.pagehelper.PageInfo<com.crlink.smartpark.customer.dto.MeetingReserveDTO>
     * @param: [query]     * @date 2022/3/11
     * @methodName listWxMeetingReservePage
     */
    PageInfo<MeetingReserveDTO> listWxMeetingReservePage(MeetingReserveQuery query);

    /**
     * 预定-审核-PC
     *
     * @param reqDTO:
     * @return java.lang.Boolean
     * @MethodName review
     */
    Boolean review(MeetingReserveReviewDTO reqDTO);

    /**
     * 取消申请
     *
     * @param id:
     * @return java.lang.Boolean
     * @MethodName cancel
     */
    Boolean cancel(String id);


    /**
     * 预定订单导出
     *
     * @param query:
     * @return void
     * @MethodName export
     */
    void export(MeetingReserveQuery query);

    List<MeetingReserveDTO> getMeetingReserveBillList(String parkOrgId, Boolean nearTwelveMonths);
}
