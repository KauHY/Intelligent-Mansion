package com.crlink.smartpark.attendance.service;

import com.crlink.smartpark.attendance.entity.FieldworkApplyRecord;
import com.crlink.smartpark.attendance.controller.request.query.FieldworkApplyRecordQuery;
import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.attendance.model.AuditReqDTO;
import com.crlink.smartpark.attendance.model.FieldworkApplyRecordReqDTO;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 外勤申请记录 服务类
 * </p>
 * */
public interface FieldworkApplyRecordService extends IService<FieldworkApplyRecord> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<FieldworkApplyRecord, ?> sValue);

    FieldworkApplyRecord wrapApplyReq(FieldworkApplyRecordReqDTO reqDTO);
    Boolean cancel(String id);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    FieldworkApplyRecordDTO getFieldworkApplyRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<FieldworkApplyRecordDTO> listFieldworkApplyRecordPage(FieldworkApplyRecordQuery query);

    Boolean audit(AuditReqDTO reqDTO);

    void export(FieldworkApplyRecordQuery query);

    Boolean authority();
}
