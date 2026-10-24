package com.crlink.smartpark.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.customer.entity.Activity;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 活动 Mapper 接口
 * </p>
 * */
public interface ActivityMapper extends BaseMapper<Activity> {

    /**
     * 运营大屏使用
     * @param parkOrgId
     * @param now
     * @return
     */
    List<Activity> getActivityList(@Param("parkOrgId") String parkOrgId,
                                         @Param("now") LocalDateTime now);
}
