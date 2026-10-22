package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.company.CompanyBindUpdateDTO;
import com.crlink.smartpark.system.dto.company.CompanyQueryDTO;
import com.crlink.smartpark.system.dto.company.CompanySaveDTO;
import com.crlink.smartpark.system.query.CompanyBindListQuery;
import com.crlink.smartpark.system.query.CompanyQuery;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.crlink.smartpark.system.vo.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 入驻企业管理
 */
@Slf4j
@Api(tags = "园区运营平台-入驻企业管理")
@RestController
@RequestMapping("/parkManagement/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 入驻企业-保存
     */
    @ApiOperation(value = "入驻企业-保存", tags = "入驻企业管理")
    @PostMapping("/save")
    public ResponseResult create(@RequestBody @Valid CompanySaveDTO companySaveDTO) {
        companyService.saveOrUpdateCompany(companySaveDTO);
        return ResponseResult.success();
    }

    /**
     * 入驻企业-删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "入驻企业-删除", tags = "入驻企业管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") String id) {
        Boolean result = companyService.removeCompanyById(id);
        return ResponseResult.success(result);
    }

    /**
     * 入驻企业-根据ID查询
     *
     * @param id 主键
     * @return 入驻企业表实体
     */
    @ApiOperation(value = "入驻企业-根据ID查询", tags = "入驻企业管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<CompanyVO> findById(@PathVariable("id") String id) {
        CompanyVO result = companyService.getCompanyById(id);
        return ResponseResult.success(result);
    }

    /**
     * 入驻企业-分页查询
     *
     * @param companyQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "入驻企业-分页查询", tags = "入驻企业管理")
    @PostMapping("/listCompanyPage")
    public ResponseResult<PageInfo<CompanyListVO>> findListByPage(@RequestBody @Valid CompanyQuery companyQuery) {
        PageInfo<CompanyListVO> result = companyService.listCompanyPage(companyQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "入驻企业-导出", tags = "入驻企业管理")
    @PostMapping("/export")
    public void export(@RequestBody @Valid CompanyQuery query) {
        companyService.export(query);
    }


    @ApiOperation(value = "入驻企业导入", tags = "入驻企业导入")
    @PostMapping("/importCompany")
    public ResponseResult<FileImportErrorInfoDTO> importCompany(@RequestPart("file") MultipartFile file, @RequestParam String parkOrgId) {
        FileImportErrorInfoDTO errorInfoDTO = companyService.importCompany(file, parkOrgId);
        return ResponseResult.success(errorInfoDTO);
    }

    /**
     * 更改员工状态
     *
     * @return
     */
    @ApiOperation(value = "更改企业状态", tags = "入驻企业管理")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@Valid @RequestBody UpdateStatusReqDTO statusReqDTO) {
        AssertUtil.requireNotNull(statusReqDTO.getId(), "10007");
        Boolean result = companyService.updateCompanyStatus(statusReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 入驻企业-分页查询客户端绑定关系
     *
     * @param query 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "入驻企业-分页查询客户端绑定关系", tags = "配置管理")
    @PostMapping("/clientBindInfo/page")
    public ResponseResult<PageInfo<CompanyBindListVO>> findListByPage(@RequestBody @Valid CompanyBindListQuery query) {
        PageInfo<CompanyBindListVO> result = companyService.pageBindList(query);
        return ResponseResult.success(result);
    }

    /**
     * 入驻企业-客户端绑定关系修改
     *
     * @param dto 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "入驻企业-客户端绑定关系设置/新增", tags = "配置管理")
    @PostMapping("/clientBindInfo")
    public ResponseResult saveBindClient(@RequestBody @Valid CompanyBindUpdateDTO dto) {
        dto.setSaveOperation(true);
        companyService.updateClientInfo(dto);
        return ResponseResult.success();
    }

    /**
     * 入驻企业-客户端绑定关系修改
     *
     * @param dto 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "入驻企业-客户端绑定关系设置/修改", tags = "配置管理")
    @PutMapping("/clientBindInfo")
    public ResponseResult updateBindClient(@RequestBody @Valid CompanyBindUpdateDTO dto) {
        companyService.updateClientInfo(dto);
        return ResponseResult.success();
    }

    /**
     * 入驻企业-客户端绑定关系删除
     *
     * @param companyId 企业ID
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "入驻企业-客户端绑定关系删除", tags = "配置管理")
    @DeleteMapping("/clientBindInfo/{companyId}")
    public ResponseResult cleanClientBindInfo(@PathVariable String companyId) {
        companyService.cleanClientBindInfo(companyId);
        return ResponseResult.success();
    }

    /**
     * 入驻企业-根据ID查询企业宣传信息
     *
     * @param id 主键
     * @return 入驻企业表实体
     */
    @ApiOperation(value = "入驻企业-根据ID查询企业宣传信息", tags = "入驻企业管理")
    @GetMapping("/findById/clientBindInfo/{id}")
    public ResponseResult<CompanyClientInfoVO> getClientBindInfo(@PathVariable("id") String id) {
        CompanyClientInfoVO result = companyService.getClientBindInfo(id);
        return ResponseResult.success(result);
    }

    /**
     * 入驻企业-根据园区ID获取公司名列表
     *
     * @param parkOrgId 企业ID
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "入驻企业-根据园区ID获取公司名列表", tags = "入驻企业管理")
    @GetMapping("/listCompanyDict")
    public ResponseResult<List<SimpleNode>> listCompanyDictByParkOrgId(String parkOrgId, Boolean filterUnBindClientInfo) {
        List<SimpleNode> result = companyService.listCompanyDictByParkOrgId(parkOrgId, filterUnBindClientInfo);
        return ResponseResult.success(result);
    }

    /**
     * 查询园区下企业
     *
     * @param parkOrgId
     * @param companyType 0 物业，1普通， 空查所有
     * @return
     */
    @ApiOperation(value = "获取下拉列表", tags = "入驻企业管理")
    @GetMapping("/listCompanyDictByType")
    public ResponseResult<List<DictVO>> listCompanyDictByType(@RequestParam("parkOrgId") String parkOrgId,
                                                              @RequestParam(value = "companyType", required = false) Integer companyType) {
        List<DictVO> result = companyService.listCompanyDictByType(parkOrgId, companyType);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "获取物业下拉列表", tags = "入驻企业管理")
    @GetMapping("/listPropertyCompanyDict")
    public ResponseResult<List<DictVO>> listPropertyCompanyDict(@RequestParam("parkOrgId") String parkOrgId,
                                                                @RequestParam(value = "type", required = false) Integer type) {
        List<DictVO> result = companyService.listPropertyCompanyDictByParkOrgId(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "入驻企业总览", tags = "运营大屏")
    @GetMapping("/getCompanyOverview")
    public ResponseResult<CompanyOverviewDTO> getCompanyOverview(@RequestParam("parkOrgId") String parkOrgId) {
        CompanyOverviewDTO result = companyService.getCompanyOverview(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "获取当前用户的物业公司", tags = "入驻企业管理")
    @GetMapping("/curUserPropertyCompanyDict")
    public ResponseResult<List<DictVO>> curUserPropertyCompanyDict() {
        List<DictVO> result = companyService.curUserPropertyCompanyDict();
        return ResponseResult.success(result);
    }


    /**
     * 查询企业入驻的园区
     *
     * @param code 企业信用代码
     * @return
     */
    @ApiOperation(value = "查询企业入驻的园区", tags = "入驻企业管理")
    @GetMapping("/listParkDictByCode")
    public ResponseResult<List<DictVO>> listParkDictByCode(@RequestParam("code") String code) {
        List<DictVO> result = companyService.listParkDictByCode(code);
        return ResponseResult.success(result);
    }

    /**
     * 【入驻企业表】通过名字查ID集合
     *
     * @param companyName 主键
     */
    @ApiOperation(value = "通过名字查ID集合", tags = "入驻企业管理")
    @GetMapping("/findIdsByName")
    public ResponseResult<Set<String>> findIdsByName(@RequestParam("companyName") String companyName) {
        Set<String> result = companyService.findIdsByName(companyName);
        return ResponseResult.success(result);
    }

    /**
     * 【入驻企业表】通过企业端用户id查ID集合
     *
     * @param businessUserId 主键
     */
    @ApiOperation(value = "通过企业端用户id查ID集合", tags = "内部接口-入驻企业管理")
    @GetMapping("/findIdsByBusinessUserId")
    public Set<String> findIdsByBusinessUserId(@RequestParam("businessUserId") String businessUserId) {
        Set<String> result = companyService.findIdsByBusinessUserId(businessUserId);
        return result;
    }


    @ApiOperation(value = "条件查询入驻企业", tags = "内部接口")
    @PostMapping("/queryHouseColumns")
    public Map<String, Map<String, Object>> queryHouseColumns(@RequestBody CompanyQueryDTO dto) {
        Map<String, Map<String, Object>> map = companyService.queryHouseColumns(dto);

        return map;
    }
}

