package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.business.dto.CustomerTagDTO;
import com.crlink.smartpark.business.query.CustomerTagQuery;
import com.crlink.smartpark.business.service.CustomerTagService;
import com.crlink.smartpark.business.vo.CustomerTagVO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * park-business
 * 客户标签 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-客户标签管理")
@RequestMapping("/business/customer-tag")
public class CustomerTagController {

    /**
     * 客户标签
     */
    @Autowired
    private CustomerTagService customerTagService;

    /**
     * 【客户标签】新增
     *
     * @param customerTagDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "客户标签-新增", tags = "客户标签")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid CustomerTagDTO customerTagDTO) {
        customerTagDTO.setId(null);
        Boolean result = customerTagService.saveOrUpdateCustomerTag(customerTagDTO);
        return ResponseResult.success(result);
    }

    /**
     * 【客户标签】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "客户标签-删除", tags = "客户标签")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        boolean result = customerTagService.removeCustomerTag(id);
        return ResponseResult.success(result);
    }


    /**
     * 【客户标签】修改
     *
     * @param customerTagDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "客户标签-修改", tags = "客户标签")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid CustomerTagDTO customerTagDTO) {
        Boolean result = customerTagService.saveOrUpdateCustomerTag(customerTagDTO);
        return ResponseResult.success(result);
    }

    /**
     * 【客户标签】分页查询
     *
     * @param query 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "客户标签-分页查询", tags = "客户标签")
    @PostMapping("/listCustomerTagPage")
    public ResponseResult<PageInfo<CustomerTagVO>> listCustomerTagPage(@RequestBody CustomerTagQuery query) {
        PageInfo<CustomerTagVO> result = customerTagService.listCustomerTagPage(query);
        return ResponseResult.success(result);
    }

    /**
     * 查看客户标签名
     */
    @ApiOperation(value = "客户标签-客户标签下拉框", tags = "客户标签")
    @GetMapping("/listCustomerTagDict/{parkOrgId}")
    public ResponseResult<List<DictVO>> listCustomerTagDict(@PathVariable("parkOrgId") @NotEmpty String parkOrgId) {
        List<DictVO> result = customerTagService.listCustomerTagDict(parkOrgId);
        return ResponseResult.success(result);
    }

}

