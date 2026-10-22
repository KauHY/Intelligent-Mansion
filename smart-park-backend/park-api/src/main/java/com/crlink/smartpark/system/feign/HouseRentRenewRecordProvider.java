package com.crlink.smartpark.system.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park/system/house-rent-renew-record", contextId = "HouseRentRenewRecordProvider")
public interface HouseRentRenewRecordProvider {

    ///**
    // * 【】新增
    // *
    // * @param houseRentRenewRecordDTO 实体对象
    // * @return 返回保存成功的对象
    // */
    //@PostMapping("/create")
    //ResponseResult<HouseRentRenewRecordDTO> create(@RequestBody HouseRentRenewRecordDTO houseRentRenewRecordDTO);
    //
    ///**
    // * 【】删除
    // *
    // * @param id 主键
    // * @return 是否成功
    // */
    //@DeleteMapping("/remove/{id}")
    //ResponseResult<Boolean> remove(@PathVariable("id") String id);
    //
    ///**
    // * 【】根据ID查询
    // *
    // * @param id 主键
    // * @return 实体
    // */
    //@GetMapping("/findById/{id}")
    //ResponseResult<HouseRentRenewRecordDTO> findById(@PathVariable("id") String id);
    //
    ///**
    // * 【】修改
    // *
    // * @param houseRentRenewRecordDTO 实体对象
    // * @return 返回保存成功的对象
    // */
    //@PostMapping("/modify")
    //ResponseResult<HouseRentRenewRecordDTO> modify(@RequestBody HouseRentRenewRecordDTO houseRentRenewRecordDTO);
    //
    ///**
    // * 【】分页查询
    // *
    // * @param houseRentRenewRecordQuery 查询参数
    // * @return 分页数据
    // */
    //@PostMapping("/listPage")
    //ResponseResult<PageInfo<HouseRentRenewRecordDTO>> findListByPage(@RequestBody HouseRentRenewRecordQuery houseRentRenewRecordQuery);

}
