package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.controller.request.*;
import com.crlink.smartpark.customer.entity.ProReceiveManager;
import com.crlink.smartpark.customer.controller.request.query.ProReceiveManagerQuery;
import com.crlink.smartpark.customer.dto.ProReceiveManagerDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.model.vo.ReceiveDetailVo;
import com.crlink.smartpark.customer.model.vo.StockManagerSelectVo;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 物品领用管理 服务类
 * </p>
 * */
public interface ProReceiveManagerService extends IService<ProReceiveManager> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProReceiveManager, ?> sValue);

    /**
     * 权限判断
     *
     * @return 实体对象
     */
    Boolean auth(String parkOrgId);

    /**
     * 物品领用
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProReceiveManager(ProReceiveApplyDTO reqDTO);

    /**
     * 物品归还
     *
     * @return 实体对象
     */
    Boolean returnGoods(ProReceiveReturnDTO reqDTO);

    /**
     * 选择物品
     *
     * @return 实体对象
     */
    PageInfo<StockManagerSelectVo> selectGoods(ProReceiveGoodsDTO reqDTO);

    /**
     * 取消申请
     *
     * @return 实体对象
     */
    Boolean cancel(ProReceiveCancelDTO reqDTO);


    /**
     * 审核入库
     *
     * @return 实体对象
     */
    void auditReceive(ProReceiveInAuditDTO reqDTO);

    /**
     * 审核入库
     *
     * @return 实体对象
     */
    void auditReturn(ProReceiveOutAuditDTO reqDTO);


    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ReceiveDetailVo getProReceiveManagerById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProReceiveManagerDTO> listProReceiveManagerPage(ProReceiveManagerQuery query);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProReceiveManagerDTO> listProReceivePage(ProReceiveManagerQuery query);
}
