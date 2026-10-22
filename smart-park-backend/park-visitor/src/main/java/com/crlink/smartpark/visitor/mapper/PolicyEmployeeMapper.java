package com.crlink.smartpark.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.visitor.entity.PolicyEmployee;

/**
 * <p>
 * 策略员工表 Mapper 接口
 * </p>
 * */
public interface PolicyEmployeeMapper extends BaseMapper<PolicyEmployee> {

    /**
     * 新增批量数据
     * @param records
     */
    //void saveBatch(@Param("records")List<PolicyEmployee> records);

}
