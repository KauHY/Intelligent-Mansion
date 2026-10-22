package com.crlink.smartpark.fee.mapper;

import com.crlink.smartpark.fee.dto.BillingSchemeQuery;
import com.crlink.smartpark.fee.entity.BillingScheme;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 计费方案 Mapper 接口
 * </p>
 * */
public interface BillingSchemeMapper extends BaseMapper<BillingScheme> {

    List<BillingScheme> listBillingSchemePage(BillingSchemeQuery query);
}
