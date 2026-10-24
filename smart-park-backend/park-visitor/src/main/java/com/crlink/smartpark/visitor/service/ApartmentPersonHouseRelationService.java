package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.entity.ApartmentPersonHouseRelation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 公寓人员管理房屋关系表 服务类
 * </p>
 * */
public interface ApartmentPersonHouseRelationService extends IService<ApartmentPersonHouseRelation> {

    /**
     * 根据Id查询值
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<ApartmentPersonHouseRelation, T> sValue);

    /**
     * 根据公寓人员id查询对应的所有房屋id
     *
     * @param personIds
     * @return
     */
    Map<String, Set<String>> personId2HouseIds(Collection<String> personIds);

    /**
     * 根据房屋id查询对应的所有员工id
     *
     * @param houseIds
     * @return
     */
    Map<String, Set<String>> houseId2EmployeeIds(Collection<String> houseIds);
}
