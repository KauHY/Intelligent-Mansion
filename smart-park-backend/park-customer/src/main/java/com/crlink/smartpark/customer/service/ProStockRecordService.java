package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.customer.controller.request.query.ProGoodsManagerQuery;
import com.crlink.smartpark.customer.entity.ProStockManager;
import com.crlink.smartpark.customer.entity.ProStockRecord;
import com.crlink.smartpark.customer.controller.request.query.ProStockRecordQuery;
import com.crlink.smartpark.customer.model.dto.ProStockRecordReqDTO;
import com.crlink.smartpark.customer.dto.ProStockRecordDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 库存出入库记录 服务类
 * </p>
 * */
public interface ProStockRecordService extends IService<ProStockRecord> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProStockRecord, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProStockRecord(ProStockRecordReqDTO reqDTO);

    /**
     * 统一写入出入库记录
     * @param stockRecord:
     * @return  java.lang.Boolean
     * @MethodName saveOrUpdateStockRecord
     */
    Boolean saveOrUpdateStockRecord(ProStockRecord stockRecord);

    /**
     * 批量导入时 - 批量写入出入库记录
     * @param stockRecordList:
     * @return  void
     * @MethodName saveRecordBatch
     */
    void saveRecordBatch(List<ProStockRecord> stockRecordList);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    ProStockRecordDTO getProStockRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProStockRecordDTO> listProStockRecordPage(ProStockRecordQuery query);

    /**
     * 导出
     *
     * @param reqDTO:
     * @return java.lang.Boolean
     * @MethodName settingMinStock
     */
    void export(ProStockRecordQuery reqDTO);


}
