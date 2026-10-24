package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.controller.request.StatusReqDTO;
import com.crlink.smartpark.visitor.dto.AccessPolicySaveDTO;
import com.crlink.smartpark.visitor.dto.AccessPolicyUpdateDTO;
import com.crlink.smartpark.visitor.query.AccessPolicyQuery;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import com.crlink.smartpark.visitor.vo.AccessPolicyListVO;
import com.crlink.smartpark.visitor.vo.AccessPolicyVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

/**
 * 园区运营平台-人行管理
 * */
@Slf4j
@RestController
@RequestMapping("/access-policy")
@Api(tags = "人行管理-通行策略")
@Validated
public class AccessPolicyController {

    /**
     * 通行策略
     */
    @Autowired
    private AccessPolicyService accessPolicyService;

    /**
     * 【通行策略】新增
     *
     * @param accessPolicySaveDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "通行策略-新增", tags = "人行管理")
    @PostMapping("/create")
    public ResponseResult create(@RequestBody @Validated AccessPolicySaveDTO accessPolicySaveDTO) {
        accessPolicyService.save2(accessPolicySaveDTO);
        return ResponseResult.success();
    }

    /**
     * 【通行策略】修改
     *
     * @param accessPolicyUpdateDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "通行策略-修改", tags = "人行管理")
    @PutMapping("/modify")
    public ResponseResult modify(@RequestBody @Validated AccessPolicyUpdateDTO accessPolicyUpdateDTO) {
        accessPolicyService.update2(accessPolicyUpdateDTO);
        return ResponseResult.success();
    }

    /**
     * 【通行策略】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "通行策略-删除", tags = "人行管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(accessPolicyService.delAccessPolicy(id));
    }

    /**
     * 【通行策略】根据ID查询
     *
     * @param id 主键
     * @return 通行策略实体
     */
    @ApiOperation(value = "通行策略-根据ID查询", tags = "人行管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<AccessPolicyVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(accessPolicyService.getAccessPolicyById(id));
    }


    @ApiOperation(value = "通行策略-设置状态", tags = "人行管理")
    @PostMapping("/setStatus")
    public ResponseResult<Boolean> setStatus(@RequestBody @Validated StatusReqDTO reqDTO) {
        return ResponseResult.success(accessPolicyService.updateStatus(reqDTO));
    }

    /**
     * 【通行策略】分页查询
     *
     * @param accessPolicyQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "通行策略-分页查询", tags = "人行管理")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<AccessPolicyListVO>> findListByPage(@RequestBody AccessPolicyQuery accessPolicyQuery) {
        PageInfo<AccessPolicyListVO> result = accessPolicyService.listPage(accessPolicyQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "通过员工信息获取关联设备ID", tags = "人行管理")
    @GetMapping("/getAccessDeviceIdsByEmployeeInfo")
    public Set<String> getAccessDeviceIdsByEmployeeInfo(@RequestParam String employeeCompanyId, @RequestParam(value = "employeeTagIds", required = false) Collection<String> employeeTagIds) {
        return accessPolicyService.getAccessDeviceIdsByEmployeeInfo(employeeCompanyId, employeeTagIds);
    }

    @ApiOperation(value = "员工标签是否存在于通行策略", tags = "人行管理")
    @GetMapping("/getTagIdsExist")
    public Boolean getTagIdsExist(@RequestParam(value = "employeeTagId") String employeeTagId) {
        return accessPolicyService.getTagIdsExist(employeeTagId);
    }

    @ApiOperation(value = "设备是否存在", tags = "人行管理")
    @GetMapping("/existsDevice")
    public boolean existsDevice(@RequestParam(value = "deviceId") String deviceId) {
        return accessPolicyService.existsDevice(deviceId);
    }

}

