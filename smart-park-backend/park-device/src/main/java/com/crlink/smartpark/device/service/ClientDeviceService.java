package com.crlink.smartpark.device.service;

import com.crlink.smartpark.device.entity.ClientDevice;
import com.crlink.smartpark.device.model.query.ClientDeviceQuery;
import com.crlink.smartpark.device.model.ClientDeviceReqDTO;
import com.crlink.smartpark.device.dto.ClientDeviceDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 设备管理 服务类
 * </p>
 * */
public interface ClientDeviceService extends IService<ClientDevice> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ClientDevice, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateClientDevice(ClientDeviceReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ClientDeviceDTO getClientDeviceById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ClientDeviceDTO> listClientDevicePage(ClientDeviceQuery query);
}
