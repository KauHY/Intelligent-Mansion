package com.crlink.smartpark.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.customer.entity.Information;
import com.crlink.smartpark.customer.model.query.InformationQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 公告、资讯信息表 Mapper 接口
 * </p>
 * */
public interface InformationMapper extends BaseMapper<Information> {

    List<Information> pageInformationList(@Param("param") InformationQuery param, @Param("parkOrgIds") Set<String> orgIds);


}
