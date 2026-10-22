package com.crlink.smartpark.common.controller;

import com.crlink.smartpark.common.dto.MultistageCategoryDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationQueryDTO;
import com.crlink.smartpark.common.entity.MultistageCategoryRelation;
import com.crlink.smartpark.common.model.MultistageCategoryRelationDelDTO;
import com.crlink.smartpark.common.model.MultistageCategoryRelationReqDTO;
import com.crlink.smartpark.common.model.MultistageCategoryReqDTO;
import com.crlink.smartpark.common.entity.MultistageCategory;
import com.crlink.smartpark.common.model.MultistageCategoryUpdateReqDTO;
import com.crlink.smartpark.common.model.query.MultistageCategoryRelationQuery;
import com.crlink.smartpark.common.service.MultistageCategoryRelationService;
import com.crlink.smartpark.common.service.MultistageCategoryService;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.system.vo.TreeDictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * park-common
 * 多级分类表
 * */
@Slf4j
@RestController
@RequestMapping("/multistage-category")
@Api(tags = "多级分类")
@Validated
public class MultistageCategoryController {

    @Autowired
    private MultistageCategoryService multistageCategoryService;
    @Autowired
    private MultistageCategoryRelationService multistageCategoryRelationService;


    @ApiOperation(value = "多级分类表-新增", tags = "多级分类表", notes = "台账-TZ,企业群组-ENTERPRISE_GROUP" +"<br>" + "新增园区下一级分类不传parentId")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid MultistageCategoryReqDTO reqDTO) {
        return ResponseResult.success(multistageCategoryService.saveMultistageCategory(reqDTO));
    }


    @ApiOperation(value = "多级分类表-删除", tags = "多级分类表")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(multistageCategoryService.removeCategoryAndSub(id));
    }


    @ApiOperation(value = "多级分类表-根据ID查询", tags = "多级分类表")
    @GetMapping("/findById/{id}")
    public ResponseResult<MultistageCategoryDTO> findById(@PathVariable("id") @NotBlank(message = "id不能为空") String id) {
        return ResponseResult.success(multistageCategoryService.getMultistageCategoryById(id));
    }

    @ApiOperation(value = "多级分类表-是否有下级节点", tags = "多级分类表")
    @GetMapping("/hasSub/{id}")
    public ResponseResult<Boolean> hasSub(@PathVariable("id") @NotBlank(message = "id不能为空") String id) {
        return ResponseResult.success(multistageCategoryService.hasSub(id));
    }

    @ApiOperation(value = "多级分类表-是否被关联", tags = "多级分类表", notes = "true-已被关联；false-未被关联")
    @GetMapping("/hasAssociate")
    public ResponseResult<Boolean> hasAssociate(
            @RequestParam("type") @NotBlank(message = "type不能为空") String type,
            @RequestParam("id") @NotBlank(message = "id不能为空") String id
    ) {
        return ResponseResult.success(multistageCategoryService.hasAssociate(type, id));
    }


    @ApiOperation(value = "多级分类表-修改", tags = "多级分类表", notes = "台账-TZ,企业群组-ENTERPRISE_GROUP")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid MultistageCategoryUpdateReqDTO reqDTO) {
        return ResponseResult.success(multistageCategoryService.updateMultistageCategory(reqDTO));
    }


    @ApiOperation(value = "多级分类表-获取园区下对应类型的树", tags = "多级分类表", notes = "台账-TZ,企业群组-ENTERPRISE_GROUP")
    @PostMapping("/listMultistageCategoryTree")
    public ResponseResult<TreeDictVO> listMultistageCategoryTree(@RequestParam String parkOrgId, @RequestParam String categoryType) {
        return ResponseResult.success(multistageCategoryService.listMultistageCategoryTree(parkOrgId, categoryType));
    }

    @ApiOperation(value = "多级分类表-内部接口-获取所有子节点", tags = "多级分类表")
    @GetMapping("/getAllSubId")
    public Set<String> getAllSubId(@NotBlank(message = "id不能为空") String id) {
        return multistageCategoryService.getAllSubId(id);
    }

    @ApiOperation(value = "多级分类表-根据ids查名", tags = "多级分类表")
    @GetMapping("/getNamesByIds")
    public Map<String, String> getNamesByIds(@RequestParam("ids") Set<String> ids) {
        return (Map<String, String>) multistageCategoryService.id2ValueMap(ids, MultistageCategory::getLabel);
    }

    @ApiOperation(value = "台账类型通过二级分类名获取ID", tags = "多级分类表")
    @GetMapping("/getTzIdBySecondName")
    public String getTzIdBySecondName(@RequestParam("name") String name, @RequestParam("parkOrgId") String parkOrgId) {
        return multistageCategoryService.getTzIdBySecondName(name, parkOrgId);
    }

    @ApiOperation(value = "多级分类关联-关联", tags = "多级分类关联", notes = "一对一关联传businessId<br>一对多关联传businessIds")
    @GetMapping("/getAllRelationByCategory")
    public ResponseResult<MultistageCategoryRelationDTO> getAllRelationByCategory(
            @RequestParam(value = "categoryId")@NotBlank(message = "categoryId不能为空") String categoryId) {
        return ResponseResult.success(multistageCategoryRelationService.getAllRelationByCategory(categoryId));
    }

    @ApiOperation(value = "多级分类关联-关联", tags = "多级分类关联", notes = "一对一关联传businessId<br>一对多关联传businessIds")
    @PostMapping("/associate")
    public ResponseResult<Boolean> associate(@RequestBody @Valid MultistageCategoryRelationReqDTO reqDTO) {
        return ResponseResult.success(multistageCategoryRelationService.associate(reqDTO));
    }


    @ApiOperation(value = "多级分类关联-取消关联", tags = "多级分类关联")
    @PostMapping("/disassociate")
    public ResponseResult<Boolean> disassociate(@RequestBody @Valid MultistageCategoryRelationDelDTO reqDTO) {
        return ResponseResult.success(multistageCategoryRelationService.disassociate(reqDTO));
    }

    @ApiOperation(value = "多级分类关联-根据业务、分类ID查询", tags = "多级分类表")
    @GetMapping("/getRelationDTO")
    public ResponseResult<MultistageCategoryRelationDTO> getRelationDTO(
            @RequestParam(value = "businessId")@NotBlank(message = "businessId不能为空") String businessId,
            @RequestParam(value = "categoryId")@NotBlank(message = "categoryId不能为空") String categoryId
    ) {
        return ResponseResult.success(multistageCategoryRelationService.getMultistageCategoryRelation(businessId, categoryId));
    }

    @ApiOperation(value = "多级分类关联-根据关联ID更新", tags = "多级分类关联")
    @PostMapping("/updateRelation")
    public ResponseResult<Boolean> updateRelationById(@RequestBody @Valid MultistageCategoryRelationReqDTO reqDTO) {
        return ResponseResult.success(multistageCategoryRelationService.saveOrUpdateMultistageCategoryRelation(reqDTO));
    }

    @ApiOperation(value = "内部接口-业务id对多分类id查询列表", tags = "多级分类关联")
    @PostMapping("/listBusinessToNCategoryRelation")
    public PageInfo<MultistageCategoryRelationDTO> listBusinessToNCategoryRelation(@RequestBody @Valid MultistageCategoryRelationQueryDTO reqDTO) {
        return multistageCategoryRelationService.listPageBusinessToNCategoryRelation(reqDTO);
    }

    @ApiOperation(value = "内部接口-业务id对分类一对一id查询列表", tags = "多级分类关联")
    @PostMapping("/listBusinessToCategoryRelation")
    public PageInfo<MultistageCategoryRelationDTO> listBusinessToCategoryRelation(@RequestBody @Valid MultistageCategoryRelationQueryDTO reqDTO) {
        return multistageCategoryRelationService.listPageBusinessToCategoryRelation(reqDTO);
    }

}

