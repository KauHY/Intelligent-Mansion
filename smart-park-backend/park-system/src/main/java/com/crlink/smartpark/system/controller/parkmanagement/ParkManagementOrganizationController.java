package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.common.CacheUtil;
import com.crlink.smartpark.system.dto.organization.OrgDTO;
import com.crlink.smartpark.system.dto.organization.OrgQuery;
import com.crlink.smartpark.system.dto.organization.OrgRespVO;
import com.crlink.smartpark.system.dto.organization.OrgStructureVO;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementOrganizationService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 园区运营平台-组织架构
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-组织架构")
@RequestMapping("/parkManagement/organization")
public class ParkManagementOrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private ParkManagementOrganizationService parkManagementOrganizationService;


    //@ApiOperation(value = "根据用户查询所有组织", tags = "组织架构")
    //@GetMapping("/org/userId")
    //public Set<Organization> getOrgByUserId(String userId) {
    //    Set<Organization> organizations = organizationManageService.listOrganizationByUserId(userId);
    //    return organizations;
    //}

    /**
     * 根据用户查询组织结构,
     */
    @ApiOperation(value = "根据Id查询组织详情", tags = "组织架构")
    @GetMapping("/getOrganizationById")
    public ResponseResult<OrgRespVO> getOrganizationById(@RequestParam(value = "Id") String Id) {
        OrgRespVO result = organizationManageService.getOrganizationById(Id);
        return ResponseResult.success(result);
    }


    /**
     * 根据用户查询组织结构,
     */
    @ApiOperation(value = "根据用户查询组织结构", tags = "组织架构")
    @GetMapping("/childOrg")
    public ResponseResult<List<OrgDTO>> getOrgStructureByUserId(@RequestParam(value = "parentId", required = false) String parentId) {
        List<OrgDTO> OrgDTOS = organizationManageService.listOrgStructureByUserId(parentId);
        return ResponseResult.success(OrgDTOS);
    }


    /**
     * 新增组织
     */
    @ApiOperation(value = "新增组织", tags = "组织架构")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid OrgDTO orgDTO) {
        orgDTO.setId(null);
        boolean result = parkManagementOrganizationService.createOrganization(orgDTO);
        return ResponseResult.success(result);
    }

    /**
     * 编辑组织
     */
    @ApiOperation(value = "编辑组织", tags = "组织架构")
    @PostMapping("/update")
    public ResponseResult<Boolean> update(@RequestBody @Valid OrgDTO orgDTO) {
        boolean result = parkManagementOrganizationService.updateOrganization(orgDTO);
        return ResponseResult.success(result);
    }

    /**
     * 删除组织
     */
    @ApiOperation(value = "删除组织", tags = "组织架构")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = parkManagementOrganizationService.removeOrganizationById(id);
        return ResponseResult.success(result);
    }


    /**
     * 组织分页查询
     *
     * @return
     */
    @ApiOperation(value = "组织分页查询", tags = "组织架构")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<OrgRespVO>> findListByPage(@RequestBody OrgQuery query) {
        PageInfo<OrgRespVO> result = parkManagementOrganizationService.listOrganizationPage(query);
        return ResponseResult.success(result);
    }

    /**
     * 组织树
     */
    @ApiOperation(value = "组织树", tags = "组织架构")
    @PostMapping("/tree")
    public ResponseResult<OrgStructureVO> getOrgTree(@RequestBody BaseQuery<String> query) {
        OrgStructureVO result = organizationManageService.listOrganization(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "获取租户下所有的园区", tags = "组织架构")
    @GetMapping("/getTenantParkOrgDict")
    public ResponseResult<List<DictVO>> getTenantParkOrgDict() {
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        List<DictVO> parkOrgIds = organizationManageService.getTenantParkOrgDict(headOrgId);
        return ResponseResult.success(parkOrgIds);
    }

    @ApiOperation(value = "用户所属的园区Id集合", tags = "内部接口")
    @GetMapping("/getUserParkOrgIds")
    public Set<String> getUserParkOrgIds() {
        Set<String> parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
        return parkOrgIds;
    }


    @GetMapping("/getTenantParkOrgIds")
    @ApiOperation(value = "获取租户下所有的园区Id", tags = "内部接口")
    public Set<String> getTenantParkOrgIds(String headOrgId) {
        //String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        Set<String> parkOrgIds = organizationManageService.getTenantParkOrgIds(Sets.newHashSet(headOrgId));
        return parkOrgIds;
    }


}

