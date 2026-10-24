package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.company.CompanyQueryDTO;
import com.crlink.smartpark.system.query.CompanyQuery;
import com.crlink.smartpark.system.vo.CompanyClientInfoVO;
import com.crlink.smartpark.system.vo.CompanyListVO;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 入驻企业表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/parkManagement/company", contextId = "CompanyProvider")
public interface CompanyProvider {


    /**
     * 【入驻企业表】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @DeleteMapping("/remove/{id}")
    ResponseResult remove(@PathVariable("id") String id);

    /**
     * 【入驻企业表】根据ID查询
     *
     * @param id 主键
     * @return 入驻企业表实体
     */
    @GetMapping("/findById/{id}")
    ResponseResult<CompanyVO> findById(@PathVariable("id") String id);

    /**
     * 【入驻企业表】分页查询
     *
     * @param companyQuery 查询参数
     * @return 分页数据
     */
    @PostMapping("/listCompanyPage")
    ResponseResult<PageInfo<CompanyListVO>> findListByPage(@RequestBody @Valid CompanyQuery companyQuery);

    /**
     * 【入驻企业表】通过名字查ID集合
     *
     * @param companyName 主键
     */
    @GetMapping("/findIdsByName")
    ResponseResult<Set<String>> findIdsByName(@RequestParam("companyName") String companyName);

    /**
     * 内部接口-入驻企业管理
     * 【入驻企业表】通过企业端用户id查ID集合
     *
     * @param businessUserId 主键
     */
    @GetMapping("/findIdsByBusinessUserId")
    Set<String> findIdsByBusinessUserId(@RequestParam("businessUserId") String businessUserId);

    /**
     * 入驻企业-根据ID查询企业宣传信息
     *
     * @param id 主键
     * @return 入驻企业表实体
     */
    @GetMapping("/findById/clientBindInfo/{id}")
    ResponseResult<CompanyClientInfoVO> getClientBindInfo(@PathVariable("id") String id);

    @PostMapping("/queryHouseColumns")
    Map<String, Map<String, Object>> queryCompanyColumns(@RequestBody CompanyQueryDTO dto);
}
