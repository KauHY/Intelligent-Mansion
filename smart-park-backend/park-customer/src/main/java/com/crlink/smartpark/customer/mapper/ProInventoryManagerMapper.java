package com.crlink.smartpark.customer.mapper;

import com.crlink.smartpark.customer.dto.ProInventoryManagerDTO;
import com.crlink.smartpark.customer.entity.ProInventoryManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.customer.model.query.ProInventoryManagerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 盘点管理 Mapper 接口
 * </p>
 * */
public interface ProInventoryManagerMapper extends BaseMapper<ProInventoryManager> {

    List<ProInventoryManagerDTO> listPage(@Param("query") ProInventoryManagerQuery query,@Param("parkOrgIds")  Set<String> parkOrgIds);
}
