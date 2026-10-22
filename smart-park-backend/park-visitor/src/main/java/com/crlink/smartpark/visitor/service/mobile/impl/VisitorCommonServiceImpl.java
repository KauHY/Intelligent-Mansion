package com.crlink.smartpark.visitor.service.mobile.impl;

import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.NoAndIdUtil;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.vo.CompanyClientInfoVO;
import com.crlink.smartpark.visitor.dto.ParkCarDTO;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordVO;
import com.crlink.smartpark.visitor.service.DoorAccessDeviceService;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import com.crlink.smartpark.visitor.service.mobile.VisitorCommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class VisitorCommonServiceImpl implements VisitorCommonService {

    @Resource
    private VisitorRecordService visitorRecordService;
    @Resource
    private DoorAccessDeviceService doorAccessDeviceService;
    @Resource
    private CompanyProvider companyProvider;
    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    @Override
    @Id2objHandle
    public MobileVisitorRecordVO appointmentDetail(String id) {
        VisitorRecord visitorRecord = visitorRecordService.getById(id);
        AssertUtil.requireNotNullOrParamExp(visitorRecord);
        return MyBeanUtils.copy(visitorRecord, MobileVisitorRecordVO::new);
    }

    /**
     * 推送访客信息到设备
     *
     * @param visitorRecord
     */
    @Override
    public void pushVisitor2Device(VisitorRecord visitorRecord) {
        if (StrUtil.isBlank(visitorRecord.getCardNo())) {
            visitorRecord.setCardNo(NoAndIdUtil.generateCardNo());
        }
        if (visitorRecord.getDevicePersonId() == null) {
            visitorRecord.setDevicePersonId(NoAndIdUtil.generateDevicePersonId());
        }
        String errMsg = doorAccessDeviceService.pushVisitor(visitorRecord);
        if (Objects.nonNull(errMsg)) {
            throw new ResolinkRuntimeException(Message.code("60010", errMsg));
        }
    }

    /**
     * 生成人员id 20220808
     *
     * @return id
     */
    @Override
    public Long generatePersonId() {
        return doorAccessDeviceService.generatePersonId();
    }

    /**
     * 生成卡片id 20220808
     *
     * @return id
     */
    @Override
    public Long generateCardId() {
        return doorAccessDeviceService.generateCardId();
    }

    /**
     * 添加员工和卡片权限20220808
     *
     * @param visitorRecord
     */
    @Override
    public void addPersonAndCard(VisitorRecord visitorRecord) {
        String errMsg = doorAccessDeviceService.addPersonAndCard(visitorRecord);
        if (Objects.nonNull(errMsg)) {
            throw new ResolinkRuntimeException(Message.code("60010", errMsg));
        }
    }

    /**
     * 更新访客卡片信息
     *
     * @param visitorRecord
     */
    @Override
    public void updateVisitorCard(VisitorRecord visitorRecord) {
        AssertUtil.requireTrueOrParamExp(StrUtil.isNotBlank(visitorRecord.getCardNo()));
        String errMsg = doorAccessDeviceService.updateVisitorCard(visitorRecord);
        if (Objects.nonNull(errMsg)) {
            throw new ResolinkRuntimeException(Message.code("60010", errMsg));
        }
    }

    /**
     * 删除访客设备权限
     *
     * @param visitorRecord
     */
    @Override
    public void removeVisitorDeviceAuth(VisitorRecord visitorRecord) {
        String errMsg = doorAccessDeviceService.removeVisitorDeviceAuth(visitorRecord);
        if (Objects.nonNull(errMsg)) {
            throw new ResolinkRuntimeException(Message.code("60010", errMsg));
        }
    }

    @Override
    public CompanyClientInfoVO getCurCompanyPublicInfo(String companyId) {
        return companyProvider.getClientBindInfo(companyId).getData();
    }

    /**
     * 添加车辆
     * @param carDTO
     * @return
     */
    @Override
    public String addReservedCar(ParkCarDTO carDTO) {
        return deviceOperateProvider.addCar(carDTO);
    }
}
