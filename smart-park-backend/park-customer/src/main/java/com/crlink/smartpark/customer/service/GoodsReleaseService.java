package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.controller.request.GoodsReleaseCheckReqDTO;
import com.crlink.smartpark.customer.controller.request.GoodsReleaseReqDTO;
import com.crlink.smartpark.customer.controller.request.query.GoodsReleaseQuery;
import com.crlink.smartpark.customer.dto.GoodsReleaseDTO;
import com.crlink.smartpark.customer.entity.GoodsRelease;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 * */
public interface GoodsReleaseService extends IService<GoodsRelease> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<GoodsRelease, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateGoodsRelease(GoodsReleaseReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    GoodsReleaseDTO getGoodsReleaseById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<GoodsReleaseDTO> listGoodsReleasePage(GoodsReleaseQuery query);

    /**
     * C端小程序
     *
     * @param query
     * @param status
     * @return
     */
    PageInfo<GoodsReleaseDTO> cWxGoodsReleasePage(BaseQuery<String> query, Integer status);

    /**
     * 运营端小程序
     *
     * @param query
     * @param status
     * @return
     */
    PageInfo<GoodsReleaseDTO> bWxGoodsReleasePage(BaseQuery query, Integer status);

    Boolean checkGoodsRelease(GoodsReleaseCheckReqDTO reqDTO);

    Boolean cancelGoodsRelease(String goodsReleaseId);

    void exportGoodsRelease(GoodsReleaseQuery query);
}
