package com.crlink.smartpark.customer.mapper;

import com.crlink.smartpark.customer.controller.request.query.ProReceiveManagerQuery;
import com.crlink.smartpark.customer.dto.ProReceiveManagerDTO;
import com.crlink.smartpark.customer.entity.ProReceiveManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 物品领用管理 Mapper 接口
 * </p>
 * */
public interface ProReceiveManagerMapper extends BaseMapper<ProReceiveManager> {

    List<ProReceiveManagerDTO> listPage(@Param("query") ProReceiveManagerQuery query,
                                        @Param("parkOrgIds") Set<String> parkOrgIds,
                                        @Param("employeeIds") Set<String> employeeIds);

    List<ProReceiveManagerDTO> listWxPage(@Param("query") ProReceiveManagerQuery query);
}
