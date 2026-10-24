package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.customer.entity.ProStockManager;
import com.crlink.smartpark.customer.controller.request.query.ProStockManagerQuery;

import com.crlink.smartpark.customer.dto.ProStockManagerDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.model.dto.ProStockManagerReqDTO;
import com.crlink.smartpark.customer.model.vo.StockManagerSelectVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 库存管理 服务类
 * </p>
 * */
public interface ProStockManagerService extends IService<ProStockManager> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProStockManager, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProStockManager(ProStockManagerReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ProStockManagerDTO getProStockManagerById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProStockManagerDTO> listProStockManagerPage(ProStockManagerQuery query);

    /**
     *  根据物品ids获取库存对象(包含物品名称)
     *
     * @param ids 查询条件
     * @return   实体对象
     */
    List<StockManagerSelectVo> getStockByGoodsIds(Set<String> ids);

    /**
     * 根据物品ID查询库存
     * @param goodsId: 物品ID
     * @return  com.crlink.smartpark.customer.dto.ProStockManagerDTO
     * @MethodName getProStockManagerByGoodsId
     */
    ProStockManagerDTO getProStockManagerByGoodsId(String goodsId);

    /**
     * 库存管理-导入入库
     * @param file:
     * @param parkOrgId:
     * @return  com.crlink.smartpark.common.dto.FileImportErrorInfoDTO
     * @MethodName importGoods
     */
    FileImportErrorInfoDTO importStockIn(MultipartFile file, String parkOrgId);
}
