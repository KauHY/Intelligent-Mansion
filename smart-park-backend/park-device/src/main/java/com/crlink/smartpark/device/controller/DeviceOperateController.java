package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.device.dto.DeviceCardDTO;
import com.crlink.smartpark.device.dto.PersonDeviceAddAndAuthorityDTO;
import com.crlink.smartpark.device.dto.PersonDeviceAuthorityDTO;
import com.crlink.smartpark.device.dto.PersonDeviceDTO;
import com.crlink.smartpark.device.service.CardService;
import com.crlink.smartpark.visitor.dto.ParkCarDTO;
import com.crlink.smartpark.visitor.dto.ParkingLotSaveDTO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/** * @desc
 */
@Slf4j
@Api(tags = "设备操作管理")
@RestController
@RequestMapping("/device-operate")
public class DeviceOperateController {

    @Resource
    private CardService cardService;

    @GetMapping("/generatePersonId")
    public Long generatePersonId() throws Exception {
        return System.currentTimeMillis();
    }

    @GetMapping("/generatePersonIdBatch")
    public List<Long> generatePersonIdBatch(Integer count) throws Exception {
        List<Long> longList = Lists.newArrayList();
        Long initLong = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            longList.add(initLong + i);
        }
        return longList;
    }

    @GetMapping("/generateCardId")
    public Long generateCardId() throws Exception {
        return System.currentTimeMillis();
    }

    @GetMapping("/generateCardIdBatch")
    public List<Long> generateCardIdBatch(Integer count) throws Exception {
        List<Long> longList = Lists.newArrayList();
        Long initLong = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            longList.add(initLong + i);
        }
        return longList;
    }

    @ApiOperation(value = "新增人员", tags = "设备操作管理")
    @PostMapping("/addPerson")
    public String addPerson(@RequestBody PersonDeviceDTO dto) throws Exception {
        return "";
    }

    @ApiOperation(value = "下发人员到设备", tags = "设备操作管理")
    @PostMapping("/pushPersons2Devices")
    public Boolean pushPersons2Devices(@RequestBody PersonDeviceAuthorityDTO dto) throws Exception {
        log.info("下发人员到设备pushPersons2Devices(), {}", dto.getCardNoDevicesPushMap());
        return true;
    }

    @ApiOperation(value = "新增人员和卡片", tags = "设备操作管理")
    @PostMapping("/addPersonAndCard")
    public String addPersonAndCard(@RequestBody PersonDeviceDTO dto) throws Exception {
        log.info("新增人员和卡片:personId={},personName={},mobile={},cardNo={}", dto.getDevicePersonId(),
                dto.getPersonName(), dto.getMobilePhone(), dto.getCardNo());
        return "";
    }

    @ApiOperation(value = "上传照片", tags = "设备操作管理")
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public String uploadImg(@RequestPart("file") MultipartFile file) throws Exception {
        return "";
    }

    @ApiOperation(value = "新增人员和卡片", tags = "设备操作管理")
    @RequestMapping(value = "/addPersonAndCardNew", method = RequestMethod.POST)
    public String addPersonAndCardNew(@RequestBody PersonDeviceDTO dto) throws Exception {
        return "";
    }

    @ApiOperation(value = "新增人员并下发", tags = "设备操作管理")
    @PostMapping("/addPersonAndPush")
    public Boolean addPersonAndPush(@RequestBody PersonDeviceAddAndAuthorityDTO dto) throws Exception, InterruptedException {
        return true;
    }

    @ApiOperation(value = "编辑人员并下发", tags = "设备操作管理")
    @PostMapping("/updatePersonAndPush")
    public Boolean updatePersonAndPush(@RequestBody PersonDeviceAddAndAuthorityDTO dto) throws Exception {
        return true;
    }

    @ApiOperation(value = "删除人员", tags = "设备操作管理")
    @DeleteMapping("/delPerson/{personId}")
    public String delPerson(@PathVariable("personId") Long personId) throws Exception {
        return "";
    }

    @ApiOperation(value = "批量删除人员", tags = "设备操作管理")
    @DeleteMapping("/delPersonBatch")
    public String delPersonBatch(@RequestParam("personIds") List<Long> personIds) throws Exception {
        return "";
    }

    @ApiOperation(value = "修改人员", tags = "设备操作管理")
    @PutMapping("/updatePerson")
    public String updatePerson(@RequestBody PersonDeviceDTO dto) throws Exception {
        return "";
    }

    @ApiOperation(value = "查询人员是否存在", tags = "设备操作管理")
    @GetMapping("/personExistById/{personId}")
    public Boolean personExistById(@PathVariable("personId") Long personId) throws Exception {
        return false;
    }

    @ApiOperation(value = "查询人员是否存在", tags = "设备操作管理")
    @GetMapping("/personExist/{telephone}")
    public Boolean personExist(@PathVariable("telephone") String telephone) throws Exception {
        AssertUtil.requireNotNullOrParamExp(telephone);
        return false;
    }

    @ApiOperation(value = "新增卡片", tags = "设备操作管理")
    @PostMapping("/addCard")
    public String addCard(@RequestBody DeviceCardDTO cardDTO) throws Exception {
        return "";
    }

    @ApiOperation(value = "修改卡片", tags = "设备操作管理")
    @PutMapping("/updateCard")
    public String updateCard(@RequestBody DeviceCardDTO cardDTO) throws Exception {
        return "";
    }

    @ApiOperation(value = "删除卡片", tags = "设备操作管理")
    @DeleteMapping("/delCard/{cardNo}")
    public String delCard(@PathVariable("cardNo") String cardNo) throws Exception {
        return "";
    }

    @ApiOperation(value = "查询卡片是否存在", tags = "设备操作管理")
    @GetMapping("/cardExist/{cardNo}")
    public Boolean cardExist(@PathVariable("cardNo") String cardNo) throws Exception {
        return false;
    }

    @ApiOperation(value = "人员门禁权限清除", tags = "设备操作管理")
    @PostMapping("/personDoorAccessRemove")
    public Boolean personDoorAccessRemove(@RequestParam("cardNos") Collection<String> cardNos) throws Exception {
        return true;
    }

    @ApiOperation(value = "删除人员门禁权限", tags = "设备操作管理")
    @PostMapping("/doorAccessPersonsRemove")
    public Boolean doorAccessPersonsRemove(@RequestBody PersonDeviceAuthorityDTO dto) throws Exception {
        log.info("删除人员门禁权限doorAccessPersonsRemove(), {}", dto.getDeviceCardNosPushMap());
        return true;
    }

    @ApiOperation(value = "修改人员状态", tags = "设备操作管理")
    @PutMapping("/updatePersonStatus")
    public String updatePersonStatus(@RequestParam("devicePersonId") Long devicePersonId, @RequestParam("status") Integer status) throws Exception {
        return "";
    }

    @ApiOperation(value = "启用多个人员", tags = "设备操作管理")
    @PutMapping("/enablePerson")
    public String enablePerson(@RequestParam("devicePersonIds") List<Long> devicePersonIds) throws Exception {
        log.info("enablePerson devicePersonIds:{}", devicePersonIds);
        return "";
    }

    @ApiOperation(value = "禁用多个人员", tags = "设备操作管理")
    @PutMapping("/unablePerson")
    public String unablePerson(@RequestParam("devicePersonIds") List<Long> devicePersonIds) throws Exception {
        log.info("unablePerson devicePersonIds:{}", devicePersonIds);
        return "";
    }

    @ApiOperation(value = "刷新员工的门禁权限", tags = "设备操作管理")
    @PostMapping("/refreshEmployeeDoorAccessAuthorities/{employeeId}")
    public Boolean refreshEmployeeDoorAccessAuthorities(@PathVariable("employeeId") String employeeId) throws Exception {
        return true;
    }

    @ApiOperation(value = "刷新人员的智能锁权限", tags = "设备操作管理")
    @PostMapping("/refreshSmartLockAuth")
    public String refreshSmartLockAuth(@RequestParam("employeeId") String employeeId, @RequestParam("isDisableOrDel") Boolean isDisableOrDel) throws Exception {
        return "";
    }

    @ApiOperation(value = "同步车场信息", tags = "设备操作管理")
    @GetMapping("/getParkingLotList")
    public List<ParkingLotSaveDTO> getParkingLotList() throws Exception {
        return Lists.newArrayList();
    }

    @ApiOperation(value = "更新车场人员", tags = "设备操作管理")
    @PostMapping("/updateParkPerson")
    public String updateParkPerson(@RequestBody ParkCarDTO carDTO) throws Exception {
        log.info("更新车场人员:{}", carDTO);
        return "";
    }

    @ApiOperation(value = "新增车辆", tags = "设备操作管理")
    @PostMapping("/addCar")
    public String addCar(@RequestBody ParkCarDTO carDTO) throws Exception {
        log.info("新增车辆:{}", carDTO);
        return "" + System.currentTimeMillis();
    }

    @ApiOperation(value = "删除车辆", tags = "设备操作管理")
    @DeleteMapping("/deleteBatchCar")
    String deleteBatchCar(@RequestBody Set<String> ids) throws Exception {
        log.info("删除车辆:{}", ids);
        return "";
    }

    @ApiOperation(value = "新增预约车辆", tags = "设备操作管理")
    @PostMapping("/addReservedCar")
    public String addReservedCar(@RequestBody ParkCarDTO carDTO) throws Exception {
        log.info("新增预约车辆:{}", carDTO);
        return "" + System.currentTimeMillis();
    }

    @ApiOperation(value = "修改预约车辆", tags = "设备操作管理")
    @PutMapping("/updateReservedCar")
    public String updateReservedCar(@RequestBody ParkCarDTO carDTO) throws Exception {
        log.info("修改预约车辆:{}", carDTO);
        return "";
    }

    @ApiOperation(value = "删除预约车辆", tags = "设备操作管理")
    @DeleteMapping("/deleteBatchReservedCar")
    String deleteBatchReservedCar(@RequestBody Set<String> ids) throws Exception {
        log.info("删除预约车辆:{}", ids);
        return "";
    }

}
