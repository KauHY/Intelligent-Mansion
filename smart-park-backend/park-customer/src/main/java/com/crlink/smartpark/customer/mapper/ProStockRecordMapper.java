package com.crlink.smartpark.customer.mapper;

import com.crlink.smartpark.customer.controller.request.query.ProStockManagerQuery;
import com.crlink.smartpark.customer.controller.request.query.ProStockRecordQuery;
import com.crlink.smartpark.customer.dto.ProStockRecordDTO;
import com.crlink.smartpark.customer.entity.ProStockRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 库存出入库记录 Mapper 接口
 * </p>
 * */
public interface ProStockRecordMapper extends BaseMapper<ProStockRecord> {

    List<ProStockRecordDTO> listPage(@Param("query") ProStockRecordQuery query,
        @Param("parkOrgIds") Set<String> parkOrgIds);
}
