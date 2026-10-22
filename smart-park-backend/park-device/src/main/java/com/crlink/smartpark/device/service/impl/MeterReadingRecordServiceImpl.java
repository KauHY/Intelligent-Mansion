package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.MeterDTO;
import com.crlink.smartpark.device.dto.MeterReadingRecordDTO;
import com.crlink.smartpark.device.entity.MeterReadingRecord;
import com.crlink.smartpark.device.mapper.MeterReadingRecordMapper;
import com.crlink.smartpark.device.model.MeterReadingReqDTO;
import com.crlink.smartpark.device.model.excel.MeterReadingRecordExportDTO;
import com.crlink.smartpark.device.model.excel.MeterRecordImportDTO;
import com.crlink.smartpark.device.model.query.MeterReadingRecordQuery;
import com.crlink.smartpark.device.service.MeterReadingRecordService;
import com.crlink.smartpark.device.service.MeterService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.crlink.smartpark.common.constants.DictConstant.DeviceType.WATER_METER;
import static com.crlink.smartpark.device.common.MeterConstant.MeteTypeEnum.GENERAL_METER;
import static com.crlink.smartpark.device.common.MeterConstant.READING_RECORD_EXPORT_FILENAME;


/**
 * 每天抄表记录 服务实现类
 * */
@Service
public class MeterReadingRecordServiceImpl extends ServiceImpl<MeterReadingRecordMapper, MeterReadingRecord> implements MeterReadingRecordService {


    @Autowired
    private MeterService meterService;


    @Override
    public Boolean modifyMeterReading(MeterReadingReqDTO reqDTO) {
        return this.lambdaUpdate().set(MeterReadingRecord::getCorrectedReading, reqDTO.getCorrectedReading())
                .set(MeterReadingRecord::getRemark, reqDTO.getRemark())
                .set(MeterReadingRecord::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .eq(MeterReadingRecord::getId, reqDTO.getId()).update();
    }

    @Override
    @Id2objHandle
    public PageInfo<MeterReadingRecordDTO> listMeterReadingRecordPage(MeterReadingRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.setParkOrgIds(parkOrgIds);
        query.startPage();
        List<MeterReadingRecord> list = this.list(getConditionWrapper(query));
        List<MeterReadingRecordDTO> result = MyBeanUtils.copyList(list, MeterReadingRecordDTO::new);
        return query.createPageInfo(result);
    }

    private LambdaQueryWrapper<MeterReadingRecord> getConditionWrapper(MeterReadingRecordQuery query) {
        LambdaQueryWrapper<MeterReadingRecord> wrapper = new LambdaQueryWrapper();
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), MeterReadingRecord::getDeviceName, query.getDeviceName());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceId()), MeterReadingRecord::getDeviceId, query.getDeviceId());
        wrapper.eq(Objects.nonNull(query.getDeviceType()), MeterReadingRecord::getDeviceType, query.getDeviceType());
        wrapper.eq(Objects.nonNull(query.getEffectProperty()), MeterReadingRecord::getEffectProperty, query.getEffectProperty());
        wrapper.eq(Objects.nonNull(query.getMeterType()), MeterReadingRecord::getMeterType, query.getMeterType());
        wrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeterReadingRecord::getParkOrgId, query.getParkOrgId());
        if (query.getReadingTimeStart() != null && query.getReadingTimeEnd() != null) {
            wrapper.le(MeterReadingRecord::getReadingTime, query.getReadingTimeEnd());
            wrapper.ge(MeterReadingRecord::getReadingTime, query.getReadingTimeStart());
        }
        wrapper.in(MeterReadingRecord::getParkOrgId, query.getParkOrgIds());
        wrapper.orderByDesc(MeterReadingRecord::getReadingTime, MeterReadingRecord::getDeviceName, MeterReadingRecord::getDeviceId);
        return wrapper;
    }


    @Override
    public FileImportErrorInfoDTO importMeterReadingRecord(MultipartFile file, String parkOrgId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<MeterRecordImportDTO> importListener = new ExcelImportRollbackListener<>(MeterRecordImportDTO.class,
                    //业务参数校验方法
                    meterRecordImportDTO -> validExcelDTO(meterRecordImportDTO, parkOrgId),
                    //数据读取完成后操作
                    meterRecordImportDTOs -> {
                        List<MeterReadingRecord> records = MyBeanUtils.copyList(meterRecordImportDTOs, MeterReadingRecord::new, (s, t) -> {
                            EntityUserUtil.setAll(t);
                        });
                        this.saveOrUpdateBatch(records);
                    });
            ExcelImportUtils.importExcel(file, MeterRecordImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }


    @Override
    public void readingRecordPageDownload(MeterReadingRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        Map<Integer, String> dictTypeMaps = DictUtil.getLabels("ELECTRICITY_METER_EFFECT_TYPE");
        query.setParkOrgIds(parkOrgIds);
        ExcelExportUtils.export(() -> this.list(getConditionWrapper(query)),
                listRecords -> MyBeanUtils.copyList(listRecords, MeterReadingRecordExportDTO::new, (s, t) -> {
                    if (s.getDeviceType().equals(WATER_METER.type)) {
                        t.setEffectType(WATER_METER.desc);
                    } else if (CollectionUtil.isNotEmpty(dictTypeMaps)) {
                        t.setEffectType(dictTypeMaps.get(s.getEffectType()));
                    }
                }),
                READING_RECORD_EXPORT_FILENAME, MeterReadingRecordExportDTO.class);
    }

    /**
     * 导入校验
     *
     * @param dto
     * @param parkOrgId
     * @return
     */
    private String validExcelDTO(MeterRecordImportDTO dto, String parkOrgId) {
        //同一园区下，若表计号码相同，则以待导入的数据为准更新系统已有数据。
        MeterDTO meter = meterService.getMeterByDeviceId(parkOrgId, dto.getDeviceId());
        if (Objects.isNull(meter)) {
            return "设备不存在！";
        }
        BeanUtil.copyProperties(meter, dto);
        //展示数据来源时，表示手动抄表
        dto.setMeterType(GENERAL_METER.type);
        dto.setMeterId(meter.getId());
        return null;
    }

}


