package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.annotations.CurrentUserParkOrgIds;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.DeviceInfoReqDTO;
import com.crlink.smartpark.device.dto.DeviceSyncDTO;
import com.crlink.smartpark.device.model.DropDownDeviceDTO;
import com.crlink.smartpark.device.query.DeviceInfoQuery;
import com.crlink.smartpark.device.query.ScreenDeviceQuery;
import com.crlink.smartpark.device.service.DeviceInfoService;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 园区运营平台-设备管理
 * */
@Slf4j
@RestController
@Api(tags = "设备管理")
@RequestMapping("/device-info")
@Validated
public class DeviceInfoController {

    /**
     * 设备信息表
     */
    @Autowired
    private DeviceInfoService deviceInfoService;

    /**
     * 【设备信息表】新增
     *
     * @param deviceInfoReqDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "设备信息-新增", tags = "设备管理")
    @PostMapping("/create")
    public ResponseResult create(@RequestBody @Validated DeviceInfoReqDTO deviceInfoReqDTO) {
        deviceInfoReqDTO.setId(null);
        deviceInfoService.saveOrUpdateDevice(deviceInfoReqDTO);
        return ResponseResult.success();
    }

    /**
     * 【设备信息表】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "设备信息-删除", tags = "设备管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        boolean b = deviceInfoService.removeDeviceById(id);
        return ResponseResult.success(b);
    }

    /**
     * 【设备信息表】根据ID查询
     *
     * @param id 主键
     * @return 设备信息表实体
     */
    @ApiOperation(value = "设备信息-根据ID查询", tags = "设备管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<DeviceInfoVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(deviceInfoService.getDeviceInfoById(id));
    }

    /**
     * 【设备信息表】修改
     *
     * @param deviceInfoUpdateDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "设备信息-修改", tags = "设备管理")
    @PostMapping("/modify")
    public ResponseResult modify(@RequestBody @Validated DeviceInfoReqDTO deviceInfoUpdateDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(deviceInfoUpdateDTO.getId()), "10007");
        deviceInfoService.saveOrUpdateDevice(deviceInfoUpdateDTO);
        return ResponseResult.success();
    }

    /**
     * 【设备信息表】下拉列表
     *
     * @return 分页数据
     */
    @ApiOperation(value = "设备信息-下拉列表1", tags = "设备管理")
    @GetMapping("/listDropDownDevices")
    public ResponseResult<List<DictVO>> listDropDownDevices(
            @RequestParam(value = "parkOrgId") String parkOrgId,
            @RequestParam(value = "deviceType") Integer deviceType) {
        return ResponseResult.success(deviceInfoService.listDropDownDevices(parkOrgId, Lists.newArrayList(deviceType)));
    }

    @ApiOperation(value = "设备信息-下拉列表2", tags = "设备管理")
    @PostMapping("/listDropDownDevices")
    public ResponseResult<List<DictVO>> listDropDownDevices(@RequestBody DropDownDeviceDTO param) {
        return ResponseResult.success(deviceInfoService.listDropDownDevices(param.getParkOrgId(), param.getDeviceTypes()));
    }

    /**
     * 【设备信息表】分页查询
     *
     * @param deviceInfoQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "设备信息-分页查询", tags = "设备管理")
    @PostMapping("/listPage")
    @CurrentUserParkOrgIds
    public ResponseResult<PageInfo<DeviceInfoVO>> findListByPage(@RequestBody DeviceInfoQuery deviceInfoQuery) {
        PageInfo<DeviceInfoVO> result = deviceInfoService.listPage(deviceInfoQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "设备信息-同步设备信息", tags = "设备管理")
    @PostMapping("/sync")
    public ResponseResult sync(@RequestBody DeviceSyncDTO dto) {
        return ResponseResult.success();
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>会议室屏幕设备<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @ApiOperation(value = "会议室信息屏-查看会议室屏屏幕", tags = "会议室管理-C端小程序")
    @GetMapping("/getMeetingScreenshot")
    public ResponseResult<String> getMeetingScreenshot(@RequestParam("screenDeviceId") String id) {
        String result = deviceInfoService.getMeetingScreenshot(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "会议室信息屏-休眠会议室屏", tags = "会议室管理-C端小程序")
    @PostMapping("/sleepDevice/{screenDeviceId}")
    public ResponseResult<Boolean> sleepDevice(@PathVariable("screenDeviceId") String id) {
        Boolean result = deviceInfoService.sleepDevice(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "会议室信息屏-唤醒会议室屏", tags = "会议室管理-C端小程序")
    @PostMapping("/wakeUpDevice/{screenDeviceId}")
    public ResponseResult<Boolean> wakeUpDevice(@PathVariable("screenDeviceId") String id) {
        Boolean result = deviceInfoService.wakeUpDevice(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "会议室信息屏-分页查询", tags = "设备管理")
    @PostMapping("/listScreenDevicePage")
    @CurrentUserParkOrgIds
    public ResponseResult<PageInfo<DeviceInfoVO>> listScreenDevicePage(@RequestBody ScreenDeviceQuery query) {
        PageInfo<DeviceInfoVO> result = deviceInfoService.listScreenDevicePage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "消防设备数据同步", tags = "设备管理")
    @PostMapping("/fireDeviceSync")
    @CurrentUserParkOrgIds
    public ResponseResult<Boolean> fireDeviceSync() {
        Boolean result = deviceInfoService.fireDeviceSync();
        return ResponseResult.success(result);
    }
}

