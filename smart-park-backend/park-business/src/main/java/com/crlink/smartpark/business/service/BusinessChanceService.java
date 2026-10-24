package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.controller.request.BusinessChanceOperationDTO;
import com.crlink.smartpark.business.dto.chance.*;
import com.crlink.smartpark.business.entity.BusinessChance;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 商机表 服务类
 * </p>
 * */
public interface BusinessChanceService extends IService<BusinessChance> {


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    BusinessChanceVO getBusinessChanceById(String id);


    Boolean saveOrUpdateBusinessChance(BusinessChanceReqDTO dto);

    PageInfo<BusinessChanceVO> listBusinessChancePage(BusinessChanceQuery query, Integer type);

    PageInfo<FollowBusinessChanceVO> getFollowRecordsById(String id, BaseQuery<String> query);

    List<BusinessChanceRecordVO> getStageProcessById(String id);

    Boolean operateBusinessChance(BusinessChanceOperationDTO dto, Boolean isPc);

    Boolean remindBusinessChance(RemindBusinessChanceDTO dto);

    Boolean followBusinessChance(FollowBusinessChanceDTO dto);

    Boolean removeBusinessChanceById(String id);

    Boolean convertToCustomer(ConvertToCustomerReqDTO reqDTO);

    PageInfo<BusinessChanceVO> listBusinessChanceWxPage(BaseQuery<String> query, Integer type);

    void export(BusinessChanceQuery query, Integer type);

    Integer getChanceCount(String parkOrgId);

    AnalysisOverview getChanceOverview(BusinessStatisticQuery query);

    AnalysisOverview getThreadSource(BusinessStatisticQuery query);

    AnalysisOverview getTransitionRate(BusinessStatisticQuery query);
}
