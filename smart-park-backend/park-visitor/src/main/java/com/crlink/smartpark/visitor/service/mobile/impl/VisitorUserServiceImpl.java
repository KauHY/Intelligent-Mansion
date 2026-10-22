package com.crlink.smartpark.visitor.service.mobile.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.SmsConstant;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyStrUtil;
import com.crlink.smartpark.core.utils.SendSmsUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.crlink.smartpark.visitor.dto.ParkCarDTO;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.entity.ProcessConfig;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.model.dto.AppointmentApplyDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorInfoCompleteDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorRecordQuery;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordListVO;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.service.ProcessConfigService;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import com.crlink.smartpark.visitor.service.mobile.VisitorCommonService;
import com.crlink.smartpark.visitor.service.mobile.VisitorUserService;
import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VisitorUserServiceImpl implements VisitorUserService {
    @Resource
    private EmployeeProvider employeeProvider;
    @Resource
    private VisitorRecordService visitorRecordService;
    @Resource
    private CompanyProvider companyProvider;
    @Resource
    private ProcessConfigService processConfigService;
    @Resource
    private VisitorCommonService visitorCommonService;
    @Resource
    private ParkingLotService parkingLotService;
    @Resource
    private AccessPolicyService accessPolicyService;

    @Override
    public void makeAppointment(AppointmentApplyDTO dto) {
        String companyId = dto.getCompanyId();
        AssertUtil.requireNotNullOrParamExp(companyId);
        EmployeeVO employeeVO = employeeProvider.findEmployeeByCompanyAndTel(dto.getCompanyId(), dto.getInvitorTel());
        AssertUtil.requireNotNull(employeeVO, "70009");
        CompanyVO companyVO = companyProvider.findById(dto.getCompanyId()).getData();
        AssertUtil.requireNotNullOrParamExp(companyVO);
        VisitorRecord visitorRecord = MyBeanUtils.copy(dto, VisitorRecord::new);
        visitorRecord.setInvitorEmployeeId(employeeVO.getId());
        visitorRecord.setVisitorSource(DictUtil.getValue(DictConstant.VisitSourceType.SUBSCRIBE));
        visitorRecord.setParkOrgId(companyVO.getParkOrgId());
        EntityUserUtil.setAll(visitorRecord);
        visitorRecordService.save(visitorRecord);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", visitorRecord.getVisitorName());
        jsonObject.set("telephone", visitorRecord.getVisitorTel());
        CompletableFuture.runAsync(() -> {
            boolean flag = SendSmsUtil.sendSms(SmsConstant.SIGN_NAME, SmsConstant.PARK_VISITOR_CODE, visitorRecord.getInvitorTel(),
                    JSONUtil.toJsonStr(jsonObject));
            System.out.println("发送短信状态==============" + flag);
        });
    }

    @Override
    @Id2objHandle
    public PageInfo<MobileVisitorRecordListVO> listAppointments(VisitorRecordQuery query) {
        query.startPage();
        LambdaQueryWrapper<VisitorRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(query.getCompanyId()), VisitorRecord::getCompanyId, query.getCompanyId());
        wrapper.and(e -> e.eq(VisitorRecord::getCreateBy, SystemUtil.getInstance().getCurrentUserId())
                .or().eq(VisitorRecord::getVisitorTel, SystemUtil.getInstance().getWxUserMobile()));
        wrapper.orderByDesc(VisitorRecord::getVisitDate, VisitorRecord::getCreateTime);
        List<VisitorRecord> list = visitorRecordService.list(wrapper);
        PageInfo pageInfo = new PageInfo(list);
        List<MobileVisitorRecordListVO> voList = MyBeanUtils.copyList(list, MobileVisitorRecordListVO::new);
        pageInfo.setList(voList);
        return pageInfo;
    }

    @Override
    public MobileVisitorRecordListVO detail(String id) {
        VisitorRecord visitorRecord = visitorRecordService.getById(id);
        return MyBeanUtils.copy(visitorRecord, MobileVisitorRecordListVO::new);
    }

    @Override
    public void cancelAppointment(String id) {
        VisitorRecord visitorRecord = visitorRecordService.getById(id);
        AssertUtil.requireTrue(visitorRecord != null
                && DictUtil.getValue(DictConstant.VisitSourceType.SUBSCRIBE).equals(visitorRecord.getVisitorSource())
                && DictUtil.getValue(DictConstant.ProcessStatus.ZERO).equals(visitorRecord.getProcessStatus()), "70013");
        visitorRecord.setProcessStatus(DictUtil.getValue(DictConstant.ProcessStatus.THREE));
        EntityUserUtil.setUpdateBy(visitorRecord);
        visitorRecordService.updateById(visitorRecord);
    }

    @Override
    @Transactional(rollbackFor = ResolinkRuntimeException.class)
    public void completeVisitorInfo(String id, VisitorInfoCompleteDTO dto) {
        VisitorRecord visitorRecord = visitorRecordService.getById(id);
        AssertUtil.requireTrue(visitorRecord != null && DictUtil.getValue(DictConstant.ProcessStatus.ZERO).equals(visitorRecord.getProcessStatus()), "70013");
        BeanUtils.copyProperties(dto, visitorRecord);
        //查找流程配置
        ProcessConfig processConfig = getProcessConfig(visitorRecord.getCompanyId(), visitorRecord.getVisitorTypeId(), DictUtil.getValue(DictConstant.VisitSourceType.INVITE));
        List<String> processNodes = Arrays.asList(processConfig.getProcessNodes().split(","));
        //没人脸照片时，人证核验节点必须存在
        if (StrUtil.isAllBlank(dto.getVisitorFaceImg(), visitorRecord.getVisitorFaceImg())
                && !processNodes.contains(DictUtil.getValue(DictConstant.VisitProcessNode.ID_CARD_VERIFY).toString())) {
            throw new ResolinkRuntimeException("70018");
        }
        //查找车场
        if (StrUtil.isNotBlank(visitorRecord.getCarNum())) {
            List<ParkingLotVO> lotVOList = parkingLotService.getAllowVisitorList();
            AssertUtil.requireTrue(CollUtil.isNotEmpty(lotVOList), "70022");
            Set<String> parkingLotCodes = lotVOList.stream().map(ParkingLotVO::getParkingLotCode).collect(Collectors.toSet());
            visitorRecord.setParkingLotCodes(parkingLotCodes);
        }
        //查找通行策略
        AccessPolicy accessPolicy = accessPolicyService.getById(visitorRecord.getAccessPolicyId());
        AssertUtil.requireTrue(accessPolicy != null && CollUtil.isNotEmpty(accessPolicy.getDeviceIds())
                && DictUtil.getValue(DictConstant.YesOrNo.YES).equals(accessPolicy.getAllowVisitor())
                && DictUtil.getValue(DictConstant.YesOrNo.YES).equals(accessPolicy.getStatus()), "70015");
        visitorRecord.setDeviceIds(accessPolicy.getDeviceIds());
        visitorRecord.setProcessStatus(DictUtil.getValue(DictConstant.ProcessStatus.ONE));
        visitorRecord.setCardNo(MyStrUtil.parseIdToNo(visitorCommonService.generateCardId()));
        visitorRecord.setDevicePersonId(visitorCommonService.generatePersonId());
        visitorRecord.setAuditTime(LocalDateTime.now());
        visitorRecord.setSignOutTime(visitorRecord.getVisitDate().atTime(23, 59, 59));
        visitorRecordService.updateById(visitorRecord);
        log.info("邀约{}补充资料，推送设备", id);
        visitorCommonService.addPersonAndCard(visitorRecord);
        //添加车辆
        if (StrUtil.isNotBlank(visitorRecord.getCarNum())) {
            ParkCarDTO carDTO = new ParkCarDTO();
            carDTO.setCarNum(visitorRecord.getCarNum());
            carDTO.setParkingLotCodes(visitorRecord.getParkingLotCodes());
            carDTO.setAuthStartTime(visitorRecord.getVisitDate().atTime(00, 00, 00));
            carDTO.setAuthEndTime(visitorRecord.getSignOutTime());
            log.info("邀约{}补充资料，添加车辆: {}", id, carDTO);
            String carId = visitorCommonService.addReservedCar(carDTO);
            try {
                Long.parseLong(carId);
            } catch (NumberFormatException e) {
                throw new ResolinkRuntimeException(Message.code("60010", carId));
            }
            VisitorRecord vr = new VisitorRecord();
            vr.setCarId(carId);
            vr.setId(id);
            visitorRecordService.updateById(vr);
        }
        //// TODO: 2022/1/17 短息提醒
    }

    /**
     * 获取公司的流程配置
     *
     * @param companyId
     * @return
     */
    private ProcessConfig getProcessConfig(String companyId, String visitorTypeId, Integer VisitSourceType) {
        LambdaQueryWrapper<ProcessConfig> processConfigWrapper = new LambdaQueryWrapper<>();
        processConfigWrapper.eq(ProcessConfig::getVisitorTypeId, visitorTypeId);
        processConfigWrapper.eq(ProcessConfig::getProcessType, VisitSourceType);
        processConfigWrapper.eq(ProcessConfig::getCompanyId, companyId);
        processConfigWrapper.eq(ProcessConfig::getEnable, DictUtil.getValue(DictConstant.YesOrNo.YES));
        ProcessConfig processConfig = processConfigService.getOne(processConfigWrapper);
        AssertUtil.requireNotNull(processConfig, "70010");
        return processConfig;
    }

}
