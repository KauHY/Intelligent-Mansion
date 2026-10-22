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
import com.crlink.smartpark.system.utils.EmployeeInfoUtil;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.crlink.smartpark.visitor.dto.ParkCarDTO;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.entity.AuditPersonConfig;
import com.crlink.smartpark.visitor.entity.ProcessConfig;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.entity.VisitorType;
import com.crlink.smartpark.visitor.model.dto.EmployeeAuditDTO;
import com.crlink.smartpark.visitor.model.dto.QueryProcessNodeDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorInviteDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorRecordQuery;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordListVO;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import com.crlink.smartpark.visitor.service.AuditPersonConfigService;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.service.ProcessConfigService;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import com.crlink.smartpark.visitor.service.VisitorTypeService;
import com.crlink.smartpark.visitor.service.mobile.VisitorCommonService;
import com.crlink.smartpark.visitor.service.mobile.VisitorEmployeeService;
import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VisitorEmployeeServiceImpl implements VisitorEmployeeService {
    @Resource
    private VisitorRecordService visitorRecordService;
    @Resource
    private AccessPolicyService accessPolicyService;
    @Resource
    private ProcessConfigService processConfigService;
    @Resource
    private AuditPersonConfigService auditPersonConfigService;
    @Resource
    private VisitorCommonService visitorCommonService;
    @Resource
    private VisitorTypeService visitorTypeService;
    @Resource
    private CompanyProvider companyProvider;
    @Resource
    private EmployeeProvider employeeProvider;
    @Resource
    private ParkingLotService parkingLotService;

    @Override
    @Transactional(rollbackFor = ResolinkRuntimeException.class)
    public void audit(String id, EmployeeAuditDTO dto) {
        //查找预约记录
        VisitorRecord visitorRecord = visitorRecordService.getById(id);
        AssertUtil.requireTrue(visitorRecord != null && DictUtil.getValue(DictConstant.SubscribeProcessStatus.AUDITING)
                .equals(visitorRecord.getProcessStatus()), "70013");
        EmployeeVO employeeVO = employeeProvider.findEmployeeByCompanyAndTel(visitorRecord.getCompanyId(), SystemUtil.getInstance().getWxUserMobile());
        AssertUtil.requireNotNull(employeeVO, "70017");
        String employeeId = employeeVO.getId();
        //查找审批人配置
        AuditPersonConfig auditPersonConfig = getAuditPersonConfig(visitorRecord.getCompanyId());
        //获取允许审批的员工ID集合
        List<String> allowAuditEmployeeIds = getAllowAuditEmployeeIds(visitorRecord, auditPersonConfig);
        //校验本次审核合法性
        AssertUtil.requireTrue(allowAuditEmployeeIds.contains(employeeId), "70011");

        visitorRecord.setAccessPolicyId(dto.getAccessPolicyId());
        visitorRecord.setVisitorTypeId(dto.getVisitorTypeId());
        visitorRecord.setProcessStatus(dto.getProcessStatus());
        visitorRecord.setAuditTime(LocalDateTime.now());
        visitorRecord.setAuditEmployeeId(employeeId);
        visitorRecord.setAuditRemark(dto.getAuditRemark());
        EntityUserUtil.setUpdateBy(visitorRecord);
        //审核不通过，流程结束
        if (DictUtil.getValue(DictConstant.ProcessStatus.TWO).equals(dto.getProcessStatus())) {
            visitorRecordService.updateById(visitorRecord);
            return;
        }
        //查找车场
        if (StrUtil.isNotBlank(visitorRecord.getCarNum())) {
            List<ParkingLotVO> lotVOList = parkingLotService.getAllowVisitorList();
            AssertUtil.requireTrue(CollUtil.isNotEmpty(lotVOList), "70022");
            Set<String> parkingLotCodes = lotVOList.stream().map(ParkingLotVO::getParkingLotCode).collect(Collectors.toSet());
            visitorRecord.setParkingLotCodes(parkingLotCodes);
        }
        //查找通行策略
        AccessPolicy accessPolicy = accessPolicyService.getById(dto.getAccessPolicyId());
        AssertUtil.requireTrue(accessPolicy != null && CollUtil.isNotEmpty(accessPolicy.getDeviceIds())
                && DictUtil.getValue(DictConstant.YesOrNo.YES).equals(accessPolicy.getAllowVisitor())
                && DictUtil.getValue(DictConstant.YesOrNo.YES).equals(accessPolicy.getStatus()), "70015");
        VisitorType visitorType = visitorTypeService.getById(dto.getVisitorTypeId());
        AssertUtil.requireNotNull(visitorType, "70016");
        visitorRecord.setVisitorTypeName(visitorType.getVisitorTypeName());
        visitorRecord.setCardNo(MyStrUtil.parseIdToNo(visitorCommonService.generateCardId()));
        visitorRecord.setDevicePersonId(visitorCommonService.generatePersonId());
        visitorRecord.setSignOutTime(visitorRecord.getVisitDate().atTime(23, 59, 59));
        visitorRecordService.updateById(visitorRecord);
        log.info("预约访客{}审核通过，推送设备", id);
        visitorRecord.setDeviceIds(accessPolicy.getDeviceIds());
        visitorCommonService.addPersonAndCard(visitorRecord);
        //添加车辆
        if (StrUtil.isNotBlank(visitorRecord.getCarNum())) {
            ParkCarDTO carDTO = new ParkCarDTO();
            carDTO.setCarNum(visitorRecord.getCarNum());
            carDTO.setParkingLotCodes(visitorRecord.getParkingLotCodes());
            carDTO.setAuthStartTime(visitorRecord.getVisitDate().atTime(00, 00, 00));
            carDTO.setAuthEndTime(visitorRecord.getSignOutTime());
            log.info("预约访客{}审核通过，添加车辆: {}", id, carDTO);
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", visitorRecord.getVisitorName());
        jsonObject.set("telephone", visitorRecord.getVisitorTel());
        CompletableFuture.runAsync(() -> {
            SendSmsUtil.sendSms(SmsConstant.SIGN_NAME, SmsConstant.PARK_VISITOR_TEL_CODE, visitorRecord.getVisitorTel(),
                    JSONUtil.toJsonStr(jsonObject));
        });
    }

    @Override
    @Id2objHandle
    public PageInfo<MobileVisitorRecordListVO> listRecord(VisitorRecordQuery query) {
        LambdaQueryWrapper<VisitorRecord> wrapper = buildEmployeeRecordListQueryWrapper(query);
        query.startPage();
        List<VisitorRecord> list = visitorRecordService.list(wrapper);
        List<MobileVisitorRecordListVO> voList = MyBeanUtils.copyList(list, MobileVisitorRecordListVO::new);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = ResolinkRuntimeException.class)
    public String inviteVisitor(VisitorInviteDTO dto) {
        String curCompanyId = dto.getCompanyId();
        EmployeeVO employeeVO = employeeProvider.findEmployeeByCompanyAndTel(curCompanyId, SystemUtil.getInstance().getWxUserMobile());
        AssertUtil.requireNotNull(employeeVO, "70017");
        String curEmployeeId = employeeVO.getId();
        CompanyVO companyVO = companyProvider.findById(curCompanyId).getData();
        AssertUtil.requireNotNullOrParamExp(companyVO);
        VisitorType visitorType = visitorTypeService.getById(dto.getVisitorTypeId());
        AssertUtil.requireNotNullOrParamExp(visitorType);
        List<String> processNodes = this.getInviteProcessNodes(new QueryProcessNodeDTO(curCompanyId, dto.getVisitorTypeId()));
        //不上传人脸图片时，补充信息节点必须存在
        if (StrUtil.isBlank(dto.getVisitorFaceImg())
                && !processNodes.contains(DictUtil.getValue(DictConstant.VisitProcessNode.COMPLETE_INFO).toString())) {
            throw new ResolinkRuntimeException("70014");
        }
        VisitorRecord visitorRecord = MyBeanUtils.copy(dto, VisitorRecord::new);
        visitorRecord.setParkOrgId(companyVO.getParkOrgId());
        visitorRecord.setVisitorTypeName(visitorType.getVisitorTypeName());
        visitorRecord.setCompanyId(companyVO.getId());
        visitorRecord.setInvitorName(EmployeeInfoUtil.getEmployeeNameByIdOrEx(curEmployeeId));
        visitorRecord.setInvitorTel(SystemUtil.getInstance().getWxUserMobile());
        visitorRecord.setInvitorEmployeeId(curEmployeeId);
        visitorRecord.setVisitorSource(DictUtil.getValue(DictConstant.VisitSourceType.INVITE));
        EntityUserUtil.setAll(visitorRecord);
        boolean push2device = false;
        //无须补充资料
        if (!processNodes.contains(DictUtil.getValue(DictConstant.VisitProcessNode.COMPLETE_INFO).toString())) {
            visitorRecord.setProcessStatus(DictUtil.getValue(DictConstant.ProcessStatus.ONE));
            visitorRecord.setCardNo(MyStrUtil.parseIdToNo(visitorCommonService.generateCardId()));
            visitorRecord.setDevicePersonId(visitorCommonService.generatePersonId());
            visitorRecord.setAuditTime(LocalDateTime.now());
            visitorRecord.setSignOutTime(visitorRecord.getVisitDate().atTime(23, 59, 59));
            //查找车场
            if (StrUtil.isNotBlank(visitorRecord.getCarNum())) {
                List<ParkingLotVO> lotVOList = parkingLotService.getAllowVisitorList();
                AssertUtil.requireTrue(CollUtil.isNotEmpty(lotVOList), "70022");
                Set<String> parkingLotCodes = lotVOList.stream().map(ParkingLotVO::getParkingLotCode).collect(Collectors.toSet());
                visitorRecord.setParkingLotCodes(parkingLotCodes);
            }
            push2device = true;
        }
        ////邀请短信发送补充信息提醒
        visitorRecordService.save(visitorRecord);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", visitorRecord.getInvitorName());
        CompletableFuture.runAsync(() -> {
            SendSmsUtil.sendSms(SmsConstant.SIGN_NAME, SmsConstant.PARK_VISITOR_INVITE_CODE, visitorRecord.getVisitorTel(),
                    JSONUtil.toJsonStr(jsonObject));
        });
        if (push2device) {
            log.info("邀约记录{}无需补充资料，添加人和卡", visitorRecord.getId());
            //查找通行策略
            AccessPolicy accessPolicy = accessPolicyService.getById(dto.getAccessPolicyId());
            AssertUtil.requireTrue(accessPolicy != null && CollUtil.isNotEmpty(accessPolicy.getDeviceIds())
                    && DictUtil.getValue(DictConstant.YesOrNo.YES).equals(accessPolicy.getAllowVisitor())
                    && DictUtil.getValue(DictConstant.YesOrNo.YES).equals(accessPolicy.getStatus()), "70015");
            visitorRecord.setDeviceIds(accessPolicy.getDeviceIds());
            log.info("邀约记录{}无需补充资料，推送设备", visitorRecord.getId());
            visitorCommonService.addPersonAndCard(visitorRecord);
            //添加车辆
            if (StrUtil.isNotBlank(visitorRecord.getCarNum())) {
                ParkCarDTO carDTO = new ParkCarDTO();
                carDTO.setCarNum(visitorRecord.getCarNum());
                carDTO.setParkingLotCodes(visitorRecord.getParkingLotCodes());
                carDTO.setAuthStartTime(visitorRecord.getVisitDate().atTime(00, 00, 00));
                carDTO.setAuthEndTime(visitorRecord.getSignOutTime());
                log.info("邀约记录{}无需补充资料，添加车辆: {}", visitorRecord.getId(), carDTO);
                String carId = visitorCommonService.addReservedCar(carDTO);
                try {
                    Long.parseLong(carId);
                } catch (NumberFormatException e) {
                    throw new ResolinkRuntimeException(Message.code("60010", carId));
                }
                VisitorRecord vr = new VisitorRecord();
                vr.setCarId(carId);
                vr.setId(visitorRecord.getId());
                visitorRecordService.updateById(vr);
            }
        }
        return visitorRecord.getId();
    }

    /**
     * 通过类型id获取邀约流程节点
     *
     * @param queryDTO
     * @return
     */
    @Override
    public List<String> getInviteProcessNodes(QueryProcessNodeDTO queryDTO) {
        //查找流程配置
        ProcessConfig processConfig = getProcessConfig(queryDTO.getCompanyId(), queryDTO.getVisitorTypeId(), DictUtil.getValue(DictConstant.VisitSourceType.INVITE));
        if (Objects.isNull(processConfig) || StrUtil.isBlank(processConfig.getProcessNodes())) {
            return Lists.newArrayList();
        }
        return Arrays.asList(processConfig.getProcessNodes().split(","));
    }

    /**
     * 封装员工查看记录列表查询条件
     *
     * @return
     */
    private LambdaQueryWrapper<VisitorRecord> buildEmployeeRecordListQueryWrapper(VisitorRecordQuery query) {
        String curUserId = SystemUtil.getInstance().getCurrentUserId();
        String curCompanyId = query.getCompanyId();
        EmployeeVO employeeVO = employeeProvider.findEmployeeByCompanyAndTel(curCompanyId, SystemUtil.getInstance().getWxUserMobile());
        AssertUtil.requireNotNull(employeeVO, "70017");
        String curEmployeeId = employeeVO.getId();
        LambdaQueryWrapper<VisitorRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(VisitorRecord::getCreateTime);
        wrapper.eq(StrUtil.isNotBlank(curCompanyId), VisitorRecord::getCompanyId, curCompanyId);
        //查找审批人配置
        AuditPersonConfig auditPersonConfig = getAuditPersonConfig(curCompanyId);
        //预约流程字典值
        Integer subscribeProcessDictVal = DictUtil.getValue(DictConstant.VisitSourceType.SUBSCRIBE);
        wrapper.eq(VisitorRecord::getCreateBy, curUserId);
        //被访人审批
        if (DictUtil.getValue(DictConstant.YesOrNo.YES).equals(auditPersonConfig.getInvitorEmployeeAudit())) {
            wrapper.or(e1 -> e1.eq(VisitorRecord::getInvitorEmployeeId, curEmployeeId).eq(VisitorRecord::getVisitorSource, subscribeProcessDictVal));
        } else {
            String auditEmployeeIds = auditPersonConfig.getAuditEmployeeIds();
            if (StringUtils.isNotBlank(auditEmployeeIds)) {
                List<String> allowAuditEmployeeIds = Lists.newArrayList(auditEmployeeIds.split(","));
                //有权限审批
                if (allowAuditEmployeeIds.contains(curEmployeeId)) {
                    wrapper.or(e1 -> e1.eq(VisitorRecord::getVisitorSource, subscribeProcessDictVal));
                }
            }
        }
        return wrapper;
    }

    /**
     * 获取记录允许的所有审核员工
     *
     * @param visitorRecord
     * @param auditPersonConfig
     * @return
     */
    private List<String> getAllowAuditEmployeeIds(VisitorRecord visitorRecord, AuditPersonConfig auditPersonConfig) {
        List<String> allowAuditEmployeeIds;
        if (DictUtil.getValue(DictConstant.YesOrNo.YES).equals(auditPersonConfig.getInvitorEmployeeAudit())) {
            allowAuditEmployeeIds = Lists.newArrayList(visitorRecord.getInvitorEmployeeId());
        } else {
            allowAuditEmployeeIds = Arrays.asList(auditPersonConfig.getAuditEmployeeIds());
        }
        return allowAuditEmployeeIds;
    }

    /**
     * 获取公司的审核配置
     *
     * @param companyId
     * @return
     */
    private AuditPersonConfig getAuditPersonConfig(String companyId) {
        LambdaQueryWrapper<AuditPersonConfig> auditPersonWrapper = new LambdaQueryWrapper<>();
        auditPersonWrapper.eq(AuditPersonConfig::getCompanyId, companyId);
        AuditPersonConfig auditPersonConfig = auditPersonConfigService.getOne(auditPersonWrapper, false);
        AssertUtil.requireNotNull(auditPersonConfig, "70012");
        return auditPersonConfig;
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
