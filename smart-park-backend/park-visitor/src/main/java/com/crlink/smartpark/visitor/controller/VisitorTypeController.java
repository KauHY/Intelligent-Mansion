package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.model.dto.VisitorTypeReqDTO;
import com.crlink.smartpark.visitor.model.query.VisitorTypeQuery;
import com.crlink.smartpark.visitor.dto.VisitorTypeDTO;
import com.crlink.smartpark.visitor.service.VisitorTypeService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * park-visitor
 * 访客类型
 * */
@Slf4j
@RestController
@RequestMapping("/visitor-type")
@Validated
@Api(tags = "访客模块-访客类型")
public class VisitorTypeController {

    @Autowired
    private VisitorTypeService visitorTypeService;


    @ApiOperation(value = "访客类型-新增", tags = "访客类型")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid VisitorTypeReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = visitorTypeService.saveOrUpdateVisitorType(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "访客类型-删除", tags = "访客类型")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(visitorTypeService.removeVisitorTypeById(id));
    }


    @ApiOperation(value = "访客类型-根据ID查询", tags = "访客类型")
    @GetMapping("/findById/{id}")
    public ResponseResult<VisitorTypeDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(visitorTypeService.getVisitorTypeById(id));
    }


    @ApiOperation(value = "访客类型-修改", tags = "访客类型")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid VisitorTypeReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        Boolean result = visitorTypeService.saveOrUpdateVisitorType(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "访客类型-分页查询", tags = "访客类型")
    @PostMapping("/listVisitorTypePage")
    public ResponseResult<PageInfo<VisitorTypeDTO>> listVisitorTypePage(@RequestBody VisitorTypeQuery query) {
        PageInfo<VisitorTypeDTO> result = visitorTypeService.listVisitorTypePage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "根据入驻企业获取访客类型", tags = "访客类型")
    @GetMapping("/listVisitorTypeByCompany")
    public ResponseResult<List<DictVO>> listVisitorTypeByCompany(String companyId) {
        List<DictVO> result = visitorTypeService.listVisitorTypeByCompany(companyId);
        return ResponseResult.success(result);
    }

}

