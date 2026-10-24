package com.crlink.smartpark.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.OperateLogDTO;
import com.crlink.smartpark.common.entity.OperateLog;
import com.crlink.smartpark.common.model.query.OperateLogQuery;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 操作日志 服务类
 * </p>
 * */
public interface OperateLogService extends IService<OperateLog> {

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOperateLog(OperateLogDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    OperateLogDTO getOperateLogById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<OperateLogDTO> listOperateLogPage(OperateLogQuery query);
}
