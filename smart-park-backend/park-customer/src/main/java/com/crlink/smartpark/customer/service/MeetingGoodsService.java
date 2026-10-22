package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.entity.MeetingGoods;
import com.crlink.smartpark.customer.model.query.MeetingGoodsQuery;
import com.crlink.smartpark.customer.model.MeetingGoodsReqDTO;
import com.crlink.smartpark.customer.dto.MeetingGoodsDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 会议室物资管理 服务类
 * </p>
 * */
public interface MeetingGoodsService extends IService<MeetingGoods> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingGoods, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingGoods(MeetingGoodsReqDTO reqDTO);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean removeByGoodsId(String id);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingGoodsDTO getMeetingGoodsById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingGoodsDTO> listMeetingGoodsPage(MeetingGoodsQuery query);
}
