package com.crlink.smartpark.device.feign;

import com.crlink.smartpark.device.dto.DeviceCardDTO;
import com.crlink.smartpark.device.dto.PersonDeviceAddAndAuthorityDTO;
import com.crlink.smartpark.device.dto.PersonDeviceAuthorityDTO;
import com.crlink.smartpark.device.dto.PersonDeviceDTO;
import com.crlink.smartpark.visitor.dto.ParkCarDTO;
import com.crlink.smartpark.visitor.dto.ParkingLotSaveDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 设备操作 服务类
 * </p>
 * */
@FeignClient(name = "park-device", path = "/smartPark/park-device/device-operate", contextId = "DeviceOperateProvider")
public interface DeviceOperateProvider {

    @GetMapping("/generatePersonId")
    Long generatePersonId();

    @GetMapping("/generatePersonIdBatch")
    List<Long> generatePersonIdBatch(@RequestParam(value = "count") Integer count);

    @GetMapping("/generateCardId")
    Long generateCardId();

    @PostMapping(value = "/uploadImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadImg(@RequestPart("file") MultipartFile file);

    @GetMapping("/generateCardIdBatch")
    List<Long> generateCardIdBatch(@RequestParam(value = "count") Integer count);

    @ApiOperation(value = "新增人员", tags = "设备操作管理")
    @PostMapping("/addPerson")
    String addPerson(@RequestBody PersonDeviceDTO dto);

    @ApiOperation(value = "修改人员", tags = "设备操作管理")
    @PutMapping("/updatePerson")
    String updatePerson(@RequestBody PersonDeviceDTO dto);

    @ApiOperation(value = "下发人员到设备", tags = "设备操作管理")
    @PostMapping("/pushPersons2Devices")
    Boolean pushPersons2Devices(@RequestBody PersonDeviceAuthorityDTO dto);

    @ApiOperation(value = "新增人员和卡片", tags = "设备操作管理")
    @PostMapping("/addPersonAndCard")
    String addPersonAndCard(@RequestBody PersonDeviceDTO dto);

    @ApiOperation(value = "新增人员和卡片新", tags = "设备操作管理")
    @PostMapping(value = "/addPersonAndCardNew")
    String addPersonAndCardNew(@RequestBody PersonDeviceDTO dto);

    @ApiOperation(value = "新增人员并下发", tags = "设备操作管理")
    @PostMapping("/addPersonAndPush")
    Boolean addPersonAndPush(@RequestBody PersonDeviceAddAndAuthorityDTO dto);

    @ApiOperation(value = "编辑人员并下发", tags = "设备操作管理")
    @PostMapping("/updatePersonAndPush")
    Boolean updatePersonAndPush(@RequestBody PersonDeviceAddAndAuthorityDTO dto);

    @ApiOperation(value = "删除人员", tags = "设备操作管理")
    @DeleteMapping("/delPerson/{personId}")
    String delPerson(@PathVariable("personId") Long personId);

    @ApiOperation(value = "批量删除人员", tags = "设备操作管理")
    @DeleteMapping("/delPersonBatch")
    public String delPersonBatch(@RequestParam("personIds") List<Long> personIds);

    @ApiOperation(value = "查询人员是否存在", tags = "设备操作管理")
    @GetMapping("/personExistById/{personId}")
    Boolean personExistById(@PathVariable("personId") Long personId);

    @ApiOperation(value = "查询人员是否存在", tags = "设备操作管理")
    @GetMapping("/personExist/{telephone}")
    Boolean personExist(@PathVariable("telephone") String telephone);

    @ApiOperation(value = "新增卡片", tags = "设备操作管理")
    @PostMapping("/addCard")
    String addCard(@RequestBody DeviceCardDTO cardDTO);

    @ApiOperation(value = "修改卡片", tags = "设备操作管理")
    @PutMapping("/updateCard")
    String updateCard(@RequestBody DeviceCardDTO cardDTO);

    @ApiOperation(value = "删除卡片", tags = "设备操作管理")
    @DeleteMapping("/delCard/{cardNo}")
    String delCard(@PathVariable("cardNo") String cardNo);

    @ApiOperation(value = "查询卡片是否存在", tags = "设备操作管理")
    @GetMapping("/cardExist/{cardNo}")
    Boolean cardExist(@PathVariable("cardNo") String cardNo);

    @ApiOperation(value = "人员门禁权限清除", tags = "设备操作管理")
    @PostMapping("/personDoorAccessRemove")
    Boolean personDoorAccessRemove(@RequestParam("cardNos") Collection<String> cardNos);

    @ApiOperation(value = "删除人员门禁权限", tags = "设备操作管理")
    @PostMapping("/doorAccessPersonsRemove")
    Boolean doorAccessPersonsRemove(@RequestBody PersonDeviceAuthorityDTO dto);

    @ApiOperation(value = "修改人员状态", tags = "设备操作管理")
    @PutMapping("/updatePersonStatus")
    String updatePersonStatus(@RequestParam("devicePersonId") Long devicePersonId, @RequestParam("status") Integer status);

    @ApiOperation(value = "启用多个人员", tags = "设备操作管理")
    @PutMapping("/enablePerson")
    String enablePerson(@RequestParam("devicePersonIds") List<Long> devicePersonIds);

    @ApiOperation(value = "禁用多个人员", tags = "设备操作管理")
    @PutMapping("/unablePerson")
    String unablePerson(@RequestParam("devicePersonIds") List<Long> devicePersonIds);

    @ApiOperation(value = "刷新员工的门禁权限", tags = "设备操作管理")
    @PostMapping("/refreshEmployeeDoorAccessAuthorities/{employeeId}")
    Boolean refreshEmployeeDoorAccessAuthorities(@PathVariable("employeeId") String employeeId);

    @ApiOperation(value = "刷新人员的智能锁权限", tags = "设备操作管理")
    @PostMapping("/refreshSmartLockAuth")
    String refreshSmartLockAuth(@RequestParam("employeeId") String employeeId, @RequestParam("isDisableOrDel") Boolean isDisableOrDel);

    @ApiOperation(value = "同步车场信息", tags = "设备操作管理")
    @GetMapping("/getParkingLotList")
    List<ParkingLotSaveDTO> getParkingLotList();

    @ApiOperation(value = "更新车场人员", tags = "设备操作管理")
    @PostMapping("/updateParkPerson")
    String updateParkPerson(@RequestBody ParkCarDTO carDTO);

    @ApiOperation(value = "新增车辆", tags = "设备操作管理")
    @PostMapping("/addCar")
    String addCar(@RequestBody ParkCarDTO carDTO);

    @ApiOperation(value = "删除车辆", tags = "设备操作管理")
    @DeleteMapping("/deleteBatchCar")
    String deleteBatchCar(@RequestBody Set<String> ids);

    @ApiOperation(value = "新增预约车辆", tags = "设备操作管理")
    @PostMapping("/addReservedCar")
    String addReservedCar(@RequestBody ParkCarDTO carDTO);

    @ApiOperation(value = "修改预约车辆", tags = "设备操作管理")
    @PutMapping("/updateReservedCar")
    String updateReservedCar(@RequestBody ParkCarDTO carDTO);

    @ApiOperation(value = "删除预约车辆", tags = "设备操作管理")
    @DeleteMapping("/deleteBatchReservedCar")
    String deleteBatchReservedCar(@RequestBody Set<String> ids);
}
