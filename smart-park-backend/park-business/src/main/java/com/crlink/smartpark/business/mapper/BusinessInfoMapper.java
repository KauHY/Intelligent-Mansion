package com.crlink.smartpark.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.business.query.BusinessInfoQuery;
import com.crlink.smartpark.business.entity.BusinessInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 * */
public interface BusinessInfoMapper extends BaseMapper<BusinessInfo> {

    public void listBusinessInfoPage(@Param("param") BusinessInfoQuery query);

}
