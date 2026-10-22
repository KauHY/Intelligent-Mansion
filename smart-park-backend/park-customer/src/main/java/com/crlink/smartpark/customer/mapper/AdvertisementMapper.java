package com.crlink.smartpark.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.customer.controller.request.query.AdvertisementQuery;
import com.crlink.smartpark.customer.entity.Advertisement;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 广告 Mapper 接口
 * </p>
 * */
public interface AdvertisementMapper extends BaseMapper<Advertisement> {

    /**
     * 当状态查询条件为空时
     *
     * @param query
     * @param parkOrgIds
     * @param now
     * @return
     */
    List<Advertisement> pageAdvertisementList(@Param("query") AdvertisementQuery query,
                                              @Param("parkOrgIds") Set<String> parkOrgIds,
                                              @Param("now") LocalDateTime now);
}
