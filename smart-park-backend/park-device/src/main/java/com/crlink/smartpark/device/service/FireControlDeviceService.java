package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.dto.FireControlDeviceDTO;
import com.crlink.smartpark.device.entity.FireControlDevice;
import com.crlink.smartpark.device.model.FireControlDeviceReqDTO;
import com.crlink.smartpark.device.model.query.FireControlDeviceQuery;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 消防设备 服务类
 * </p>
 * */
public interface FireControlDeviceService extends IService<FireControlDevice> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<FireControlDevice, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateFireControlDevice(FireControlDeviceReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    FireControlDeviceDTO getFireControlDeviceById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<FireControlDeviceDTO> listFireControlDevicePage(FireControlDeviceQuery query);

    void exportFireControlDevice(FireControlDeviceQuery query);
}
