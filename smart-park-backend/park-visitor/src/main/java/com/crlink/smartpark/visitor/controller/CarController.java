package com.crlink.smartpark.visitor.controller;

import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.dto.CarSaveDTO;
import com.crlink.smartpark.visitor.query.CarQuery;
import com.crlink.smartpark.visitor.service.CarService;
import com.crlink.smartpark.visitor.vo.CarVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 园区运营平台-车行管理
 * */
@Slf4j
@RestController
@RequestMapping("/car")
@Api(tags = "访客模块-车辆管理")
public class CarController {

    @Autowired
    private CarService carService;

    /**
    * 【车辆管理】新增
    *
    * @param carSaveDTO 实体对象
    * @return 返回保存成功的对象
    */
    @ApiOperation(value = "车辆管理-新增", tags = "车辆管理")
    @PostMapping("/create")
    public ResponseResult<CarSaveDTO> create(@RequestBody @Validated CarSaveDTO carSaveDTO) {
        carService.saveOrUpdateCar(carSaveDTO);
        return ResponseResult.success();
    }

    /**
     * 【车辆管理】修改
     *
     * @param carSaveDTO 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "车辆管理-修改", tags = "车辆管理")
    @PostMapping("/modify")
    public ResponseResult<CarSaveDTO> modify(@RequestBody @Validated CarSaveDTO carSaveDTO) {
        AssertUtil.requireTrue(StrUtil.isNotBlank(carSaveDTO.getId()), "10007");
        carService.saveOrUpdateCar(carSaveDTO);
        return ResponseResult.success();
    }

    /**
     * 【车辆管理】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "车辆管理-删除", tags = "车辆管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        carService.deleteCar(id);
        return ResponseResult.success();
    }

    /**
    * 【车辆管理】根据ID查询
    *
    * @param id 主键
    * @return 车辆管理实体
    */
    @ApiOperation(value = "车辆管理-根据ID查询", tags = "车辆管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<CarVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(carService.getCarById(id));
    }

    /**
    * 【车辆管理】分页查询
    *
    * @param carQuery 查询参数
    * @return 分页数据
    */
    @ApiOperation(value = "车辆管理-分页查询", tags = "车辆管理")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<CarVO>> listPage(@RequestBody CarQuery carQuery){
        PageInfo<CarVO> result = carService.listPage(carQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "车辆管理-导入", tags = "车辆管理")
    @PostMapping("/import")
    public ResponseResult<FileImportErrorInfoDTO> importExcel(@RequestPart("file") MultipartFile file,
                                                              @RequestParam String parkOrgId,
                                                              @RequestParam String companyId,
                                                              @RequestParam Set<String> parkingLotIds) {
        FileImportErrorInfoDTO errorInfoDTO = carService.importExcel(file, parkOrgId, companyId, parkingLotIds);
        return ResponseResult.success(errorInfoDTO);
    }

}

