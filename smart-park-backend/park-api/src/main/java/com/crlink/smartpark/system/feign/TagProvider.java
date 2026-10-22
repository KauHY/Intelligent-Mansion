package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.tag.TagDTO;
import com.crlink.smartpark.system.query.TagQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/park/system/tag", contextId = "TagProvider")
public interface TagProvider {

    /**
     * 【标签表】新增
     *
     * @param tagDTO 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/create")
    ResponseResult<TagDTO> create(@RequestBody TagDTO tagDTO);

    /**
     * 【标签表】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @DeleteMapping("/remove/{id}")
    ResponseResult<Boolean> remove(@PathVariable("id") String id);

    /**
     * 【标签表】根据ID查询
     *
     * @param id 主键
     * @return 标签表实体
     */
    @GetMapping("/findById/{id}")
    ResponseResult<TagDTO> findById(@PathVariable("id") String id);

    /**
     * 【标签表】修改
     *
     * @param tagDTO 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/modify")
    ResponseResult<TagDTO> modify(@RequestBody TagDTO tagDTO);

    /**
     * 【标签表】分页查询
     *
     * @param tagQuery 查询参数
     * @return 分页数据
     */
    @PostMapping("/listPage")
    ResponseResult<PageInfo<TagDTO>> findListByPage(@RequestBody TagQuery tagQuery);

}
