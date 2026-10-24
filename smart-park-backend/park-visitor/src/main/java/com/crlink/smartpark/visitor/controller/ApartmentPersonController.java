package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.dto.ApartmentPersonDTO;
import com.crlink.smartpark.visitor.model.dto.ApartmentPersonReqDTO;
import com.crlink.smartpark.visitor.model.dto.UpdateApartmentPersonReqDTO;
import com.crlink.smartpark.visitor.model.query.ApartmentPersonQuery;
import com.crlink.smartpark.visitor.model.vo.ApartmentPersonVO;
import com.crlink.smartpark.visitor.service.ApartmentPersonHouseRelationService;
import com.crlink.smartpark.visitor.service.ApartmentPersonService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.Set;

/**
 * park-visitor
 * 公寓人员管理
 * */
@Slf4j
@RestController
@RequestMapping("/apartmentPerson")
@Api(tags = "公寓人员管理")
@Validated
public class ApartmentPersonController {

    @Autowired
    private ApartmentPersonService apartmentPersonService;

    @Autowired
    private ApartmentPersonHouseRelationService personHouseRelationService;


    @ApiOperation(value = "公寓人员管理-新增", tags = "公寓人员管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ApartmentPersonReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(apartmentPersonService.saveOrUpdateApartmentPerson(reqDTO));
    }

    @ApiOperation(value = "公寓人员管理-修改", tags = "公寓人员管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ApartmentPersonReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(apartmentPersonService.saveOrUpdateApartmentPerson(reqDTO));
    }


    @ApiOperation(value = "公寓人员管理-删除", tags = "公寓人员管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(apartmentPersonService.removePersonById(id));
    }


    @ApiOperation(value = "公寓人员管理-根据ID查询", tags = "公寓人员管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<ApartmentPersonDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(apartmentPersonService.getApartmentPersonById(id));
    }


    @ApiOperation(value = "公寓人员管理-分页查询", tags = "公寓人员管理")
    @PostMapping("/listApartmentPersonPage")
    public ResponseResult<PageInfo<ApartmentPersonVO>> listApartmentPersonPage(@RequestBody @Valid ApartmentPersonQuery query) {
        return ResponseResult.success(apartmentPersonService.listApartmentPersonPage(query));
    }

    @ApiOperation(value = "公寓人员管理-启用/禁用", tags = "公寓人员管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@Valid @RequestBody UpdateApartmentPersonReqDTO statusReqDTO) {
        Boolean result = apartmentPersonService.updateStatus(statusReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 公寓人员管理-导出
     *
     * @return
     */
    @ApiOperation(value = "公寓人员管理-导出", tags = "公寓人员管理")
    @PostMapping("/export")
    public void download(@RequestBody ApartmentPersonQuery query) {
        apartmentPersonService.exportPerson(query);
    }

    @ApiOperation(value = "公寓人员管理-导入", tags = "公寓人员管理")
    @PostMapping("/import")
    public ResponseResult<FileImportErrorInfoDTO> importPerson(@RequestPart("file") MultipartFile file,
                                                               @RequestParam String parkOrgId,
                                                               @RequestParam String companyId) {
        FileImportErrorInfoDTO errorInfoDTO = apartmentPersonService.importPerson(file, parkOrgId, companyId);
        return ResponseResult.success(errorInfoDTO);
    }

    //@ApiOperation(value = "公寓人员管理-根据员工ID查询公寓设备ID集合", tags = "公寓人员管理")
    //@GetMapping("/findDeviceIdsByEmployeeId/{employeeId}")
    //public Set<String> findDeviceIdsByEmployeeId(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId) {
    //    return apartmentPersonService.findDeviceIdsByEmployeeId(employeeId);
    //}

    @ApiOperation(value = "公寓人员管理-根据员工ID查询公寓设备ID集合", tags = "公寓人员管理")
    @GetMapping("/findDeviceIdsByEmployeeId/{employeeId}")
    public ApartmentPersonDTO findDeviceIdsByEmployeeId(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId) {
        return apartmentPersonService.findByEmployeeId(employeeId);
    }

    @ApiOperation(value = "公寓人员管理-根据员工ID查询关联房间ID集合", tags = "公寓人员管理")
    @GetMapping("/findHouseIdsByEmployeeId/{employeeId}")
    public Set<String> findHouseIdsByEmployeeId(@PathVariable("employeeId") @NotEmpty(message = "employeeId不能为空") String employeeId) {
        return apartmentPersonService.findHouseIdsByEmployeeId(employeeId);
    }


    /**
     * 根据房屋id获取关联的所有员工id
     *
     * @param houseIds
     */
    @ApiOperation(value = "公寓人员管理-根据房屋id获取关联的所有员工id", tags = "公寓人员管理")
    @GetMapping("/houseRelation/getEmployeeIdsByHouseId")
    Map<String, Set<String>> getEmployeeIdsByHouseId(@RequestParam("houseIds") Set<String> houseIds) {
        return personHouseRelationService.houseId2EmployeeIds(houseIds);
    }


    @ApiOperation(value = "设备是否存在", tags = "公寓人员管理")
    @GetMapping("/existsDevice")
    public boolean existsDevice(@RequestParam(value = "deviceId") String deviceId) {
        return apartmentPersonService.existsDevice(deviceId);
    }

}

