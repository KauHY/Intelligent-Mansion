package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.controller.request.EmployeeTreeSelectQuery;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.employee.*;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.query.EmployeeQuery;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeManageService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.system.vo.TreeDictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 员工管理
 *
 */
@Slf4j
@RestController
@Api(tags = "园区运营平台-员工管理")
@RequestMapping("/parkManagement/employee")
public class EmployeeController {

    @Autowired
    private EmployeeManageService employeeManageService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工新增
     */
    @ApiOperation(value = "员工新增", tags = "员工管理")
    @PostMapping("/create")
    public ResponseResult create(@RequestBody EmployeeReqDTO reqDTO) {
        reqDTO.setId(null);
        employeeManageService.saveOrUpdateEmployee(reqDTO);
        return ResponseResult.success();
    }

    /**
     * 修改员工信息
     */
    @ApiOperation(value = "修改员工信息", tags = "员工管理")
    @PostMapping("/modify")
    public ResponseResult modify(@RequestBody EmployeeReqDTO employeeDTO) {
        AssertUtil.requireNotNull(employeeDTO.getId(), "10007");
        employeeManageService.saveOrUpdateEmployee(employeeDTO);
        return ResponseResult.success();
    }

    /**
     * 修改员工信息
     */
    @ApiOperation(value = "修改员工主卡号", tags = "内部接口")
    @PostMapping("/updateEmployeeCardNo")
    public Boolean updateEmployeeCardNo(@RequestParam(value = "id") String id,
                                        @RequestParam(value = "cardNo") String cardNo) {
        AssertUtil.requireNotNull(id, "10007");
        return employeeManageService.updateEmployeeCardNo(id, cardNo);
    }

    /**
     * 删除员工
     */
    @ApiOperation(value = "删除员工", tags = "员工管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        employeeManageService.removeEmployeeById(id);
        return ResponseResult.success();
    }

    /**
     * 更改员工状态
     *
     * @return
     */
    @ApiOperation(value = "更改员工状态", tags = "员工管理")
    @PostMapping("/updateStatus")
    public ResponseResult updateStatus(@Valid @RequestBody UpdateStatusReqDTO statusReqDTO) {
        AssertUtil.requireNotNull(statusReqDTO.getId(), "10007");
        employeeManageService.updateEmployeeStatus(statusReqDTO);
        return ResponseResult.success();
    }

    /**
     * 员工-分页查询
     */
    @ApiOperation(value = "员工-分页查询", tags = "入住企业员工信息表")
    @PostMapping("/listEmployeePage")
    public ResponseResult<PageInfo<EmployeeDTO>> listEmployeePage(@RequestBody EmployeeQuery employeeQuery) {
        PageInfo<EmployeeDTO> result = employeeManageService.listEmployeePage(employeeQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "员工-下拉列表", tags = "入住企业员工信息表")
    @GetMapping("/listEmployeeDict")
    public ResponseResult<List<DictVO>> listEmployeeDict(@RequestParam(value = "companyId", required = true) @NotNull String companyId,
                                                         @RequestParam(value = "name", required = false) String name) {
        List<DictVO> result = employeeManageService.listEmployeeDict(Sets.newHashSet(companyId), name);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "物业类型公司员工-下拉列表", tags = "入住企业员工信息表")
    @GetMapping("/listSpecialEmployeeDict")
    public ResponseResult<List<DictVO>> listSpecialEmployeeDict(@RequestParam("orgId") String orgId,
                                                                @RequestParam(value = "name", required = false) String name) {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        List<DictVO> result = employeeManageService.listSpecialEmployeeDict(orgId, name, userId);
        return ResponseResult.success(result);
    }

    /**
     * 员工-导出
     *
     * @return
     */
    @ApiOperation(value = "员工-导出", tags = "入住企业员工信息表")
    @PostMapping("/download")
    public void download(@RequestBody EmployeeQuery employeeQuery) {
        Boolean result = employeeManageService.download(employeeQuery);
    }

    @ApiOperation(value = "员工-导入", tags = "员工")
    @PostMapping("/importEmployees")
    public ResponseResult<FileImportErrorInfoDTO> importEmployees(@RequestPart("file") MultipartFile file, @RequestParam String parkOrgId,
                                                                  @RequestParam String companyId) {
        FileImportErrorInfoDTO errorInfoDTO = employeeManageService.importEmployees(file, parkOrgId, companyId);
        return ResponseResult.success(errorInfoDTO);
    }

    @ApiOperation(value = "员工-上传压缩包", tags = "入住企业员工信息表")
    @PostMapping("/importEmployeesZip")
    public ResponseResult<String> importEmployeesZip(@RequestPart("file") MultipartFile file) {
        return ResponseResult.success(employeeManageService.handleEmployeeZip(file));
    }

    @ApiOperation(value = "员工-导入2", tags = "入住企业员工信息表")
    @PostMapping("/importEmployeesNew")
    public ResponseResult<FileImportErrorInfoDTO> importEmployeesNew(@RequestBody ImportEmployeeDTO dto) {
        FileImportErrorInfoDTO errorInfoDTO = employeeManageService.importEmployeesNew(dto);
        return ResponseResult.success(errorInfoDTO);
    }

    @ApiOperation(value = "处理人-下拉列表", tags = "入住企业员工信息表")
    @GetMapping("/listHandlerDict")
    public ResponseResult<List<DictVO>> listHandlerDict(@RequestParam("companyId") @NotNull String companyId,
                                                        @RequestParam("orderType") @NotNull int orderType,
                                                        @RequestParam(value = "name", required = false) String name) {
        List<DictVO> result = employeeManageService.listHandlerDict(companyId, orderType, name);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "查询员工，穿梭组件", tags = "查询员工，穿梭组件")
    @GetMapping("/selectEmployees")
    public ResponseResult<List<EmployeeTreeVO>> selectEmployees(String parkOrgId,
                                                                @RequestParam(required = false) String companyId,
                                                                @RequestParam(required = false) Integer companyType,
                                                                @RequestParam(required = false) String employeeName,
                                                                @RequestParam(required = false) Integer[] jobCodes) {
        List<EmployeeTreeVO> result = employeeManageService.queryEmployeeDicts(parkOrgId, companyId, companyType, employeeName, jobCodes);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "查询员工,下拉框", tags = "查询员工")
    @GetMapping("/selectEmployeesList")
    public ResponseResult<List<SimpleNode>> selectEmployeesByCondition(@RequestParam(required = false) String orgId,
                                                                       @RequestParam(required = false) String parkOrgId,
                                                                       @RequestParam(required = false) String companyId,
                                                                       @RequestParam(required = false) Integer companyType,
                                                                       @RequestParam(required = false) String employeeName,
                                                                       @RequestParam(required = false) Integer[] jobCodes) {
        List<SimpleNode> result = employeeManageService.selectEmployeesByCondition(orgId, parkOrgId, companyId, companyType, employeeName, jobCodes);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "查询员工,下拉框", tags = "")
    @GetMapping("/queryEmployeesList")
    public List<SimpleNode> queryEmployeesList(@RequestParam(required = false) String orgId,
                                               @RequestParam(required = false) String parkOrgId,
                                               @RequestParam(required = false) String companyId,
                                               @RequestParam(required = false) Integer companyType,
                                               @RequestParam(required = false) String employeeName,
                                               @RequestParam(required = false) Integer[] jobCodes) {
        List<SimpleNode> result = employeeManageService.selectEmployeesByCondition(orgId, parkOrgId, companyId, companyType, employeeName, jobCodes);
        return result;
    }


    @ApiOperation(value = "选人组件，选择园区或公司或标签", tags = "查询员工")
    @GetMapping("/selectTagTree")
    public ResponseResult<Set<TreeDictVO>> selectEmployeesByConditions(@RequestParam(required = false) String orgId,
                                                                       @RequestParam(required = false) Set<String> parkOrgIds,
                                                                       @RequestParam(required = false) Set<String> companyIds,
                                                                       @RequestParam(required = false) Integer companyType) {
        Set<TreeDictVO> result = employeeManageService.selectCompanyOrTagByConditions(orgId, parkOrgIds, companyIds, companyType);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "选人组件，根据园区id或公司Id或标签Id", tags = "查询员工")
    @PostMapping("/selectEmployeesByIdAndType")
    public ResponseResult<PageInfo<DictVO>> selectEmployeesByIdAndType(@RequestBody @Valid EmployeeTreeSelectQuery query) {
        PageInfo<DictVO> result = employeeManageService.selectEmployeesByIdAndType(query);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "员工切换", tags = "查询员工")
    @GetMapping("/selectListEmployeeInfos")
    public ResponseResult<List<EmployeeInfoVO>> listEmployeeInfos(@RequestParam String mobile) {
        List<EmployeeInfoVO> result = employeeManageService.listEmployeeInfosByMobile(mobile);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "根据员工名like查询物业企业员工", tags = "内部接口")
    @GetMapping("/listSpecialEmployeeByLikeName")
    public Map<String, String> getEmployeeIdsByNameLike(@RequestParam(value = "orgId", required = false) String orgId, @RequestParam("name") String name) {
        Map<String, String> employeeId2NameMap = employeeService.listSpecialEmployeeByLikeName(orgId, name);
        return employeeId2NameMap;
    }

    @ApiOperation(value = "查询员工信息", tags = "内部接口")
    @PostMapping("/queryEmployeeColumns")
    public Map<String, Map<String, Object>> queryEmployeeColumns(@RequestBody EmployeeQueryDTO queryDTO) {
        Map<String, Map<String, Object>> result = employeeService.queryEmployeeColumns(queryDTO);
        return result;
    }

    @ApiOperation(value = "查询员工信息2", tags = "内部接口")
    @PostMapping("/queryEmployeeByCondition")
    public Map<String, EmployeeDTO> queryEmployeeByCondition(@RequestBody EmployeeQueryDTO queryDTO) {
        Map<String, EmployeeDTO> result = employeeService.queryEmployeeByCondition(queryDTO, SystemUtil.getInstance().getCurrentHeadOrgId());
        return result;
    }

    @ApiOperation(value = "投诉、报修详情中查询员工信息", tags = "内部接口")
    @PostMapping("/orderRecordQueryEmployeeColumns")
    public Map<String, EmployeeDTO> orderRecordQueryEmployeeColumns(@RequestBody Collection<String> employeeIds) {
        Map<String, EmployeeDTO> result = employeeService.orderRecordQueryEmployeeColumns(employeeIds);
        return result;
    }


    @ApiOperation(value = "获取员工岗位", tags = "内部接口")
    @PostMapping("/getJobCodeByEmployeeId")

    public Map<String, Set<Integer>> getJobCodeByEmployeeIds(@RequestBody Collection<String> employeeIds) {
        Map<String, Set<Integer>> result = employeeService.getJobCodeByEmployeeIds(employeeIds);
        return result;
    }

    @ApiOperation(value = "通过企业ID和手机号获取员工", tags = "内部接口")
    @GetMapping("/findEmployeeByCompanyAndTel/{companyId}/{mobile}")
    EmployeeVO findEmployeeByCompanyAndTel(@PathVariable("companyId") String companyId, @PathVariable("mobile") String mobile) {
        EmployeeVO result = employeeService.findEmployeeByCompanyAndTel(companyId, mobile);
        return result;
    }

    @ApiOperation(value = "通过员工ID集合获取员工名集合", tags = "内部接口")
    @GetMapping("/findEmployeeNamesByIds")
    Map<String, String> findEmployeeNamesByIds(@RequestParam("employeeIds") String[] employeeIds) {
        Map<String, String> result = employeeService.findEmployeeNamesByIds(employeeIds);
        return result;
    }

    @ApiOperation(value = "通过企业ID集合和标签ID集合获取员工", tags = "内部接口")
    @GetMapping("/findEmployeeByCompanyIdsAndTagIds")
    List<EmployeeVO> findEmployeeByCompanyIdsAndTagIds(@RequestParam(value = "companyIds", required = false) Collection<String> companyIds, @RequestParam(value = "tagIds", required = false) Collection<String> tagIds, @RequestParam("translateParkCompany") Boolean translateParkCompany) {
        List<EmployeeVO> result = employeeService.findEmployeeByCompanyIdsAndTagIds(companyIds, tagIds, translateParkCompany);
        return result;
    }

    @ApiOperation(value = "获取每个企业员工个数", tags = "内部接口")
    @PostMapping("/queryEmployeesCount")
    public Map<String, Integer> queryEmployeesCount(@RequestBody Collection<String> companyIds) {
        Map<String, Integer> result = employeeService.queryEmployeesCount(companyIds);
        return result;
    }

    @ApiOperation(value = "通过员工人员设备ID获取员工", tags = "内部接口")
    @GetMapping("/findEmployeeByDevicePersonId/{devicePersonId}")
    EmployeeVO findEmployeeByDevicePersonId(@PathVariable("devicePersonId") Long devicePersonId) {
        return employeeService.findEmployeeByDevicePersonId(devicePersonId);
    }

    @ApiOperation(value = "根据员工名like查询企业员工", tags = "内部接口")
    @GetMapping("/listEmployeeByName")
    public Map<String, String> listEmployeeByName(@RequestParam("name") String name) {
        Map<String, String> employeeId2NameMap = employeeService.listEmployeeByName(name);
        return employeeId2NameMap;
    }

    @ApiOperation(value = "公寓人员管理-添加指纹", tags = "")
    @PostMapping("/finger")
    public ResponseResult<Boolean> finger(@RequestBody @Valid PersonFingerReqDTO reqDTO) {
        return ResponseResult.success(employeeManageService.addFinger(reqDTO));
    }

    @ApiOperation(value = "根据id获取员工信息", tags = "内部接口")
    @GetMapping("/getEmployeeById/{id}")
    public EmployeeVO getEmployeeById(@PathVariable("id") String id) {
        return employeeService.getEmployeeVOById(id);
    }

}

