package com.crlink.smartpark.fee.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.fee.controller.request.ChargeItemReqDTO;
import com.crlink.smartpark.fee.controller.request.StatusReqDTO;
import com.crlink.smartpark.fee.controller.request.query.ChargeItemQuery;
import com.crlink.smartpark.fee.dto.ChargeItemDTO;
import com.crlink.smartpark.fee.entity.ChargeItem;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收费项 服务类
 * </p>
 * */
public interface ChargeItemService extends IService<ChargeItem> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ChargeItem, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateChargeItem(ChargeItemReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ChargeItemDTO getChargeItemById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<ChargeItemDTO> listChargeItemPage(ChargeItemQuery query);

    Boolean updateStatusById(StatusReqDTO chargeItem);

    /**
     * 收费项下拉框
     *
     * @param parkOrgId
     * @return
     */
    List<DictVO> selectChargeItemDict(String parkOrgId);
}
