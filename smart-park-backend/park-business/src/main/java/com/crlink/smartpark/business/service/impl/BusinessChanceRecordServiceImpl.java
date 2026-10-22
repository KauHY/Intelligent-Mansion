package com.crlink.smartpark.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.business.dto.chance.BusinessChanceRecordDTO;
import com.crlink.smartpark.business.entity.BusinessChanceRecord;
import com.crlink.smartpark.business.mapper.BusinessChanceRecordMapper;
import com.crlink.smartpark.business.service.BusinessChanceRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商机操作记录 服务实现类
 * </p>
 * */
@Service
public class BusinessChanceRecordServiceImpl extends ServiceImpl<BusinessChanceRecordMapper, BusinessChanceRecord> implements BusinessChanceRecordService {

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 存储对象
     */
    @Override
    public BusinessChanceRecordDTO saveOrUpdate(BusinessChanceRecordDTO dto) {
        if (dto != null) {
            BusinessChanceRecord entity = new BusinessChanceRecord();
            BeanUtils.copyProperties(dto, entity);
            if (saveOrUpdate(entity)) {
                dto.setId(entity.getId());
            }
        }
        return dto;
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public BusinessChanceRecordDTO getBusinessChanceRecordById(String id) {
        if (StringUtils.isNotBlank(id)) {
            BusinessChanceRecord entity = getById(id);
            if (entity != null) {
                BusinessChanceRecordDTO dto = new BusinessChanceRecordDTO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }


    /***
     * entity转DTO
     *
     * @param list entity集合
     * @return DTO集合
     */
    private List<BusinessChanceRecordDTO> listEntityToDto(List<BusinessChanceRecord> list) {
        return list.stream().map(entity -> {
            BusinessChanceRecordDTO temp = new BusinessChanceRecordDTO();
            BeanUtils.copyProperties(entity, temp);
            return temp;
        }).collect(Collectors.toList());
    }
}
