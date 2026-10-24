package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.wxuser.WxUserDTO;
import com.crlink.smartpark.system.query.WxUserQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 微信小程序用户 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/park/system/wx-user", contextId = "WxUserProvider")
public interface WxUserProvider {

    /**
     * 【微信小程序用户】新增
     *
     * @param wxUserDTO 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/create")
    ResponseResult<WxUserDTO> create(@RequestBody WxUserDTO wxUserDTO);

    /**
     * 【微信小程序用户】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @DeleteMapping("/remove/{id}")
    ResponseResult<Boolean> remove(@PathVariable("id") String id);

    /**
     * 【微信小程序用户】根据ID查询
     *
     * @param id 主键
     * @return 微信小程序用户实体
     */
    @GetMapping("/findById/{id}")
    ResponseResult<WxUserDTO> findById(@PathVariable("id") String id);

    /**
     * 【微信小程序用户】修改
     *
     * @param wxUserDTO 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/modify")
    ResponseResult<WxUserDTO> modify(@RequestBody WxUserDTO wxUserDTO);

    /**
     * 【微信小程序用户】分页查询
     *
     * @param wxUserQuery 查询参数
     * @return 分页数据
     */
    @PostMapping("/listPage")
    ResponseResult<PageInfo<WxUserDTO>> findListByPage(@RequestBody WxUserQuery wxUserQuery);

}
