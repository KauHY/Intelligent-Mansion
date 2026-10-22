package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.dto.DeviceInfoReqDTO;
import com.crlink.smartpark.device.dto.DeviceSyncDTO;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.query.DeviceInfoQuery;
import com.crlink.smartpark.device.query.ScreenDeviceQuery;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 设备信息表 服务类
 * </p>
 * */
public interface DeviceInfoService extends IService<DeviceInfo> {

    <T> Map<String, T> id2Value(Set<String> ids, SFunction<DeviceInfo, T> function);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    DeviceInfoVO getDeviceInfoById(String id);

    DeviceInfoVO getByDeviceId(String deviceId);

    void updateScreenDeviceStatus();

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<DeviceInfoVO> listPage(DeviceInfoQuery condition);


    void saveOrUpdateDevice(DeviceInfoReqDTO dto);

    List<DictVO> listDropDownDevices(String parkOrgId, List<Integer> deviceTypes);

    DeviceInfoVO getByDeviceCode(String deviceCode);

    /**
     * 获取会议室屏屏幕截图url
     *
     * @param id
     * @return
     */
    String getMeetingScreenshot(String id);

    /**
     * 休眠会议室屏
     *
     * @param id
     * @return
     */
    Boolean sleepDevice(String id);

    Boolean wakeUpDevice(String id);

    /**
     * 会议屏设备分页列表
     *
     * @param query
     * @return
     */
    PageInfo<DeviceInfoVO> listScreenDevicePage(ScreenDeviceQuery query);

    Map<String, String> getIdByDeviceNames(Set<String> deviceNames, Integer deviceType, String parkOrgId);

    /**
     * 根据deviceId获取主键id
     *
     * @param deviceIds
     * @param deviceType
     * @return
     */
    Map<String, String> getIdByDeviceIds(Set<String> deviceIds, Integer deviceType);

    Map<String, Integer> getDeviceStatusByIds(Set<String> ids);

    boolean removeDeviceById(String id);

    Boolean fireDeviceSync();

    Map<String, Set<String>> findChannelNosByHouseIds(Set<String> houseIds);

    /**
     * 更新水表、电表设备的状态（定时任务使用）
     */
    void updateDeviceStatus();

}
