package com.crlink.smartpark.device.service;

import com.crlink.smartpark.device.entity.ClientMqttLog;
import com.crlink.smartpark.device.model.query.ClientMqttLogQuery;
import com.crlink.smartpark.device.model.ClientMqttLogReqDTO;
import com.crlink.smartpark.device.dto.ClientMqttLogDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * mqtt日志记录 服务类
 * </p>
 * */
public interface ClientMqttLogService extends IService<ClientMqttLog> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ClientMqttLog, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateClientMqttLog(ClientMqttLogReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ClientMqttLogDTO getClientMqttLogById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ClientMqttLogDTO> listClientMqttLogPage(ClientMqttLogQuery query);

    /**
     * 保存mqtt日志
     *
     * @param topic
     * @param msg
     * @param type
     */
    void saveMqttLog(String topic, String msg, Integer type);
}
