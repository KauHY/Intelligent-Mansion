package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.role.CreateRoleReqDTO;
import com.crlink.smartpark.system.query.RoleQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/role", contextId = "RoleProvider")
public interface RoleProvider {

    /**
     * 【系统角色】保存
     *
     * @param role 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/save")
    ResponseResult<CreateRoleReqDTO> create(@RequestBody CreateRoleReqDTO role);

    /**
     * 【系统角色】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @DeleteMapping("/remove/{id}")
    ResponseResult<Boolean> remove(@PathVariable("id") String id);

    /**
     * 【系统角色】根据ID查询
     *
     * @param id 主键
     * @return 系统角色实体
     */
    @GetMapping("/findById/{id}")
    ResponseResult<CreateRoleReqDTO> findById(@PathVariable("id") String id);

    /**
     * 【系统角色】分页查询
     *
     * @param roleQuery 查询参数
     * @return 分页数据
     */
    @PostMapping("/listPage")
    ResponseResult<PageInfo<CreateRoleReqDTO>> findListByPage(@RequestBody RoleQuery roleQuery);

}
