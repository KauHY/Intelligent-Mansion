package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.business.dto.chance.BusinessChanceRecordDTO;
import com.crlink.smartpark.business.entity.BusinessChanceRecord;

/**
 * <p>
 * 商机操作记录 服务类
 * </p>
 * */
public interface BusinessChanceRecordService extends IService<BusinessChanceRecord> {


    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    BusinessChanceRecordDTO saveOrUpdate(BusinessChanceRecordDTO dto);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    BusinessChanceRecordDTO getBusinessChanceRecordById(String id);

}
