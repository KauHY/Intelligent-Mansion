package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.park.ParkSaveDTO;
import com.crlink.smartpark.system.query.ParkQuery;
import com.crlink.smartpark.system.vo.ParkVO;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 园区表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/parkManagement/park", contextId = "ParkProvider")
public interface ParkProvider {

    /**
     * 【园区表】保存
     *
     * @param park 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/save")
    ResponseResult<ParkSaveDTO> create(@RequestBody ParkSaveDTO park);

    /**
     * 【园区表】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @DeleteMapping("/remove/{id}")
    ResponseResult<Boolean> remove(@PathVariable("id") String id);

    /**
     * 【园区表】根据ID查询
     *
     * @param id 主键
     * @return 园区表实体
     */
    @GetMapping("/findById/{id}")
    ResponseResult<ParkVO> findById(@PathVariable("id") String id);

    /**
     * 【园区表】分页查询
     *
     * @param parkQuery 查询参数
     * @return 分页数据
     */
    @PostMapping("/listPage")
    ResponseResult<PageInfo<ParkVO>> findListByPage(@RequestBody ParkQuery parkQuery);


}
