package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.dto.CardDTO;
import com.crlink.smartpark.device.entity.Card;
import com.crlink.smartpark.device.model.*;
import com.crlink.smartpark.device.model.query.CardQuery;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 卡片管理 服务类
 * </p>
 * */
public interface CardService extends IService<Card> {

    /**
     * 根据Id查询值
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Card, T> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveCard(CardReqDTO reqDTO) throws Exception;

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    CardDTO getCardById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<CardDTO> listCardPage(CardQuery query);

    Boolean updateStatus(CardUpdateStatusReqDTO statusReqDTO) throws Exception;

    Boolean exchangeCard(CardExchangeReqDTO reqDTO) throws Exception;

    Boolean boundCard(CardBoundReqDTO reqDTO) throws Exception;

    Boolean removeCardById(String id) throws Exception;

    void export(CardQuery query);

    Set<String> findAllCardByEmployeeId(String employeeId);

    Boolean containsEmployeeRelationCard(String employeeId);
}
