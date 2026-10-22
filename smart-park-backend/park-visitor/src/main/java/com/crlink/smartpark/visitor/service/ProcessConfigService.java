package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.dto.ProcessConfigDTO;
import com.crlink.smartpark.visitor.entity.ProcessConfig;
import com.crlink.smartpark.visitor.model.dto.ProcessConfigReqDTO;
import com.crlink.smartpark.visitor.model.dto.ProcessConfigStatusReqDTO;
import com.crlink.smartpark.visitor.model.query.ProcessConfigQuery;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 访客流程配置 服务类
 * </p>
 * */
public interface ProcessConfigService extends IService<ProcessConfig> {

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProcessConfig(ProcessConfigReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ProcessConfigDTO getProcessConfigById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<ProcessConfigDTO> listProcessConfigPage(ProcessConfigQuery query);

    /**
     * 访客流程配置-启用/禁用
     *
     * @param dto
     * @return
     */
    Boolean modifyStatus(ProcessConfigStatusReqDTO dto);

}
