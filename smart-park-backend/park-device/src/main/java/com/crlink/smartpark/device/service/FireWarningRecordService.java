package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.dto.FireWarningRecordDTO;
import com.crlink.smartpark.device.entity.FireWarningRecord;
import com.crlink.smartpark.device.model.FireWarningRecordHandleReqDTO;
import com.crlink.smartpark.device.model.query.FireWarningRecordQuery;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;

/**
 * <p>
 * 报警记录 服务类
 * </p>
 * */
public interface FireWarningRecordService extends IService<FireWarningRecord> {

    Integer countFireWarning(String parkOrgId, LocalDateTime startTime, LocalDateTime endTime, Integer status);


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    FireWarningRecordDTO getFireWarningRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<FireWarningRecordDTO> listFireWarningRecordPage(FireWarningRecordQuery query);

    Boolean handleFireRecord(FireWarningRecordHandleReqDTO reqDTO);
}
