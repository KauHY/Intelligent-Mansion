package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.business.dto.BusinessInfoReqDTO;
import com.crlink.smartpark.business.entity.BusinessInfo;
import com.crlink.smartpark.business.query.BusinessInfoQuery;
import com.crlink.smartpark.business.query.WxBusinessInfoQuery;
import com.crlink.smartpark.business.vo.BusinessInfoVO;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 * */
public interface BusinessInfoService extends IService<BusinessInfo> {

    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BusinessInfo, ?> sValue);

    Boolean saveOrUpdateBusinessInfo(BusinessInfoReqDTO dto);

    Boolean deleteBusinessInfoById(String id);

    PageInfo<BusinessInfoVO> listBusinessInfoPage(BusinessInfoQuery businessInfoQuery);

    BusinessInfoVO getBusinessInfoDetailById(String id);

    Boolean updateStatus(UpdateStatusReqDTO statusReqDTO);

    /**
     * C端小程序：招商信息
     *
     * @param query
     * @return
     */
    PageInfo<BusinessInfoVO> wxListBusinessInfoPage(WxBusinessInfoQuery query);

    void export(BusinessInfoQuery query);

    List<DictVO> listBusinessInfoParkDict();

    Integer getBusinessStatus(BusinessInfo businessInfo, LocalDateTime now);

    void checkCanUpdateOrRemove(String id, Boolean isUpdate);

    /**
     * 根据状态获取某个园区下的房源招商id集合
     *
     * @param parkOrgIds
     * @param status
     * @return
     */
    Set<String> getBusinessIdsByStatus(Set<String> parkOrgIds, Integer status);

    //void test();

}
