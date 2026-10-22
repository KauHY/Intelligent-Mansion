package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.controller.request.AdvertisementReqDTO;
import com.crlink.smartpark.customer.controller.request.query.AdvertisementQuery;
import com.crlink.smartpark.customer.dto.AdvertisementDTO;
import com.crlink.smartpark.customer.entity.Advertisement;
import com.crlink.smartpark.customer.vo.WxAdvertisementVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告 服务类
 * </p>
 * */
public interface AdvertisementService extends IService<Advertisement> {

//    /**
//     * 根据Id查询值
//     */
//    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Advertisement, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateAdvertisement(AdvertisementReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    AdvertisementDTO getAdvertisementById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<AdvertisementDTO> listAdvertisementPage(AdvertisementQuery query);

    Boolean removeAdvertisementById(String id);

    List<WxAdvertisementVO> wxAdvertisementList(String parkOrgId);
}
