package com.crlink.smartpark.customer.mapper;

import com.crlink.smartpark.customer.controller.request.query.ProStockManagerQuery;
import com.crlink.smartpark.customer.dto.ProStockManagerDTO;
import com.crlink.smartpark.customer.entity.ProStockManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.customer.model.vo.StockManagerSelectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 库存管理 Mapper 接口
 * </p>
 * */
public interface ProStockManagerMapper extends BaseMapper<ProStockManager> {

    List<StockManagerSelectVo> getStockByGoodsIds(@Param("goodsSet") Set<String> goodsSet);

    List<ProStockManagerDTO> listPage(@Param("query") ProStockManagerQuery query, @Param("parkOrgIds") Set<String> orgIds);
}
