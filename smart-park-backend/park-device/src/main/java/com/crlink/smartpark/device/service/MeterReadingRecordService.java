package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.device.dto.MeterReadingRecordDTO;
import com.crlink.smartpark.device.entity.MeterReadingRecord;
import com.crlink.smartpark.device.model.MeterReadingReqDTO;
import com.crlink.smartpark.device.model.query.MeterReadingRecordQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 每天抄表记录 服务类
 * </p>
 * */
public interface MeterReadingRecordService extends IService<MeterReadingRecord> {


    FileImportErrorInfoDTO importMeterReadingRecord(MultipartFile file, String parkOrgId);

    @Id2objHandle
    PageInfo<MeterReadingRecordDTO> listMeterReadingRecordPage(MeterReadingRecordQuery query);

    //List<MeterReadingDTO> queryMaxRecord(LocalDateTime start, LocalDateTime end, Set<String> meterIds);
    //
    //List<MeterReadingDTO> queryMinRecord(LocalDateTime start, LocalDateTime end, Set<String> meterIds);

    Boolean modifyMeterReading(MeterReadingReqDTO reqDTO);

    /**
     * 抄表记录导出
     *
     * @param query
     */
    void readingRecordPageDownload(MeterReadingRecordQuery query);

}
