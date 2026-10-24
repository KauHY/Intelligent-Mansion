package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.bean.ValidListDTO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.house.HouseRentRecordSaveDTO;
import com.crlink.smartpark.system.query.CompanyHouseQuery;
import com.crlink.smartpark.system.query.HouseBindRecordQuery;
import com.crlink.smartpark.system.vo.HouseBindListVO;
import com.crlink.smartpark.system.vo.HouseBindRecordListVO;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>
 * 房屋入驻记录表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/house-rent-record", contextId = "HouseRentRecordProvider")
public interface HouseRentRecordProvider {

    /**
     * 房屋绑定
     *
     * @param houses 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/bindHouse/{companyId}")
    ResponseResult bindHouse(@PathVariable("companyId") String companyId, @RequestBody @Valid ValidListDTO<HouseRentRecordSaveDTO> houses);

    /**
     * 房屋解绑
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/unbindHouse/{id}")
    ResponseResult<Boolean> unbindHouse(@PathVariable("id") String id);

    /**
     * 企业已绑定房屋分页查询
     *
     * @param companyHouseQuery 查询参数
     * @return 分页数据
     */
    @PostMapping("/pageCompanyHouseBind")
    ResponseResult<PageInfo<HouseBindListVO>> pageCompanyHouseBind(@RequestBody @Valid CompanyHouseQuery companyHouseQuery);


    /**
     * 【房屋入驻记录】分页查询
     *
     * @param query 查询参数
     * @return 分页数据
     */
    @PostMapping("/pageHouseBindRecord")
    ResponseResult<PageInfo<HouseBindRecordListVO>> pageHouseBindRecord(@RequestBody @Valid HouseBindRecordQuery query);

}
