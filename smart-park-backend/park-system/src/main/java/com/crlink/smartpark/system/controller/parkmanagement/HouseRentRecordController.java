package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.bean.ValidListDTO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.house.HouseRentRecordSaveDTO;
import com.crlink.smartpark.system.dto.house.HouseRentRenewDTO;
import com.crlink.smartpark.system.query.CompanyHouseQuery;
import com.crlink.smartpark.system.query.HouseBindRecordQuery;
import com.crlink.smartpark.system.query.HouseRentRenewRecordQuery;
import com.crlink.smartpark.system.service.parkmanagement.HouseRentRecordService;
import com.crlink.smartpark.system.vo.*;
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
 * 系统管理
 * 房屋入驻记录表 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-入驻企业管理")
@RequestMapping("/parkManagement/house-rent-record")
public class HouseRentRecordController {

    /**
     * 房屋入驻记录表
     */
    @Autowired
    private HouseRentRecordService houseRentRecordService;

    /**
     * 房屋绑定
     *
     * @param houses 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "房屋绑定", tags = "入驻企业管理")
    @PostMapping("/bindHouse/{companyId}")
    public ResponseResult bindHouse(@PathVariable("companyId") String companyId, @RequestBody @Valid ValidListDTO<HouseRentRecordSaveDTO> houses) {
        houseRentRecordService.bindHouse(companyId, houses.getList());
        return ResponseResult.success();
    }

    /**
     * 房屋解绑
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "房屋解绑", tags = "入驻企业管理")
    @PostMapping("/unbindHouse/{id}")
    public ResponseResult<Boolean> unbindHouse(@PathVariable("id") String id) {
        houseRentRecordService.unbindHouse(id);
        return ResponseResult.success();
    }

    /**
     * 企业已绑定房屋分页查询
     *
     * @param companyHouseQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "企业已绑定房屋分页查询", tags = "入驻企业管理")
    @PostMapping("/pageCompanyHouseBind")
    public ResponseResult<PageInfo<HouseBindListVO>> pageCompanyHouseBind(@RequestBody @Valid CompanyHouseQuery companyHouseQuery) {
        PageInfo<HouseBindListVO> result = houseRentRecordService.pageCompanyHouseBind(companyHouseQuery);
        return ResponseResult.success(result);
    }


    /**
     * 【房屋入驻记录】分页查询
     *
     * @param query 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "房屋入驻记录-分页查询", tags = "入驻企业管理")
    @PostMapping("/pageHouseBindRecord")
    public ResponseResult<PageInfo<HouseBindRecordListVO>> pageHouseBindRecord(@RequestBody @Valid HouseBindRecordQuery query) {
        PageInfo<HouseBindRecordListVO> result = houseRentRecordService.pageHouseBindRecord(query);
        return ResponseResult.success(result);
    }

    /**
     * 续租房屋
     *
     * @param dto 实体对象
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "房屋续租", tags = "入驻企业管理")
    @PostMapping("/renewHouse")
    public ResponseResult renewHouse(@RequestBody @Valid HouseRentRenewDTO dto) {
        houseRentRecordService.renewHouse(dto);
        return ResponseResult.success();
    }

    /**
     * 获取入驻企业房屋租赁详情
     *
     * @param id 入驻记录id
     * @return 返回保存成功的对象
     */
    @ApiOperation(value = "获取入驻企业房屋租赁详情", tags = "入驻企业管理")
    @PostMapping("/getRecordById/{id}")
    public ResponseResult<HouseBindVO> getRecordById(@PathVariable("id") String id) {
        HouseBindVO vo = houseRentRecordService.getRecordById(id);
        return ResponseResult.success(vo);
    }

    /**
     * 【房屋入驻记录】分页查询
     *
     * @param query 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "房屋续租记录-分页查询", tags = "入驻企业管理")
    @PostMapping("/pageHouseRenewBindRecord")
    public ResponseResult<PageInfo<HouseBindRenewListVO>> pageHouseRenewBindRecord(@RequestBody @Valid HouseRentRenewRecordQuery query) {
        PageInfo<HouseBindRenewListVO> result = houseRentRecordService.pageHouseRenewBindRecord(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "绑定房屋-房屋下拉列表", tags = "入驻企业管理")
    @GetMapping("/houseNotBindDict/{companyId}")
    public ResponseResult<List<DictVO>> getHouseNotBindDict(@PathVariable("companyId") @NotNull String companyId,
                                                            @RequestParam("buildingId") @NotNull String buildingId) {
        List<DictVO> result = houseRentRecordService.getHouseNotBindDict(companyId, buildingId);
        return ResponseResult.success(result);
    }

}

