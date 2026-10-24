package com.crlink.smartpark.enterprise.service;

import com.crlink.smartpark.enterprise.entity.TenderRecord;
import com.crlink.smartpark.enterprise.model.query.TenderRecordQuery;
import com.crlink.smartpark.enterprise.model.TenderRecordReqDTO;
import com.crlink.smartpark.industrial.dto.TenderRecordDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 投标记录 服务类
 * </p>
 * */
public interface TenderRecordService extends IService<TenderRecord> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<TenderRecord, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateTenderRecord(TenderRecordReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    TenderRecordDTO getTenderRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<TenderRecordDTO> listTenderRecordPage(TenderRecordQuery query);

    void export(TenderRecordQuery query);

    Boolean confirm(String id);
}
