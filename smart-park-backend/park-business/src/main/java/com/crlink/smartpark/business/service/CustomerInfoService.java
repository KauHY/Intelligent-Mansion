package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.business.dto.CustomerDistributionReqDTO;
import com.crlink.smartpark.business.dto.CustomerInfoDTO;
import com.crlink.smartpark.business.dto.CustomerInfoWxReqDTO;
import com.crlink.smartpark.business.dto.chance.AnalysisOverview;
import com.crlink.smartpark.business.dto.chance.BusinessStatisticQuery;
import com.crlink.smartpark.business.entity.CustomerInfo;
import com.crlink.smartpark.business.query.CustomerInfoQuery;
import com.crlink.smartpark.business.vo.CustomerInfoListVO;
import com.crlink.smartpark.business.vo.CustomerInfoVO;
import com.crlink.smartpark.business.vo.CustomerInfoWxVO;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 商机管理—客户表 服务类
 * </p>
 * */
public interface CustomerInfoService extends IService<CustomerInfo> {


    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean saveOrUpdateCustomer(CustomerInfoDTO dto);

    Boolean saveOrUpdateWxCustomer(CustomerInfoWxReqDTO dto);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    CustomerInfoVO getCustomerInfoById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<CustomerInfoListVO> listCustomerPage(CustomerInfoQuery condition);

    Boolean removeCustomerInfo(String id);

    PageInfo<CustomerInfoWxVO> listCustomerWxPage(CustomerInfoQuery query);

//    Boolean wxCreateCustomer(CustomerInfoDTO customerInfoDTO);

    CustomerInfoWxVO getWxCustomerInfoById(String id);

    Boolean customerDistribution(CustomerDistributionReqDTO dto);

    void export(CustomerInfoQuery customerInfoQuery);

    FileImportErrorInfoDTO importCustomerInfo(MultipartFile file, String parkOrgId);

    AnalysisOverview getCustomerIndustry(BusinessStatisticQuery query);
}
