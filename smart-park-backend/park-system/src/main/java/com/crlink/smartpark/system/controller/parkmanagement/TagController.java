package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.tag.TagDTO;
import com.crlink.smartpark.system.dto.tag.TagReqDTO;
import com.crlink.smartpark.system.query.TagQuery;
import com.crlink.smartpark.system.service.parkmanagement.TagService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 标签管理
 * */
@Slf4j
@RestController
@Api(tags = "员工标签管理")
@RequestMapping("/parkManagement/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 标签新增
     */
    @ApiOperation(value = "标签新增", tags = "标签管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@Valid @RequestBody TagReqDTO tagDTO) {
        tagDTO.setId(null);
        Boolean result = tagService.saveOrUpdateTag(tagDTO);
        return ResponseResult.success(result);
    }

    /**
     * 标签删除
     */
    @ApiOperation(value = "标签删除", tags = "标签管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(tagService.removeTagById(id));
    }


    /**
     * 标签修改
     */
    @ApiOperation(value = "标签修改", tags = "标签管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@Valid @RequestBody TagReqDTO tagDTO) {
        AssertUtil.requireNotNull(tagDTO.getId(), "10007");
        Boolean result = tagService.saveOrUpdateTag(tagDTO);
        return ResponseResult.success(result);
    }

    /**
     * 【标签表】分页查询
     *
     * @param tagQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "标签表-分页查询", tags = "标签表")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<TagDTO>> findListByPage(@RequestBody TagQuery tagQuery) {
        PageInfo<TagDTO> result = tagService.listTagPage(tagQuery);
        return ResponseResult.success(result);
    }


    /**
     * 员工标签-下拉列表
     *
     * @return 分页数据
     */
    @ApiOperation(value = "员工标签-下拉列表", tags = "员工标签")
    @PostMapping("/listTagDict")
    public ResponseResult<List<DictVO>> listTagDict(@RequestBody TagQuery tagQuery) {
        List<DictVO> result = tagService.listTagDict(tagQuery);
        return ResponseResult.success(result);
    }





}

