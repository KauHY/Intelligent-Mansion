package com.crlink.smartpark.contract.service.impl;

import com.crlink.smartpark.contract.entity.ContractProcessRecord;
import com.crlink.smartpark.contract.model.query.ContractProcessRecordQuery;
import com.crlink.smartpark.contract.model.request.ContractProcessRecordReqDTO;
import com.crlink.smartpark.contract.dto.ContractProcessRecordDTO;
import com.crlink.smartpark.contract.service.ContractProcessRecordService;
import com.crlink.smartpark.contract.mapper.ContractProcessRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同操作记录 服务实现类
 * </p>
 * */
@Service
public class ContractProcessRecordServiceImpl extends ServiceImpl<ContractProcessRecordMapper, ContractProcessRecord> implements ContractProcessRecordService {

    /**
     * 保存或更新数据
     *
     * @param reqDTO 存储对象
     * @return 存储对象
     */
    @Override
    public Boolean saveOrUpdateContractProcessRecord(ContractProcessRecordReqDTO reqDTO) {
        ContractProcessRecord entity = MyBeanUtils.copy(reqDTO, ContractProcessRecord::new);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public ContractProcessRecordDTO getContractProcessRecordById(String id) {
        if(StringUtils.isNotBlank(id)) {
            ContractProcessRecord entity = getById(id);
            if(entity != null) {
                ContractProcessRecordDTO dto = new ContractProcessRecordDTO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<ContractProcessRecordDTO> listPage(ContractProcessRecordQuery condition) {
        PageInfo<ContractProcessRecordDTO> pageResult = new PageInfo<ContractProcessRecordDTO>();
        if(condition != null) {
            Page<ContractProcessRecord> page;
            if (condition.getPageSize() != -1) {
                page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
            }else{
                page = new Page<ContractProcessRecord>();
            }
            PageHelper.orderBy("create_time desc");
            baseMapper.selectList(getConditionWrapper(condition));
            PageInfo<ContractProcessRecord> pageInfo = new PageInfo<ContractProcessRecord>(page);
            List<ContractProcessRecordDTO> dataList = listEntityToDto(pageInfo.getList());
            BeanUtils.copyProperties(pageInfo, pageResult, "list");
            pageResult.setList(dataList);
        }
        return pageResult;
    }

    /**
     * 封装查询条件
     *
     * @param condition 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryWrapper<ContractProcessRecord> getConditionWrapper(ContractProcessRecordQuery condition) {
        LambdaQueryWrapper<ContractProcessRecord> queryWrapper = new LambdaQueryWrapper<ContractProcessRecord>();
        if(condition != null) {
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @param list entity集合
     * @return DTO集合
     */
    private List<ContractProcessRecordDTO> listEntityToDto(List<ContractProcessRecord> list) {
        return list.stream().map(entity -> {
            ContractProcessRecordDTO temp = new ContractProcessRecordDTO();
            BeanUtils.copyProperties(entity, temp);
            return temp;
        }).collect(Collectors.toList());
    }
}
