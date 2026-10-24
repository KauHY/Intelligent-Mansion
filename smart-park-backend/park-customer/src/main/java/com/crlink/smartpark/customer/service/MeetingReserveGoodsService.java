package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.MeetingReserveGoods;
import com.crlink.smartpark.customer.model.query.MeetingReserveGoodsQuery;
import com.crlink.smartpark.customer.model.MeetingReserveGoodsReqDTO;
import com.crlink.smartpark.customer.dto.MeetingReserveGoodsDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 租赁会议室预定物资管理 服务类
 * </p>
 * */
public interface MeetingReserveGoodsService extends IService<MeetingReserveGoods> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReserveGoods, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingReserveGoods(MeetingReserveGoodsReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingReserveGoodsDTO getMeetingReserveGoodsById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingReserveGoodsDTO> listMeetingReserveGoodsPage(MeetingReserveGoodsQuery query);
}
