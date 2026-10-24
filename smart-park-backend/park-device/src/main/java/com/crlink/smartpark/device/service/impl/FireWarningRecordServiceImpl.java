package com.crlink.smartpark.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.common.FireControlConstant;
import com.crlink.smartpark.device.dto.FireWarningRecordDTO;
import com.crlink.smartpark.device.entity.FireWarningRecord;
import com.crlink.smartpark.device.mapper.FireWarningRecordMapper;
import com.crlink.smartpark.device.model.FireWarningRecordHandleReqDTO;
import com.crlink.smartpark.device.model.query.FireWarningRecordQuery;
import com.crlink.smartpark.device.service.FireWarningRecordService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


/**
 * 消防报警记录 服务实现类
 * */
@Service
public class FireWarningRecordServiceImpl extends ServiceImpl<FireWarningRecordMapper, FireWarningRecord> implements FireWarningRecordService {


    @Override
    public Integer countFireWarning(String parkOrgId, LocalDateTime startTime, LocalDateTime endTime, Integer status) {
        return this.baseMapper.countFireWarning(parkOrgId, startTime, endTime, status);
    }


    @Override
    @Id2objHandle
    public FireWarningRecordDTO getFireWarningRecordById(String id) {
        FireWarningRecordQuery recordQuery = FireWarningRecordQuery.buildRecordIds(id);
        recordQuery.setDeviceType(8);//消防设备
        List<FireWarningRecordDTO> list = baseMapper.listFireWarningRecordPage(recordQuery);
        if (ObjectUtils.isEmpty(list)) {
            throw new ResolinkRuntimeException("100071");
        }
        return list.get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<FireWarningRecordDTO> listFireWarningRecordPage(FireWarningRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.setParkOrgIds(parkOrgIds);
        query.startPage();
        List<FireWarningRecordDTO> list = baseMapper.listFireWarningRecordPage(query);
        return query.createPageInfo(list);
    }

    @Override
    public Boolean handleFireRecord(FireWarningRecordHandleReqDTO reqDTO) {
        boolean result = this.lambdaUpdate().set(FireWarningRecord::getOpinion, reqDTO.getOpinion())
                .set(FireWarningRecord::getRealReasonType, reqDTO.getRealReasonType())
                .set(FireWarningRecord::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .set(FireWarningRecord::getStatus, FireControlConstant.FireWarningRecordStatusEnum.PROCESSED_STATUS.status)
                .eq(FireWarningRecord::getId, reqDTO.getId()).update();
        return result;
    }

}
