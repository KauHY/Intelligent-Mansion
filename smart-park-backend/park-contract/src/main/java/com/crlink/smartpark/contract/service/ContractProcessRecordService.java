package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.entity.ContractProcessRecord;
import com.crlink.smartpark.contract.model.query.ContractProcessRecordQuery;
import com.crlink.smartpark.contract.model.request.ContractProcessRecordReqDTO;
import com.crlink.smartpark.contract.dto.ContractProcessRecordDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 合同操作记录 服务类
 * </p>
 * */
public interface ContractProcessRecordService extends IService<ContractProcessRecord> {


    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean saveOrUpdateContractProcessRecord(ContractProcessRecordReqDTO dto);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ContractProcessRecordDTO getContractProcessRecordById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<ContractProcessRecordDTO> listPage(ContractProcessRecordQuery condition);
}
